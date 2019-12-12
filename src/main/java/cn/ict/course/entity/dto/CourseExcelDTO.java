package cn.ict.course.entity.dto;

import lombok.Data;

/**
 * @author Jianyong Feng
 **/
@Data
public class CourseExcelDTO {
    // course
    private String college;
    private String courseCode;
    private String courseName;
    private String courseType;
    private int courseHour;
    private double credit;
    private int limitNum;
    private String courseTeacher;
    private String teacherId;
    // schedule
    private String classroom;
    private int startWeek;
    private int endWeek;
    private int day;
    private int time;
}
