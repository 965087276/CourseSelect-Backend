package cn.ict.course.service.impl;

import cn.ict.course.entity.db.Course;
import cn.ict.course.entity.db.CourseSchedule;
import cn.ict.course.entity.db.CourseSelect;
import cn.ict.course.entity.db.SelectionControl;
import cn.ict.course.entity.dto.CourseDTO;
import cn.ict.course.entity.dto.ScheduleDTO;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.CourseVO;
import cn.ict.course.mapper.CourseMapper;
import cn.ict.course.repo.*;
import cn.ict.course.service.CourseService;
import cn.ict.course.utils.CourseCodeUtil;
import cn.ict.course.utils.CourseConflictUtil;
import com.github.dozermapper.core.Mapper;
import org.hibernate.service.NullServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jianyong Feng
 **/
@Service
public class CourseServiceImpl implements CourseService {

    private final Mapper mapper;
    private final CourseRepo courseRepo;
    private final CourseScheduleRepo scheduleRepo;
    private final CoursePreselectRepo coursePreSelectRepo;
    private final SelectionControlRepo selectionControlRepo;
    private final CourseSelectRepo courseSelectRepo;
    private final CourseMapper courseMapper;

    public CourseServiceImpl(Mapper mapper,
                             CourseRepo courseRepo,
                             CourseScheduleRepo scheduleRepo,
                             CoursePreselectRepo coursePreSelectRepo,
                             SelectionControlRepo selectionControlRepo,
                             CourseSelectRepo courseSelectRepo,
                             CourseMapper courseMapper) {
        this.mapper = mapper;
        this.courseRepo = courseRepo;
        this.scheduleRepo = scheduleRepo;
        this.coursePreSelectRepo = coursePreSelectRepo;
        this.selectionControlRepo = selectionControlRepo;
        this.courseSelectRepo = courseSelectRepo;
        this.courseMapper = courseMapper;
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
    public List<CourseVO> getCourseList(String college, String courseType, String CourseName, Integer day, Integer time) {
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

        courses = courses
                .stream()
                .filter(course -> isOk(course, college, courseType, CourseName, day, time))
                .collect(Collectors.toList());

        return courses;
    }

    /**
     * 通过课程编码删除课程
     *
     * @param courseCode 课程编码
     * @return 删除结果
     */
    @Override
    @Transactional
    public ResponseEntity deleteCourseByCourseCode(String courseCode) {
        try {
            courseRepo.deleteByCourseCode(courseCode);
            return ResponseEntity.ok();
        } catch (JpaSystemException e) {
            e.printStackTrace();
            return ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR,
                                          "删除失败，课程可能已被删除");
        }
    }

    private boolean isOk(CourseVO course, String college, String courseType, String courseName, int day, int time) {

        if ((!college.equals("none") && !course.getCollege().equals(college))
                || (!courseType.equals("none") && !course.getCourseType().equals(courseType))
                || (!courseName.equals("none") && !course.getCourseName().contains(courseName))) {
            return false;
        }
        return (day == -1 || course.getSchedules().stream().anyMatch(o -> o.getDay() == day))
                && (time == -1 || course.getSchedules().stream().anyMatch(o -> o.getTime() == time));

    }



    /**
     * 教师增加课程，时间是否冲突
     * @param schedulesCurrent 当前课程的时刻表
     * @param teacherId 教师用户名
     * @return 如果时间冲突，返回true，否则false
     */
    private boolean conflictTeacher(List<CourseSchedule> schedulesCurrent, String teacherId) {
        // 判断老师时间是否冲突
        List<CourseSchedule> schedulesPrevious = scheduleRepo.findByTeacherId(teacherId);
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


}
