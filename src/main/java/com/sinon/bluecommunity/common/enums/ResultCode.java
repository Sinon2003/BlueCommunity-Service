package com.sinon.bluecommunity.common.enums;

import lombok.Getter;

/**
 * 统一的响应状态码
 */
@Getter
public enum ResultCode {
    // 基础响应码
    SUCCESS(200, "操作成功"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "无权限执行此操作"),
    NOT_FOUND(404, "请求的资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    INTERNAL_ERROR(500, "服务器内部错误"),

    // 用户相关错误码 1000-1999
    LOGIN_ERROR(1000, "用户名或密码错误"),
    USERNAME_EXISTS(1001, "用户名已存在"),
    EMAIL_EXISTS(1002, "邮箱已被使用"),
    PASSWORD_ERROR(1003, "密码错误"),
    USER_DISABLED(1004, "账号已被禁用"),
    USER_LOCKED(1005, "账号已被锁定"),
    INVALID_USERNAME(1006, "用户名格式不正确"),
    INVALID_PASSWORD(1007, "密码格式不正确"),
    INVALID_EMAIL(1008, "邮箱格式不正确"),
    OLD_PASSWORD_ERROR(1009, "原密码错误"),
    PASSWORD_SAME(1010, "新密码不能与原密码相同"),
    PASSWORD_NOT_MATCH(1011, "两次输入的密码不一致"),

    // 文件相关错误码 2000-2999
    FILE_NOT_FOUND(2000, "文件不存在"),
    FILE_UPLOAD_ERROR(2001, "文件上传失败"),
    FILE_TYPE_ERROR(2002, "文件类型不支持"),
    FILE_SIZE_EXCEED(2003, "文件大小超出限制"),
    UPLOAD_ERROR(2004, "上传失败"),

    // 帖子相关错误码 3000-3499
    POST_NOT_FOUND(3000, "帖子不存在"),
    POST_ALREADY_DELETED(3001, "帖子已删除"),
    POST_ACCESS_DENIED(3002, "无权访问该帖子"),

    // 评论相关错误码 3500-3999
    COMMENT_NOT_FOUND(3500, "评论不存在"),
    COMMENT_ALREADY_DELETED(3501, "评论已删除"),
    COMMENT_ACCESS_DENIED(3502, "无权操作该评论"),

    // 点赞相关错误码 4000-4499
    ALREADY_LIKED(4000, "已经点赞过"),
    LIKE_NOT_FOUND(4001, "点赞记录不存在"),

    // 关注相关错误码 4500-4999
    ALREADY_FOLLOWED(4500, "已经关注过"),
    FOLLOW_NOT_FOUND(4501, "关注记录不存在"),
    CANNOT_FOLLOW_SELF(4502, "不能关注自己"),

    // 通用业务错误码 5000-5999
    OPERATION_FAILED(5000, "操作失败"),
    INVALID_PARAMETER(5001, "参数不正确"),
    DATA_NOT_FOUND(5002, "数据不存在"),
    DATA_ALREADY_EXISTS(5003, "数据已存在"),
    DATA_STATUS_ERROR(5004, "数据状态错误");

    /**
     * 状态码
     */
    private final int code;
    
    /**
     * 状态信息
     */
    private final String message;

    /**
     * 构造函数
     *
     * @param code    状态码
     * @param message 状态信息
     */
    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取状态码的字符串表示
     * 注：此方法用于向后兼容，新代码建议直接使用getCode()获取int类型的状态码
     *
     * @return 状态码的字符串表示
     */
    public String getCodeAsString() {
        return String.valueOf(code);
    }
}
