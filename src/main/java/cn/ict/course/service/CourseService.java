package cn.ict.course.service;

import cn.ict.course.entity.db.Course;
import cn.ict.course.entity.dto.CourseDTO;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.CourseVO;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @author Jianyong Feng
 **/
public interface CourseService {
    ResponseEntity addCourse(CourseDTO courseDTO);

    List<CourseVO> getCourseList();

    /**
     * 学生添加预选课
     *
     * 结果：
     * 1. 预选课添加成功，保存记录到CoursePreselect表中
     * 2. 预选课添加失败，返回失败信息
     *
     * @param username 学生用户名
     * @param courseCode 课程编码
     * @return 预选课添加结果
     */
    ResponseEntity addCoursePreselect(String username, String courseCode);

    /**
     * 学生添加课程
     *
     * 需要判断以下情况：
     * 1. 选课是否开始
     * 2. 课程冲突
     * 2. 选课人数
     *
     * 结果：
     * 1. 选课成功，保存记录到CourseSelect表中，课程已选人数加1
     * 2. 选课失败，返回失败信息
     * @param username 学生用户名
     * @param courseCode 课程编码
     * @return 选课添加结果
     */
    ResponseEntity addCourseSelect(String username, String courseCode);
//
//    /**
//     * 学生退课
//     * 结果：
//     * 1. 退课成功，保存记录到CourseSelect表中，课程已选人数减1
//     * 2. 退课失败，返回失败信息
//     * @param courseCode 路径参数，课程编码
//     * @param username 学生用户名
//     * @return 退课结果
//     */
//    ResponseEntity dropCourse(String courseCode, String username);

    /**
     * 学生退预选课
     *
     * 结果：
     * 1. 预选课退课成功，保存记录到CoursePreselect表中
     * 2. 预选课退课失败，返回失败信息
     * @param courseCode 路径参数，预选课课程编码
     * @param username 学生用户名
     * @return 预选课退课结果
     */
    ResponseEntity DeleteCoursePreselected(String courseCode, String username);

    /**
     * 查看学生预选课程
     * @param username 路径参数，学生用户名
     * @return 学生所有预选课程的信息
     */
    ResponseEntity getPreSelectedCourses(String username);
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

    /**
     * 管理员修改选课开放时间
     * @param startTime 开放时间
     * @param endTime 关闭时间
     * @return 修改是否成功
     */
    ResponseEntity updateEnableTime(Date startTime, Date endTime);
}
