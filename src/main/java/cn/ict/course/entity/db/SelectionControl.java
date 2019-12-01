package cn.ict.course.entity.db;

import jdk.vm.ci.meta.Value;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Jianyong Feng
 **/
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "selection_control")
@Data
public class SelectionControl extends BaseEntity{

    @Column(name = "start_time", nullable = false)
    private Date startTime;

    @Column(name = "end_time", nullable = false)
    private Date endTime;

    @Column(name = "start", nullable = false)
    private Boolean start;

}
