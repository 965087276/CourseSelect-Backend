package cn.ict.course.entity.dto;

import lombok.Data;

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

    private ScheduleDTO scheduleDTO;
}
