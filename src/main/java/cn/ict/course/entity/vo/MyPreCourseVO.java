package cn.ict.course.entity.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.ict.course.entity.bo.MyPreCourseBO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 965087276@qq.com
 * @date 2019/12/3 16:10
 */
@Data
public class MyPreCourseVO {
    private String courseCode;
    private String courseName;
    private double predit;
    private String courseType;
    private String courseTeacher;
    private boolean addToTable;
    List<Schedule> courseSchedules = new ArrayList<>();

    @Data
    public class Schedule {
        private int startWeek;
        private int time;
        private int endWeek;
        private int day;
        private String classroom;
    }

    public void addSchedule(MyPreCourseBO bo) {
        Schedule schedule = new Schedule();
        BeanUtil.copyProperties(bo, schedule);
        courseSchedules.add(schedule);
    }
}
