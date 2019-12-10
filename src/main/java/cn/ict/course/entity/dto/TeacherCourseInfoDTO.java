package cn.ict.course.entity.dto;

import lombok.Data;

/**
 * @author Jianyong Feng
 **/
@Data
public class TeacherCourseInfoDTO {
    private String courseCode;

    private String courseName;

    private String college;

    private int limitNum;

    private int selectedNum;

    private int courseHour;

    private double credit;

    private String courseType;

    private String courseTeacher;

    private int startWeek;

    private int endWeek;

    private int day;

    private int time;

    private String classroom;
}
