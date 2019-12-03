package cn.ict.course.entity.db;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 学生-预选课表
 * @author 965087276@qq.com
 * @date 2019/11/9 14:10
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "course_preselect")
@Data
public class CoursePreselect extends BaseEntity{
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
}
