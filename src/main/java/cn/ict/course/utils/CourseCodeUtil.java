package cn.ict.course.utils;

import cn.hutool.core.util.RandomUtil;

import java.util.Date;

/**
 * @author Jianyong Feng
 **/
public class CourseCodeUtil {
    private static final String COURSE_CODE_PREFIX = "UCAS";
    private static final int RANDOM_MAX = 1000000;

    /**
     * 生成一个随机的课程编码
     * @return 随机课程编码
     */
    public static String generateCourseCode() {
        String currTime = new Date().toString();
        String randomStr = RandomUtil.randomString(currTime, 5).toUpperCase();
        int randomInt = RandomUtil.randomInt(0, RANDOM_MAX);
        return COURSE_CODE_PREFIX + randomStr + randomInt;
    }
}
