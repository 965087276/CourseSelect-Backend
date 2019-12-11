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

    public ScheduleDTO() {
    }

    public ScheduleDTO(int startWeek, int endWeek, int day, int time, String classroom) {
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.day = day;
        this.time = time;
        this.classroom = classroom;
    }
}
