package com.sinon.bluecommunity.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    
    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = "用户名必须是4-16位字母、数字、下划线或减号")
    private String username;

    /**
     * 昵称
     */
    @Size(max = 30, message = "昵称长度不能超过30个字符")
    private String nickname;

    /**
     * 密码
     */
    @JsonIgnore
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", 
            message = "密码必须包含大小写字母和数字，且长度不少于8位")
    private String password;

    /**
     * 用户等级
     * 0: 普通用户
     * 1: VIP用户
     * 2: 管理员
     * 3: 超级管理员
     */
    private Integer level;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    private String email;

    /**
     * 个人简介
     */
    @Size(max = 200, message = "个人简介不能超过200个字符")
    private String bio;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 用户状态：正常
     */
    public static final int STATUS_NORMAL = 0;
    
    /**
     * 用户状态：禁用
     */
    public static final int STATUS_DISABLED = -1;
    
    /**
     * 用户状态：未激活
     */
    public static final int STATUS_INACTIVE = 1;
    
    /**
     * 用户状态：锁定
     */
    public static final int STATUS_LOCKED = 2;

    /**
     * 用户状态
     * 0: 正常
     * -1: 禁用
     * 1: 未激活
     * 2: 锁定
     */
    private Integer status = STATUS_NORMAL;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginAt;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    // 用户等级常量
    public static final int LEVEL_NORMAL = 0;    // 普通用户
    public static final int LEVEL_VIP = 1;       // VIP用户
    public static final int LEVEL_ADMIN = 2;     // 管理员
    public static final int LEVEL_SUPER = 3;     // 超级管理员

    /**
     * 设置密码（只允许写入，不允许读取）
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 判断用户是否被禁用
     */
    public boolean isDisabled() {
        return STATUS_DISABLED == status;
    }
    
    /**
     * 判断用户是否未激活
     */
    public boolean isInactive() {
        return STATUS_INACTIVE == status;
    }
    
    /**
     * 判断用户是否被锁定
     */
    public boolean isLocked() {
        return STATUS_LOCKED == status;
    }
    
    /**
     * 判断用户是否正常
     */
    public boolean isNormal() {
        return STATUS_NORMAL == status;
    }

    /**
     * 检查用户等级
     */
    public boolean isNormalUser() {
        return level != null && level == LEVEL_NORMAL;
    }

    public boolean isVipUser() {
        return level != null && level == LEVEL_VIP;
    }

    public boolean isAdmin() {
        return level != null && level >= LEVEL_ADMIN;
    }

    public boolean isSuperAdmin() {
        return level != null && level >= LEVEL_SUPER;
    }

    /**
     * 初始化默认值
     */
    public void initializeDefaults() {
        this.level = this.level == null ? LEVEL_NORMAL : this.level;
        if (this.nickname == null || this.nickname.trim().isEmpty()) {
            this.nickname = this.username;
        }
    }

    /**
     * 验证用户名是否有效
     */
    public boolean isValidUsername() {
        return username != null && username.matches("^[a-zA-Z0-9_-]{4,16}$");
    }

    /**
     * 验证密码是否有效
     */
    public boolean isValidPassword() {
        return password != null && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$");
    }

    /**
     * 验证邮箱是否有效
     */
    public boolean isValidEmail() {
        return email == null || email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    /**
     * 验证头像URL是否有效
     */
    public boolean isValidAvatarUrl() {
        return avatarUrl == null || avatarUrl.length() <= 255;
    }

    /**
     * 验证所有必填字段
     */
    public boolean isValid() {
        return isValidUsername() && isValidPassword() && isValidEmail() && isValidAvatarUrl();
    }
}