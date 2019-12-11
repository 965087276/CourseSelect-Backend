package cn.ict.course.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Jianyong Feng
 **/
@Data
public class CourseDTO {

    /**
     * 对应course中的teacher_id
     */
    private String teacherId;

    private String courseTeacher;

    private String courseName;

    private String courseType;

    private Integer courseHour;

    private Integer credit;

    private Integer limitNum;

    private String college;

    private List<ScheduleDTO> schedules;

    public CourseDTO() {
    }

    public CourseDTO(String teacherId, String courseTeacher, String courseName, String courseType, Integer courseHour
            , Integer credit, Integer limitNum, String college, List<ScheduleDTO> scheduleList) {
        this.teacherId = teacherId;
        this.courseTeacher = courseTeacher;
        this.courseName = courseName;
        this.courseType = courseType;
        this.courseHour = courseHour;
        this.credit = credit;
        this.limitNum = limitNum;
        this.college = college;
        this.schedules = scheduleList;
    }
}
