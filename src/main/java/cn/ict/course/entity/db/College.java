package cn.ict.course.entity.db;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 965087276@qq.com
 * @date 2019/11/26 16:30
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "college")
@Data
public class College extends BaseEntity{
    /**
     * 学院名
     */
    @Column(name = "name", nullable = false)
    private String name;
}
