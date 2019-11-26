package cn.ict.course.entity.db;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 课程表
 * @author 965087276@qq.com
 * @date 2019/11/9 14:10
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "course")
@Data
public class Course extends BaseEntity {
    /**
     * 课程编码
     */
    @Column(name = "course_code", nullable = false)
    private String courseCode;

    /**
     * 课程名
     */
    @Column(name = "course_name", nullable = false)
    private String courseName;

    /**
     * 课程属性(核心课、普及课)
     * 核心课、普及课、研讨课等等
     */
    @Column(name = "course_type", nullable = false)
    private String courseType;

    /**
     * 开课学院
     */
    @Column(name = "college", nullable = false)
    private String college;

    /**
     * 课时
     */
    @Column(name = "course_hour", nullable = false)
    private int courseHour;

    /**
     * 学分
     */
    @Column(name = "credit", nullable = false)
    private double credit;

    /**
     * 限选
     */
    @Column(name = "limit_num", nullable = false)
    private int limitNum;

    /**
     * 已选
     */
    @Column(name = "selected_num", nullable = false)
    private int selectedNum = 0;

    /**
     * 授课方式
     */
    @Column(name = "teaching_type", nullable = false)
    private String teachingType;

    /**
     * 考核方式
     */
    @Column(name = "exam_type", nullable = false)
    private String examType;

    /**
     * 授课教师工号
     * 此处为教师用户的username
     * 外键关联查询教师表
     */
    @Column(name = "teacher_id", nullable = false)
    private String teacherId;

    /**
     * 授课教师的名字
     * 冗余存储，如果user表中修改了教师的名字，course表也需要修改
     */
    @Column(name = "course_teacher", nullable = false)
    private String courseTeacher;
}
