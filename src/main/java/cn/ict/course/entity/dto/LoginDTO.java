package cn.ict.course.entity.dto;

import lombok.Data;

/**
 * @author Jianyong Feng
 **/
@Data
public class LoginDTO {
    private String userCode;
    private String password;
}