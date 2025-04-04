package com.sinon.bluecommunity.user.controller;

import com.sinon.bluecommunity.common.dto.UserProfileDTO;
import com.sinon.bluecommunity.common.entity.User;
import com.sinon.bluecommunity.user.service.UserService;
import com.sinon.bluecommunity.common.utils.AliOssUtil;
import com.sinon.bluecommunity.common.utils.JwtUtil;
import com.sinon.bluecommunity.common.utils.ThreadLocalUtil;
import com.sinon.bluecommunity.common.vo.Result;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 用户控制器
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 用户注册接口
     * @param request 注册请求，包括用户名、密码和确认密码
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<?> register(@RequestBody Map<@NotBlank(message = "不能为空") String, @NotBlank(message = "不能为空") String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String confirmPassword = request.get("confirmPassword");
        String email = request.get("email");

        User existingUser = userService.findByUsername(username);
        if (existingUser != null) {
            return Result.error("用户名已存在");
        }

        if (!password.equals(confirmPassword)) {
            return Result.error("两次密码不一致");
        }

        userService.register(username, password, email);
        return Result.success();
    }

    /**
     * 用户登录接口
     * @param request 登录请求，包括用户名和密码
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        try {
            User user = userService.login(username, password);

            // 生成JWT令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getId());
            claims.put("username", user.getUsername());
            String token = JwtUtil.getToken(claims);

            // 将令牌写入Redis，设置过期时间与JWT一致
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set("TOKEN_" + user.getId(), token, 12, TimeUnit.HOURS);

            return Result.success(token);
        } catch (Exception e) {
            log.debug("登录失败: {}", e.getMessage());
            return Result.error("用户名或密码错误");
        }
    }

    /**
     * 更新用户信息接口
     * @param user 用户信息（部分字段可选）
     * @return 更新结果
     */
    @PutMapping("/update")
    public Result<?> updateUser(@RequestBody User user) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Long userId = Long.valueOf(map.get("userId").toString());
        user.setId(userId);

        // 如果要更新用户名，检查是否已存在
        String username = user.getUsername();
        if (username != null) {
            User existingUser = userService.findByUsername(username);
            if (existingUser != null && !existingUser.getId().equals(userId)) {
                return Result.error("用户名已存在");
            }
        }

        try {
            User updatedUser = userService.updateBasicInfo(user);
            return Result.success(updatedUser);
        } catch (Exception e) {
            log.error("更新用户信息失败", e);
            return Result.error("更新失败");
        }
    }

    /**
     * 获取用户信息接口
     * @return 用户信息
     */
    @GetMapping("/info")
    public Result<?> getUserInfo() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long userId = Long.valueOf(claims.get("userId").toString());
        
        // 获取基本用户信息
        User user = userService.selectById(userId);
        
        // 获取用户统计信息
        UserProfileDTO profile = userService.getUserProfile(userId);
        
        // 组合返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("topicCount", profile.getTopicCount());
        result.put("commentCount", profile.getCommentCount());
        result.put("likeCount", profile.getLikeCount());
        result.put("activityCount", profile.getActivityCount());

        return Result.success(result);
    }

    /**
     * 更新头像接口
     * @return 头像链接
     */
    @PostMapping("/updateAvatar")
    public Result<String> updateAvatar(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.error("文件不能为空");
        }

        Map<String, Object> map = ThreadLocalUtil.get();
        Long userId = Long.valueOf(map.get("userId").toString());

        try {
            // 获取文件原始名
            String originalFilename = file.getOriginalFilename();
            String filename = null;
            if (originalFilename != null) {
                filename = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // 上传文件到OSS并返回URL
            String url = AliOssUtil.uploadFile(filename, file.getInputStream());
            
            // 更新用户头像
            userService.updateAvatar(userId, url);
            return Result.success(url);
        } catch (Exception e) {
            log.error("上传头像失败", e);
            return Result.error("上传失败,服务异常");
        }
    }

    /**
     * 重置密码接口
     * @param request 包含旧密码和新密码
     * @return 重置结果
     */
    @PatchMapping("/resetPassword")
    public Result<?> resetPassword(@RequestBody Map<@NotBlank(message = "不能为空") String, @NotBlank(message = "不能为空") String> request) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        if (claims == null || claims.get("userId") == null) {
            return Result.error("未登录或登录已失效");
        }

        Long userId = Long.valueOf(claims.get("userId").toString());
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");
        String confirmPassword = request.get("confirmPassword");

        if (oldPassword.equals(newPassword)) {
            return Result.error("新密码与旧密码一致");
        }
        if (!newPassword.equals(confirmPassword)) {
            return Result.error("两次密码不一致");
        }

        try {
            boolean success = userService.updatePassword(userId, oldPassword, newPassword);
            if (!success) {
                return Result.error("用户名或密码错误");
            }
            String tokenKey = "TOKEN_" + userId;
            stringRedisTemplate.delete(tokenKey);
            return Result.success("密码重置成功");
        } catch (Exception e) {
            log.error("重置密码失败", e);
            return Result.error("更新失败");
        }
    }

    /**
     * 注销登录
     * @return 注销结果
     */
    @DeleteMapping("/logout")
    public Result<?> logout() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        if (claims == null || claims.get("userId") == null) {
            return Result.error("未登录或登录已失效");
        }

        Long userId = Long.valueOf(claims.get("userId").toString());
        String tokenKey = "TOKEN_" + userId;
        Boolean isDeleted = stringRedisTemplate.delete(tokenKey);

        if (Boolean.TRUE.equals(isDeleted)) {
            return Result.success("注销成功");
        } else {
            return Result.error("注销失败");
        }
    }

    /**
     * 获取当前用户的个人主页部分数据
     * @return 用户个人主页部分数据
     */
    @GetMapping("/profileDTO")
    public Result<UserProfileDTO> getUserProfile() {
        try {
            UserProfileDTO profileDTO = userService.getCurrentUserProfile();
            return Result.success(profileDTO);
        } catch (Exception e) {
            log.error("获取用户主页数据失败", e);
            return Result.error("获取用户主页数据失败");
        }
    }

    /**
     * 获取用户最近动态
     * @param limit 限制条数（可选，默认3条）
     * @return 用户动态列表
     */
    @GetMapping("/recentActions")
    public Result<List<Map<String, Object>>> getUserRecentActions(
            @RequestParam(required = false) Integer limit) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long userId = Long.valueOf(claims.get("userId").toString());
        return Result.success(userService.getUserRecentActions(userId, limit));
    }
}