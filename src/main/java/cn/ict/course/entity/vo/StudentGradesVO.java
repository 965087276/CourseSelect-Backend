package cn.ict.course.entity.vo;

import lombok.Data;

/**
 * @author Jianyong Feng
 **/
@Data
public class StudentGradesVO {
    private String courseCode;
    private String courseName;
    private double credit;
    private String courseType;
    private String courseTeacher;
    private double grade;
}
