package cn.ict.course.enums;

import lombok.Getter;

/**
 * @author Jianyong Feng
 **/
@Getter
public enum ResultEnum {
    /*
     * 通用状态
     */
    SUCCESS(200, "成功"),
    ERROR(400, "错误"),

    /*
     * 账户问题
     */
    USERNAME_PASSWORD_NOT_MATCH(400, "账号密码不匹配"),
    USER_LOCKED(401, "该账户被冻结"),
    USER_NOT_EXIST(402, "用户不存在"),
    UNKNOWN_EXCEPTION(403, "未知错误");

    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
