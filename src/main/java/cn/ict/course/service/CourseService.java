package cn.ict.course.service;

import cn.ict.course.entity.dto.CourseDTO;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.CourseVO;

import java.util.Date;
import java.util.List;

/**
 * @author Jianyong Feng
 **/
public interface CourseService {
    ResponseEntity addCourse(CourseDTO courseDTO);

    List<CourseVO> getCourseList(String college, String courseType, String CourseName, Integer day, Integer time);





//
//    /**
//     * 查看学生已选课程
//     * @param username 路径参数，学生用户名
//     * @return 学生所有已选课程的信息
//     */
//    ResponseEntity getSelectedCourses(String username);
//
//    /**
//     * 获取课程的上课时间、教室信息
//     * @param courseCode 路径参数，课程编码
//     * @return 该课程的Schedule
//     */
//    ResponseEntity getCourseSchedules(String courseCode);
//
//    ResponseEntity getWeekSchedule(String username, Integer week);


}
