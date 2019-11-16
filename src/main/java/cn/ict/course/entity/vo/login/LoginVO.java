package cn.ict.course.entity.vo.login;

import lombok.Builder;
import lombok.Data;

/**
 * @author Jianyong Feng
 **/
@Data
@Builder
public class LoginVO {
    private int code;

    private String token;
}
