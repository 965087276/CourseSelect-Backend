package cn.ict.course.entity.vo;

import lombok.Data;

/**
 * @author Jianyong Feng
 **/
@Data
public class CourseStudentInfoVO {
    private String studentUsername;
    private String studentRealName;
    private String college;
    private double grade;
    private boolean finished;
}
