package cn.ict.course.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 965087276@qq.com
 * @date 2019/11/30 21:40
 */
@Data
public class CourseSelectStatsVO {
    private String type;

    private List<String> requiredCoursesAndCredit;

    private List<String> specializedCoursesAndCredit;

    private List<String> electiveCoursesAndCredit;

    private String totalCredit;
}
