package cn.ict.course.entity.dto;

import lombok.Data;

/**
 * @author Jianyong Feng
 **/
@Data
public class UserUpdateDTO {
    private String college;
    private String username;
    private String realName;
    private String email;
    private String phoneNumber;
    private String oldPassword;
    private String password;
}
