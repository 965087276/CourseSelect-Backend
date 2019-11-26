package cn.ict.course.entity.db;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 课程安排表
 * @author 965087276@qq.com
 * @date 2019/11/9 14:10
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "course_schedule")
@Data
public class CourseSchedule extends BaseEntity{
    /**
     * 课程编码
     */
    @Column(name = "course_code", nullable = false)
    private String courseCode;

    /**
     * 起始周
     */
    @Column(name = "start_week", nullable = false)
    private int startWeek;

    /**
     * 结束周
     */
    @Column(name = "end_week", nullable = false)
    private int endWeek;

    /**
     * 上课时间(周几)
     */
    @Column(name = "day", nullable = false)
    private int day;

    /**
     *  第几节
     */
    @Column(name = "time", nullable = false)
    private int time;

    /**
     * 教室
     */
    @Column(name = "classroom", nullable = false)
    private String classroom;
}
