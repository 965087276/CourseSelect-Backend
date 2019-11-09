package cn.ict.course.entity.db;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author 965087276@qq.com
 * @date 2019/11/9 14:10
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "gmt_create", nullable = false)
    private Date createdTime;

    @Column(name = "gmt_modified", nullable = false)
    private Date modifiedTime;

    @PrePersist
    protected void prePersist() {
        if (this.createdTime == null) {
            this.createdTime = new Date();
        }
        if (this.modifiedTime == null) {
            this.modifiedTime = new Date();
        }
    }

    @PreUpdate
    protected void preUpdate() {
        this.modifiedTime = new Date();
    }

}