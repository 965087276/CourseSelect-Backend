package cn.ict.course.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Jianyong Feng
 **/
@Data
public class LoginVO {
    private Integer code;
    private String message;
    private Serializable authToken;
}
