package cn.ict.course.service.impl;

import cn.ict.course.entity.db.Course;
import cn.ict.course.entity.db.CourseSchedule;
import cn.ict.course.entity.dto.CourseDTO;
import cn.ict.course.entity.dto.ScheduleDTO;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.CourseVO;
import cn.ict.course.repo.CoursePreSelectRepo;
import cn.ict.course.repo.CourseRepo;
import cn.ict.course.repo.CourseScheduleRepo;
import cn.ict.course.repo.UserRepo;
import cn.ict.course.service.CourseService;
import cn.ict.course.utils.CourseCodeUtil;
import cn.ict.course.utils.CourseConflictUtil;
import com.github.dozermapper.core.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final CoursePreSelectRepo coursePreSelectRepo;

    public CourseServiceImpl(Mapper mapper,
                             CourseRepo courseRepo,
                             CourseScheduleRepo scheduleRepo,
                             CoursePreSelectRepo coursePreSelectRepo) {
        this.mapper = mapper;
        this.courseRepo = courseRepo;
        this.scheduleRepo = scheduleRepo;
        this.coursePreSelectRepo = coursePreSelectRepo;
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
