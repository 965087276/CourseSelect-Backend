package cn.ict.course.entity.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 965087276@qq.com
 * @date 2019/11/30 21:40
 */
@Data
public class CourseSelectStatsVO {
    private String type;

    private List<String> requiredCoursesAndCredit = new ArrayList<>();

    private List<String> specializedCoursesAndCredit = new ArrayList<>();

    private List<String> electiveCoursesAndCredit = new ArrayList<>();

    private String requiredCoursesCreditSum;

    private String specializedCoursesCreditSum;

    private String electiveCoursesCreditSum;

    private String totalCredit;

    public CourseSelectStatsVO(String type) {
        this.type = type;
    }
}
