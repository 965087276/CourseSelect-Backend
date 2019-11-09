package cn.ict.course.entity.db;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 965087276@qq.com
 * @date 2019/11/9 14:10
 */
@Entity
@Table(name = "course_preselect")
@Data
/**
 * 学生-预选课表
 */
public class CoursePreSelect extends BaseEntity{
    /**
     * 学号
     */
    @Column(name = "student_code", nullable = false)
    private String studentCode;

    /**
     * 课程编码
     */
    @Column(name = "course_code", nullable = false)
    private String courseCode;
}
