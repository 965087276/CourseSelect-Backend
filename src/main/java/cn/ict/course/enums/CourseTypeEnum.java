package cn.ict.course.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

/**
 * @author 965087276@qq.com
 * @date 2019/11/30 22:05
 */
@Getter
public enum CourseTypeEnum {

    REQUIRED_COURSE("公共必修课"),

    SPECIALIZED_CORE_COURSE("专业核心课"),

    SPECIALIZED_POPULARIZATION_COURSE("专业普及课"),

    SPECIALIZED_DISCUSSION_COURSE("专业研讨课"),

    ELECTIVE_COURSE("公共选修课");

    private String name;

    CourseTypeEnum(String name) {
        this.name = name;
    }

    public static CourseTypeEnum getCourseType(String courseType) {
        for (CourseTypeEnum courseTypeEnum : CourseTypeEnum.values()) {
            if (courseTypeEnum.getName().equals(courseType)) {
                return courseTypeEnum;
            }
        }
        return null;
    }
}
