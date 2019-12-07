package cn.ict.course.service;

import cn.ict.course.entity.dto.CourseDTO;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.CourseVO;
import cn.ict.course.entity.vo.TeacherCourseTableVO;

import java.util.Date;
import java.util.List;

/**
 * @author Jianyong Feng
 **/
public interface CourseService {
    ResponseEntity addCourse(CourseDTO courseDTO);

    List<CourseVO> getCourseList(String college, String courseType, String CourseName, Integer day, Integer time);

    /**
     * 通过课程编码删除课程
     * @param courseCode 课程编码
     * @return 删除结果
     */
    ResponseEntity deleteCourseByCourseCode(String courseCode);


    ResponseEntity<List<TeacherCourseTableVO>> getTeacherCourseTable(String teacherId);


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
