package cn.ict.course.entity.db;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 965087276@qq.com
 * @date 2019/11/9 14:11
 */
@Entity
@Table(name = "teacher")
@Data
public class Teacher extends BaseEntity{
    /**
     * 教工号
     */
    @Column(name = "teacher_code", nullable = false)
    private String teacherCode;

    /**
     * 密码
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * 姓名
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 邮箱
     */
    @Column(name = "mailbox", nullable = false)
    private String mailbox;

    /**
     * 手机号
     */
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    /**
     * 学院
     */
    @Column(name = "college", nullable = false)
    private String college;

    /**
     * 专业
     */
    @Column(name = "major", nullable = false)
    private double major;
}
