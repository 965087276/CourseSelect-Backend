package cn.ict.course.service.impl;

import cn.ict.course.entity.db.Course;
import cn.ict.course.entity.db.CoursePreSelect;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.repo.CoursePreSelectRepo;
import cn.ict.course.repo.CourseRepo;
import cn.ict.course.service.CoursePreSelectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jianyong Feng
 **/
@Service
public class CoursePreSelectImpl implements CoursePreSelectService {

    private final CoursePreSelectRepo coursePreSelectRepo;
    private final CourseRepo courseRepo;

    @Autowired
    public  CoursePreSelectImpl(CoursePreSelectRepo coursePreSelectRepo,
                                 CourseRepo courseRepo) {
        this.coursePreSelectRepo = coursePreSelectRepo;
        this.courseRepo = courseRepo;
    }

    /**
     * 学生添加预选课
     * <p>
     * 结果：
     * 1. 预选课添加成功，保存记录到CoursePreselect表中
     * 2. 预选课添加失败，返回失败信息
     *
     * @param username   学生用户名
     * @param courseCode 课程编码
     * @return 预选课添加结果
     */

    @Override
    @Transactional
    public ResponseEntity addCoursePreselect(String username, String courseCode) {
        CoursePreSelect preSelect = new CoursePreSelect();
        preSelect.setUsername(username);
        preSelect.setCourseCode(courseCode);
        coursePreSelectRepo.save(preSelect);

        CoursePreSelect preselected = coursePreSelectRepo.findByUsernameAndCourseCode(username, courseCode);

        if (preselected != null) {
            return ResponseEntity.ok();
        }

        return ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR, "预选课添加失败");
    }

    /**
     * 学生退预选课
     * <p>
     * 结果：
     * 1. 预选课退课成功，保存记录到CoursePreselect表中
     * 2. 预选课退课失败，返回失败信息
     *
     * @param courseCode 路径参数，预选课课程编码
     * @param username   学生用户名
     * @return 预选课退课结果
     */
    @Override
    @Transactional
    public ResponseEntity DeleteCoursePreselected(String courseCode, String username) {
        coursePreSelectRepo.deleteByUsernameAndCourseCode(username, courseCode);
        CoursePreSelect preSelected = coursePreSelectRepo.findByUsernameAndCourseCode(username, courseCode);
        if(preSelected == null) {
            return ResponseEntity.ok();
        }
        return ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR, "删除预选课失败");
    }

    /**
     * 查看学生预选课程
     *
     * @param username 路径参数，学生用户名
     * @return 学生所有预选课程的信息
     */
    @Override
    public ResponseEntity getPreSelectedCourses(String username) {
        List<CoursePreSelect> preCourses = coursePreSelectRepo.findAllByUsername(username);
        List<String> courseCodesPre = preCourses
                .stream()
                .map(CoursePreSelect::getCourseCode)
                .collect(Collectors.toList());
        List<Course> coursesPre = courseRepo.findByCourseCode(courseCodesPre);
        return ResponseEntity.ok(coursesPre);
    }
}
