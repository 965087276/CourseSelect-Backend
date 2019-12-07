package cn.ict.course.service;

import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.CurriculumVO;
import cn.ict.course.entity.vo.EnableTimeVO;

import java.util.Date;
import java.util.List;

/**
 * @author Jianyong Feng
 **/
public interface CourseSelectService {
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

    /**
     * 在选课时间内学生退课
     * 结果：
     * 1. 退课成功，保存记录到CourseSelect表中，课程已选人数减1
     * 2. 退课失败，返回失败信息
     * @param courseCode 路径参数，课程编码
     * @param username 学生用户名
     * @return 退课结果
     */
    ResponseEntity deleteCourseSelected(String courseCode, String username);

    /**
     * 管理员修改选课开放时间
     * @param startTime 开放时间
     * @param endTime 关闭时间
     * @return 修改是否成功
     */
    ResponseEntity updateEnableTime(Date startTime, Date endTime);

    /**
     * 获取选课开放结束时间
     * @return 开放时间段
     */
    ResponseEntity<EnableTimeVO> getEnableTime();

    /**
     * 获取我的课表
     * @param username 学生用户名
     * @return 课表界面中该学生选的所有课程的课程信息
     */
    ResponseEntity<List<CurriculumVO>> getCurriculum(String username);
}
