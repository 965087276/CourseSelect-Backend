package cn.ict.course.service.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.ict.course.constants.CourseConflictConst;
import cn.ict.course.entity.bo.GradesInfoBO;
import cn.ict.course.entity.bo.StudentGradesExcelBO;
import cn.ict.course.entity.db.Course;
import cn.ict.course.entity.db.CourseSchedule;
import cn.ict.course.entity.db.CourseSelect;
import cn.ict.course.entity.db.SelectionControl;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.*;
import cn.ict.course.mapper.CourseMapper;
import cn.ict.course.repo.CourseRepo;
import cn.ict.course.repo.CourseScheduleRepo;
import cn.ict.course.repo.CourseSelectRepo;
import cn.ict.course.repo.SelectionControlRepo;
import cn.ict.course.service.CourseSelectService;
import cn.ict.course.utils.CourseConflictUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Jianyong Feng
 **/
@Slf4j
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
        String conflictedCourseCode = getConflictedCourseCode(courseCode, username);
        if (!conflictedCourseCode.equals(CourseConflictConst.NO_COURSE_SCHEDULE_CONFLICT)) {
            String conflictedCourseName = courseRepo.findByCourseCode(conflictedCourseCode).getCourseName();
            return ResponseEntity.error(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "当前课程与" + conflictedCourseName + "的上课时间冲突"
            );
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
     * 获取选课状态（是否开放）
     *
     * @return 选课状态
     */
    @Override
    public ResponseEntity getCourseSelectStatus() {
        SelectionControl selectionControl = selectionControlRepo.findById(1L).orElse(null);
        if (selectionControl == null) {
            return ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR,
                                          "管理员未设置选课状态");
        }
        boolean status = !selectCourseClosed();
        return ResponseEntity.ok(status);
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
     * 使用学生用户名获得学生的成绩
     *
     * @param username 学生用户名
     * @return 学生成绩
     */
    @Override
    public ResponseEntity<List<StudentGradesVO>> getStudentGradesByUsername(String username) {
        List<StudentGradesVO> grades = courseMapper.getStudentGradesByUsername(username);
        if (grades.size() == 0) {
            return ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR,
                                          "暂无课程成绩");
        }
        return ResponseEntity.ok(grades);
    }

    /**
     * 录入成绩
     *
     * @param gradesInfoBO 成绩信息
     * @return 录入结果
     */
    @Override
    @Transactional
    public ResponseEntity updateStudentGrades(GradesInfoBO gradesInfoBO) {
        String username = gradesInfoBO.getStudentId();
        String courseCode = gradesInfoBO.getCourseCode();
        double grade = gradesInfoBO.getGrade();
        CourseSelect courseSelect = courseSelectRepo.findByUsernameAndCourseCode(username, courseCode);
        if (courseSelect == null) {
            return ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR,
                                          "无法查询到相关列表");
        }
        courseSelect.setFinished(true);
        courseSelect.setGrade(grade);
        courseSelectRepo.save(courseSelect);
        return ResponseEntity.ok();
    }

    /**
     * 通过excel导入成绩
     *
     * @param courseCode 课程编码
     * @param file       excel文件
     * @return 导入是否成功
     */
    @Override
    @Transactional
    public ResponseEntity updateStudentGradesByFile(String courseCode, MultipartFile file) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<Map<String, Object>> readAll = reader.readAll();
        Map<String, String> map = new HashMap<>();
        map.put("学号", "username");
        map.put("姓名", "realName");
        map.put("学院", "college");
        map.put("成绩", "grade");
        List<Map<String, Object>> readTransformed = readAll.stream()
                .map(list -> transform(list, map))
                .collect(Collectors.toList());
        ObjectMapper objectMapper = new ObjectMapper();
        List<StudentGradesExcelBO> studentGrades = readTransformed.stream()
                .map(list -> objectMapper.convertValue(list, StudentGradesExcelBO.class)).collect(Collectors.toList());
        log.info(studentGrades.toString());
        for (StudentGradesExcelBO studentGrade:studentGrades) {
            CourseSelect courseSelect = courseSelectRepo.findByUsernameAndCourseCode(
                    studentGrade.getUsername(),
                    courseCode
            );
            courseSelect.setGrade(studentGrade.getGrade());
            courseSelect.setFinished(true);
            courseSelectRepo.save(courseSelect);
        }
        return ResponseEntity.ok();
    }

    /**
     * 获得与当前课程冲突的课程编码
     * @param courseCode 要添加课程的课程编码
     * @param username 学生用户名
     * @return 存在冲突的课程的课程编码
     */
    @Override
    public String getConflictedCourseCode(String courseCode, String username) {
        // 当前课程的时间表
        List<CourseSchedule> schedulesCurrent = scheduleRepo.findByCourseCode(courseCode);
        // 判断学生时间是否冲突
        List<CourseSchedule> schedulesPrevious = scheduleRepo.findByUsername(username);
        return CourseConflictUtil.getConflictedCourseCode(schedulesPrevious, schedulesCurrent);
    }

    private static Map<String, Object> transform(Map<String, Object> origin, Map<String, String> map) {
        Map<String, Object> ans = new HashMap<>();
        origin.forEach((key, value) -> ans.put(map.get(key), value));
        return ans;
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
