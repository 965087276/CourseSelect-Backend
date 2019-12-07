package cn.ict.course.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author Jianyong Feng
 **/
@Data
public class TeacherCourseTableVO {
    private String courseName;
    private String courseCode;
    private int startWeek;
    private int endWeek;
    private String classroom;
    private int time;
    private int day;
}
