package cn.ict.course.entity.db;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Jianyong Feng
 **/
@Entity
@Table(name = "user")
@Data
public class User extends BaseEntity {
    /**
     * 工号
     */
    @Column(name = "user_code", nullable = false)
    private String userCode;

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
     * 单位
     */
    @Column(name = "unit", nullable = false)
    private String unit;

    /**
     * 专业
     */
    @Column(name = "major", nullable = false)
    private String major;

    /**
     * 类型
     */
    @Column(name = "type", nullable = false)
    private String type;
}
