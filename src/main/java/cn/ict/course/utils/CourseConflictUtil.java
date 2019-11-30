package cn.ict.course.utils;

import cn.ict.course.entity.db.CourseSchedule;

import java.util.List;

/**
 * @author Jianyong Feng
 **/
public class CourseConflictUtil {
    private static boolean courseConflict(List<CourseSchedule> schedules) {
        int len = schedules.size();
        for (int i = 0; i < len; i++) {
            for (int j = i+1; j < len; j++) {
                if (isConflict(schedules.get(i), schedules.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isConflict(CourseSchedule schedule1, CourseSchedule schedule2) {
        if (schedule1.getEndWeek() < schedule2.getStartWeek()
                || schedule2.getEndWeek() < schedule1.getStartWeek()) {
            return true;
        }
        return schedule1.getDay() == schedule2.getDay() && schedule1.getTime() == schedule2.getTime();
    }

    public static boolean isConflict(CourseSchedule course, List<CourseSchedule> coursePrevious) {
        for (CourseSchedule myCourse : coursePrevious) {
            if (isConflict(course, myCourse)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isConflict(List<CourseSchedule> schedulesPrevious, List<CourseSchedule> schedulesCurrent) {
        schedulesPrevious.addAll(schedulesCurrent);
        return courseConflict(schedulesPrevious);
    }

}
