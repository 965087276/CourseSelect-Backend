package cn.ict.course.entity.dto;

import lombok.Data;

/**
 * @author Jianyong Feng
 **/
@Data
public class ScheduleDTO {
    private Integer startWeek;
    private Integer endWeek;
    private Integer day;
    private Integer time;
    private String classroom;
}
