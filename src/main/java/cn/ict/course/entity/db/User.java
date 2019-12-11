package cn.ict.course.entity.db;

import com.github.dozermapper.core.Mapping;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Jianyong Feng
 **/
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user")
@Data
public class User extends BaseEntity {
    /**
     * 默认工号，如学工号/教师工号
     */
    @Column(name = "username", nullable = false)
    private String username;

    /**
     * 真实姓名
     */
    @Column(name = "real_name", nullable = false)
    private String realName;

    /**
     * 邮箱
     */
    @Column(name = "email", nullable = false)
    private String email;

    /**
     * 手机号
     */
    @Column(name = "phone_number", nullable = false)

    private String phoneNumber;
    /**
     * 密码
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * 盐
     */
    @Column(name = "salt", nullable = false)
    private String salt;

    /**
     * 学院
     */
    @Column(name = "college", nullable = false)
    private String college;

    /**
     * 专业
     */
    @Column(name = "major", nullable = false)
    private String major;

    /**
     * 类型
     * student - 学生
     * admin - 管理员
     * teacher - 教师
     */
    @Column(name = "role", nullable = false)
    private String role;
}
