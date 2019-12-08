package cn.ict.course.entity.bo;

import lombok.Data;

/**
 * @author Jianyong Feng
 **/
@Data
public class GradesInfoBO {
    private String courseCode;
    private String studentId;
    private double grade;
}
