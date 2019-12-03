package cn.ict.course.service;

import cn.ict.course.entity.http.ResponseEntity;

/**
 * @author Jianyong Feng
 **/
public interface CoursePreSelectService {
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

}
