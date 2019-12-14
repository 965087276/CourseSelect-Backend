package cn.ict.course.entity.bo;

import lombok.Data;

/**
 * @author 965087276@qq.com
 * @date 2019/12/3 15:57
 */
@Data
public class MyPreCourseBO {
    private String courseCode;
    private String courseName;
    private double credit;
    private String courseType;
    private String courseTeacher;
    private boolean addToTable;
    private int startWeek;
    private int time;
    private int endWeek;
    private int day;
    private String classroom;
}
