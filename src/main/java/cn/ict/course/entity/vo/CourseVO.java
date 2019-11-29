package cn.ict.course.entity.vo;

import cn.ict.course.entity.db.CourseSchedule;
import cn.ict.course.entity.dto.ScheduleDTO;
import lombok.Data;

import java.util.List;

/**
 * @author Jianyong Feng
 **/
@Data
public class CourseVO {
    private String courseCode;

    private String courseName;

    private String college;

    private Integer limitNum;

    private Integer seletedNum;

    private Integer credit;

    private String courseType;

    private String courseTeacher;

    private List<ScheduleDTO> schedules;
}
