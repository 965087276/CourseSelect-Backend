package cn.ict.course.service;

import cn.ict.course.entity.db.Course;
import cn.ict.course.entity.dto.CourseDTO;
import cn.ict.course.entity.vo.CourseVO;

import java.util.List;

/**
 * @author Jianyong Feng
 **/
public interface CourseService {
    void addCourse(CourseDTO courseDTO);

    List<CourseVO> getCourseList();
}
