package cn.ict.course.service.impl;

import cn.ict.course.entity.db.*;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.*;
import cn.ict.course.mapper.CourseMapper;
import cn.ict.course.repo.CourseRepo;
import cn.ict.course.repo.CourseScheduleRepo;
import cn.ict.course.repo.CourseSelectRepo;
import cn.ict.course.repo.SelectionControlRepo;
import cn.ict.course.service.CourseSelectService;
import cn.ict.course.utils.CourseConflictUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jianyong Feng
 **/
@Service
public class CourseSelectServiceImpl implements CourseSelectService {

    private final SelectionControlRepo selectionControlRepo;
    private final CourseSelectRepo courseSelectRepo;
    private final CourseRepo courseRepo;
    private final CourseScheduleRepo scheduleRepo;
    private final CourseMapper courseMapper;


    @Autowired
    public CourseSelectServiceImpl(
                             SelectionControlRepo selectionControlRepo,
                             CourseSelectRepo courseSelectRepo,
                             CourseRepo courseRepo,
                             CourseScheduleRepo scheduleRepo,
                             CourseMapper courseMapper
                             ) {

        this.selectionControlRepo = selectionControlRepo;
        this.courseSelectRepo = courseSelectRepo;
        this.courseRepo = courseRepo;
        this.scheduleRepo = scheduleRepo;
        this.courseMapper = courseMapper;
    }
    /**
     * 学生添加课程
     * <p>
     * 需要判断以下情况：
     * 1. 选课是否开始
     * 2. 课程冲突
     * 2. 选课人数
     * <p>
     * 结果：
     * 1. 选课成功，保存记录到CourseSelect表中，课程已选人数加1
     * 2. 选课失败，返回失败信息
     *
     * @param username   学生用户名
     * @param courseCode 课程编码
     * @return 选课添加结果
     */
    @Override
    @Transactional
    public ResponseEntity addCourseSelect(String username, String courseCode) {

        // 该课程是否已经选择
        CourseSelect courseSelect = courseSelectRepo.findByUsernameAndCourseCode(username, courseCode);
        if (courseSelect != null) {
            return ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR, "课程不可重复选择");
        }
        // 判断选课是否开放
        if (selectCourseClosed()) {
            return ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR, "选课未开放");
        }

        // 学生课表冲突验证
        if (conflictStudent(courseCode, username)) {
            return ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR, "当前课程存在时间冲突");
        }

        // 选课人数限制
        // to-do: 原子操作
        Course courseCurrent = courseRepo.findByCourseCode(courseCode);
        int limitNum = courseCurrent.getLimitNum();
        int selectedNum = courseCurrent.getSelectedNum();

        if (limitNum != 0 && selectedNum >= limitNum) {
            return ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR, "当前课程选课人数已满");
        }

        // 选择课程
        CourseSelect select = new CourseSelect();
        select.setUsername(username);
        select.setCourseCode(courseCode);
        courseSelectRepo.save(select);

        // 课程已选人数加1
        courseCurrent.setSelectedNum(selectedNum + 1);
        courseRepo.save(courseCurrent);

        return ResponseEntity.ok();
    }

    /**
     * 在选课时间内学生退课
     * 结果：
     * 1. 退课成功，保存记录到CourseSelect表中，课程已选人数减1
     * 2. 退课失败，返回失败信息
     *
     * @param courseCode 路径参数，课程编码
     * @param username   学生用户名
     * @return 退课结果
     */
    @Override
    @Transactional
    public ResponseEntity deleteCourseSelected(String courseCode, String username) {
        if (selectCourseClosed()) {
            return ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR, "选课未开放");
        }

        CourseSelect courseSelect = courseSelectRepo.findByUsernameAndCourseCode(username, courseCode);
        if (courseSelect == null) {
            return ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR, "该课程未选择");
        }

        courseSelectRepo.deleteByUsernameAndCourseCode(username, courseCode);

        Course course = courseRepo.findByCourseCode(courseCode);
        int selectedNum = course.getSelectedNum();
        course.setSelectedNum(selectedNum - 1);
        courseRepo.save(course);

        return ResponseEntity.ok();
    }

    /**
     * 管理员修改选课开放时间
     *
     * @param startTime 开放时间
     * @param endTime   关闭时间
     * @return 修改是否成功
     */
    @Override
    @Transactional
    public ResponseEntity updateEnableTime(Date startTime, Date endTime) {
        if (startTime.after(endTime)) {
            return ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR, "起始时间不可以在结束时间之后");
        }
        SelectionControl selectionControl = selectionControlRepo.findById(1L).orElse(null);
        if (selectionControl == null) {
            return ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR, "数据库中无该字段");
        }
        selectionControl.setStartTime(startTime);
        selectionControl.setEndTime(endTime);
        selectionControlRepo.save(selectionControl);
        return ResponseEntity.ok();
    }

    /**
     * 获取选课开放结束时间
     *
     * @return 开放时间段
     */
    @Override
    public ResponseEntity<EnableTimeVO> getEnableTime() {
        SelectionControl selectionControl = selectionControlRepo.findById(1L).orElse(null);
        if (selectionControl == null) {
            return ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR,
                                          "管理员未设置选课开放截止时间");
        }
        Date startTime = selectionControl.getStartTime();
        Date endTime = selectionControl.getEndTime();
        EnableTimeVO enableTime = new EnableTimeVO(startTime, endTime);
        return ResponseEntity.ok(enableTime);
    }

    /**
     * 获取我的课表
     *
     * @param username 学生用户名
     * @return 课表界面中该学生选的所有课程的课程信息
     */
    @Override
    public ResponseEntity<List<CurriculumVO>> getCurriculum(String username) {
        List<CurriculumVO> curriculumList = courseMapper.getSelectedCourses(username);
        return ResponseEntity.ok(curriculumList);
    }

    /**
     * 获取学生所有已选课程的课程编码
     *
     * @param username 学生用户名
     * @return 所有已选课程的课程编码
     */
    @Override
    public ResponseEntity<List<String>> getSelectedCourseCodesByUsername(String username) {
        List<CourseSelect> courseList = courseSelectRepo.findAllByUsername(username);
        List<String> courseCodeList = courseList
                .stream()
                .map(CourseSelect::getCourseCode)
                .collect(Collectors.toList());
        return ResponseEntity.ok(courseCodeList);
    }

    /**
     * 查看学生已选课程
     *
     * @param username 路径参数，学生用户名
     * @return 学生所有已选课程的信息
     */
    @Override
    public ResponseEntity getSelectedCourses(String username) {
        List<MyCourseVO> courses = courseMapper.getMyCourses(username);
        return ResponseEntity.ok(courses);
    }

    /**
     * 获取选择该课的学生信息
     *
     * @param courseCode 课程编码
     * @return 学生信息
     */
    @Override
    public ResponseEntity<List<CourseStudentInfoVO>> getStudentInfoByCourseCode(String courseCode) {
        List<CourseStudentInfoVO> students = courseMapper.getStudentInfoByCourseCode(courseCode);
        return ResponseEntity.ok(students);
    }


    /**
     * 判断选课是否开放
     * @return 当前时间是否处于选课开放时间段
     */
    private boolean selectCourseClosed() {
        SelectionControl selectionControl = selectionControlRepo.findById(1L).orElse(null);
        if (selectionControl == null) {
            return true;
        }
        Date startTime = selectionControl.getStartTime();
        Date endTime = selectionControl.getEndTime();
        Date currentTime = new Date();
        return startTime == null || endTime == null || currentTime.before(startTime) || currentTime.after(endTime);
    }

    /**
     * 判断学生选课时间是否冲突
     *
     * 使用username查询CourseSelect中该学生选择的所有课
     * 查询这些课的课程时间是否与当前所选课程的课程时间冲突
     *
     * @param courseCode 课程编码
     * @param username 学生用户名
     * @return 课程是否冲突
     */
    private boolean conflictStudent(String courseCode, String username) {
        // 当前课程的时间表
        List<CourseSchedule> schedulesCurrent = scheduleRepo.findByCourseCode(courseCode);
        // 判断学生时间是否冲突
        List<CourseSchedule> schedulesPrevious = scheduleRepo.findByUsername(username);
        return CourseConflictUtil.isConflict(schedulesPrevious, schedulesCurrent);
    }
}
