package cn.ict.course.service.impl;

import cn.ict.course.entity.bo.MyPreCourseBO;
import cn.ict.course.entity.db.CoursePreSelect;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.MyPreCourseVO;
import cn.ict.course.mapper.CourseMapper;
import cn.ict.course.repo.CoursePreselectRepo;
import cn.ict.course.repo.CourseRepo;
import cn.ict.course.service.CoursePreSelectService;
import com.github.dozermapper.core.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author Jianyong Feng
 **/
@Service
public class CoursePreSelectImpl implements CoursePreSelectService {

    private final CoursePreselectRepo coursePreSelectRepo;
    private final CourseRepo courseRepo;
    private final CourseMapper courseMapper;
    private final Mapper mapper;


    @Autowired
    public  CoursePreSelectImpl(CoursePreselectRepo coursePreSelectRepo,
                                CourseRepo courseRepo,
                                CourseMapper courseMapper,
                                Mapper mapper) {
        this.coursePreSelectRepo = coursePreSelectRepo;
        this.courseRepo = courseRepo;
        this.courseMapper = courseMapper;
        this.mapper = mapper;
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
    public ResponseEntity getPreselectedCourses(String username) {
        List<MyPreCourseVO> courseVOs = new ArrayList<>();
        courseMapper.listMyPreCourse(username)
                .stream()
                .collect(groupingBy(MyPreCourseBO::getCourseCode))
                .forEach((courseCode, schedules) -> {
                    MyPreCourseVO vo = mapper.map(schedules.get(0), MyPreCourseVO.class);
                    schedules.forEach(vo::addSchedule);
                    courseVOs.add(vo);
                });
        return ResponseEntity.ok(courseVOs);
    }

}
