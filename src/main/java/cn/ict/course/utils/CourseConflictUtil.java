package cn.ict.course.utils;

import cn.ict.course.entity.db.Course;
import cn.ict.course.entity.db.CourseSchedule;

import java.util.List;

/**
 * @author Jianyong Feng
 **/
public class CourseConflictUtil {
    public static boolean courseConflict(List<CourseSchedule> schedules) {
        int len = schedules.size();
        for (int i = 0; i < len; i++) {
            for (int j = i+1; j < len; j++) {
                if (isConflict(schedules.get(i), schedules.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isConflict(CourseSchedule course1, CourseSchedule course2) {
        if (course1.getEndWeek() < course2.getStartWeek()
                || course2.getEndWeek() < course1.getStartWeek()) {
            return true;
        }
        return course1.getDay() == course2.getDay() && course1.getTime() == course2.getTime();
    }

    public static boolean isConflict(CourseSchedule course, List<CourseSchedule> coursePrevious) {
        for (CourseSchedule myCourse : coursePrevious) {
            if (isConflict(course, myCourse)) {
                return false;
            }
        }
        return true;
    }

}
