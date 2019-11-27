package cn.ict.course.exception;

import cn.ict.course.enums.ResultEnum;

/**
 * @author Jianyong Feng
 **/
public class ResultException extends RuntimeException{
    private Integer code;

    public ResultException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public ResultException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
