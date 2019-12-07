package cn.ict.course.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author Jianyong Feng
 **/
@Data
public class EnableTimeVO {
    Date startTime;
    Date endTime;
    public EnableTimeVO(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
