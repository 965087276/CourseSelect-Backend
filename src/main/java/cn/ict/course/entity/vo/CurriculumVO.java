package cn.ict.course.entity.vo;

import lombok.Data;
import org.apache.poi.hssf.record.pivottable.PageItemRecord;

/**
 * 获取我的课表所需对象
 * @author Jianyong Feng
 **/
@Data
public class CurriculumVO {
    private String courseName;
    private String courseCode;
    private String courseTeacher;
    private int startWeek;
    private int endWeek;
    private int time;
    private String classroom;
    private int day;
}
