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

    public ScheduleDTO(Integer startWeek, Integer endWeek, Integer day, Integer time, String classroom) {
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.day = day;
        this.time = time;
        this.classroom = classroom;
    }
}
