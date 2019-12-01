package cn.ict.course.service.impl;

import cn.ict.course.entity.db.*;
import cn.ict.course.entity.dto.CourseDTO;
import cn.ict.course.entity.dto.ScheduleDTO;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.CourseVO;
import cn.ict.course.repo.*;
import cn.ict.course.service.CourseService;
import cn.ict.course.utils.CourseCodeUtil;
import cn.ict.course.utils.CourseConflictUtil;
import com.github.dozermapper.core.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Jianyong Feng
 **/
@Service
public class CourseServiceImpl implements CourseService {

    private final Mapper mapper;
    private final CourseRepo courseRepo;
    private final CourseScheduleRepo scheduleRepo;
    private final CoursePreSelectRepo coursePreSelectRepo;
    private final SelectionControlRepo selectionControlRepo;
    private final CourseSelectRepo courseSelectRepo;

    public CourseServiceImpl(Mapper mapper,
                             CourseRepo courseRepo,
                             CourseScheduleRepo scheduleRepo,
                             CoursePreSelectRepo coursePreSelectRepo,
                             SelectionControlRepo selectionControlRepo,
                             CourseSelectRepo courseSelectRepo) {
        this.mapper = mapper;
        this.courseRepo = courseRepo;
        this.scheduleRepo = scheduleRepo;
        this.coursePreSelectRepo = coursePreSelectRepo;
        this.selectionControlRepo = selectionControlRepo;
        this.courseSelectRepo = courseSelectRepo;
    }

    /**
     * 添加课程
     * CourseCode重复验证
     * TeacherId/College/Classroom存在验证由前端完成
     * 课程时间冲突验证：教室-上课时间
     * 教师时间冲突验证：教师-上课时间
     *
     * @param courseDTO 需要保存的课程信息
     */
    @Override
    @Transactional
    public ResponseEntity addCourse(CourseDTO courseDTO) {
        List<ScheduleDTO> schedulesDTO = courseDTO.getScheduleList();
        List<CourseSchedule> schedulesCurrent = schedulesDTO
                .stream()
                .map(schedule -> mapper.map(schedule, CourseSchedule.class))
                .collect(Collectors.toList());

        String result = timeConflict(courseDTO);
        if (result != null) {
            return ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR, result);
        }

        Course course = mapper.map(courseDTO, Course.class);
        String courseCode = CourseCodeUtil.generateCourseCode();
        course.setCourseCode(courseCode);
        course.setSelectedNum(0);
        schedulesCurrent.forEach(schedule -> schedule.setCourseCode(courseCode));
        courseRepo.save(course);
        scheduleRepo.saveAll(schedulesCurrent);
        return ResponseEntity.ok();
    }

    @Override
    public List<CourseVO> getCourseList() {
        List<Course> courseList = courseRepo.findAll();
        List<CourseVO> courses = courseList
                .stream()
                .map(course -> mapper.map(course, CourseVO.class))
                .collect(Collectors.toList());

        for (CourseVO course:courses) {
            List<CourseSchedule> scheduleList = scheduleRepo.findByCourseCode(course.getCourseCode());
            List<ScheduleDTO> schedules = scheduleList
                    .stream()
                    .map(schedule -> mapper.map(schedule, ScheduleDTO.class))
                    .collect(Collectors.toList());
            course.setSchedules(schedules);
        }

        return courses;
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
        SelectionControl selectionControl = selectionControlRepo.findById(1L).orElse(null);
        if (selectionControl == null) {
            return ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR, "管理员未定义选课时间");
        }
        Date startTime = selectionControl.getStartTime();
        Date endTime = selectionControl.getEndTime();
        if (startTime == null || !selectTimeAfterStart(startTime)) {
            return ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR, "选课未开始");
        }
        if (endTime == null || !selectTimeBeforeEnd(endTime)) {
            return ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR, "选课已结束");
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

    private boolean conflictTeacher(List<CourseSchedule> schedulesCurrent, String teacherId) {
        // 判断老师时间是否冲突
        List<CourseSchedule> schedulesPrevious = scheduleRepo.findByTeacherId(teacherId);
        return CourseConflictUtil.isConflict(schedulesPrevious, schedulesCurrent);
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

    private boolean conflictClassroom(List<CourseSchedule> schedulesCurrent, List<String> classrooms) {
        List<CourseSchedule> schedulesPrevious = scheduleRepo.findByClassroom(classrooms);

        return CourseConflictUtil.isConflict(schedulesPrevious, schedulesCurrent);
    }

    private String timeConflict(CourseDTO coursesDTO) {
        List<ScheduleDTO> schedulesDTO = coursesDTO.getScheduleList();
        List<CourseSchedule> schedulesCurrent = schedulesDTO.stream()
                .map(schedule -> mapper.map(schedule, CourseSchedule.class))
                .collect(Collectors.toList());

        String teacherId = coursesDTO.getTeacherId();
        if (conflictTeacher(schedulesCurrent, teacherId)) {
            return "教师时间冲突";
        }

        // 判断教室时间是否冲突
        List<String> classrooms = schedulesDTO
                .stream()
                .map(ScheduleDTO::getClassroom)
                .collect(Collectors.toList());

        if (conflictClassroom(schedulesCurrent, classrooms)) {
            return "教室时间冲突";
        }

        return null;
    }

    private boolean selectTimeAfterStart(Date startTime) {
        Date currentTime = new Date();

        return currentTime.after(startTime);
    }

    private boolean selectTimeBeforeEnd(Date startTime) {
        Date currentTime = new Date();

        return currentTime.before(startTime);
    }


}
