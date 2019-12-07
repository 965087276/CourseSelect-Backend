package cn.ict.course.entity.vo;

import lombok.Data;

/**
 * @author Jianyong Feng
 **/
@Data
public class MyCourseVO {
    private String courseCode;
    private String courseName;
    private float credit;
    private String courseType;
    private String courseTeacher;
    private int courseHour;
}
