package cn.ict.course.entity.db;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 学生-选课表
 * @author 965087276@qq.com
 * @date 2019/11/9 14:11
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "course_select")
@Data
public class CourseSelect extends BaseEntity{
    /**
     * 默认工号，如学工号/教师工号
     */
    @Column(name = "username", nullable = false)
    private String username;

    /**
     * 课程编码
     */
    @Column(name = "course_code", nullable = false)
    private String courseCode;

    /**
     * 是否结课（是否有成绩)
     */
    @Column(name = "is_finish", nullable = false)
    private boolean finished = false;

    /**
     * 成绩
     */
    @Column(name = "grade", nullable = false)
    private double grade = 0;
}
