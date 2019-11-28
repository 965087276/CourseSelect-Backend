package cn.ict.course.entity.dto;

import lombok.Data;

/**
 * @author Jianyong Feng
 **/
@Data
public class ScheduleDTO {
    private int startWeek;
    private int endWeek;
    private int day;
    private int time;
    private String classroom;
}
