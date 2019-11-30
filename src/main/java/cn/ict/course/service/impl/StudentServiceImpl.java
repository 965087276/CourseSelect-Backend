package cn.ict.course.service.impl;

import cn.ict.course.entity.db.Course;
import cn.ict.course.entity.vo.CourseSelectStatsVO;
import cn.ict.course.enums.CourseTypeEnum;
import cn.ict.course.repo.CourseSelectRepo;
import cn.ict.course.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 965087276@qq.com
 * @date 2019/11/30 21:44
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private CourseSelectRepo courseSelectRepo;

    @Override
    public List<CourseSelectStatsVO> getCoursesSelectedStats(String username) {

        CourseSelectStatsVO courseSelectVO = new CourseSelectStatsVO("选课情况");
        CourseSelectStatsVO coursePassedVO = new CourseSelectStatsVO("获取学分");
        List<CourseSelectStatsVO> list = new ArrayList<>();
        list.add(courseSelectVO); list.add(coursePassedVO);

        // 获取学生选的所有课
        List<Course> courseSelectList = courseSelectRepo.listSelectedCoursesByUsername(username);
        generateCourseStatsVO(courseSelectList, courseSelectVO);

        // 获取学生通过的所有课
        List<Course> coursePassedList = courseSelectRepo.listPassedCoursesByUsername(username);
        generateCourseStatsVO(coursePassedList, coursePassedVO);

        return list;
    }

    private void generateCourseStatsVO(List<Course> courses, CourseSelectStatsVO vo) {
        // 选课学分
        double requiredCourseCredit = 0;
        double electiveCourseCredit = 0;
        double specializedCourseCredit = 0;

        for (Course course : courses) {
            CourseTypeEnum courseTypeEnum = CourseTypeEnum.getCourseType(course.getCourseType());
            switch (courseTypeEnum) {
                case REQUIRED_COURSE:
                    vo.getRequiredCoursesAndCredit().add(formatCourseNameAndCredit(course));
                    requiredCourseCredit += course.getCredit();
                    break;
                case ELECTIVE_COURSE:
                    vo.getElectiveCoursesAndCredit().add(formatCourseNameAndCredit(course));
                    electiveCourseCredit += course.getCredit();
                    break;
                case SPECIALIZED_CORE_COURSE: case SPECIALIZED_DISCUSSION_COURSE: case SPECIALIZED_POPULARIZATION_COURSE:
                    vo.getElectiveCoursesAndCredit().add(formatCourseNameAndCredit(course));
                    specializedCourseCredit += course.getCredit();
                    break;
            }
        }
        vo.setRequiredCoursesCreditSum(formatCredit(requiredCourseCredit));
        vo.setElectiveCoursesCreditSum(formatCredit(electiveCourseCredit));
        vo.setSpecializedCoursesCreditSum(formatCredit(specializedCourseCredit));
        vo.setTotalCredit(formatCredit(requiredCourseCredit + electiveCourseCredit + specializedCourseCredit));
    }

    private String formatCourseNameAndCredit(Course course) {
        return course.getCourseName() + "（" + formatCredit(course.getCredit()) + ")";
    }

    private String formatCredit(double credit_) {
        String credit = String.format("%.1f", credit_);
        // 如果小数点最后一位为0，则学分为整数。去掉小数部分
        if (credit.charAt(credit.length()-1) == '0') {
            credit = credit.substring(0, credit.length()-2);
        }
        return credit + "学分";
    }
}
