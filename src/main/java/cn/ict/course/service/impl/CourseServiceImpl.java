package cn.ict.course.service.impl;

import cn.ict.course.entity.db.Course;
import cn.ict.course.entity.db.CourseSchedule;
import cn.ict.course.entity.dto.CourseDTO;
import cn.ict.course.entity.dto.ScheduleDTO;
import cn.ict.course.entity.vo.CourseVO;
import cn.ict.course.repo.CourseRepo;
import cn.ict.course.repo.CourseScheduleRepo;
import cn.ict.course.service.CourseService;
import cn.ict.course.utils.CourseCodeUtil;
import com.github.dozermapper.core.Mapper;
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

    public CourseServiceImpl(Mapper mapper, CourseRepo courseRepo, CourseScheduleRepo scheduleRepo) {
        this.mapper = mapper;
        this.courseRepo = courseRepo;
        this.scheduleRepo = scheduleRepo;
    }

    /**
     * 添加课程
     * to-do：
     * CourseCode重复验证
     * College/Classroom存在验证
     * 课程时间冲突验证：教室-上课时间
     * 教师时间冲突验证：教师-上课时间
     *
     * @param courseDTO 需要保存的课程信息
     */
    @Override
    @Transactional
    public void addCourse(CourseDTO courseDTO) {
        Course course = mapper.map(courseDTO, Course.class);

        String courseCode = CourseCodeUtil.generateCourseCode();

        // 设置课程编码
        course.setCourseCode(courseCode);
        // 初始选课人数为0
        course.setSelectedNum(0);

        // 课程时间表
        // to-do 判断课程时间冲突
        List<ScheduleDTO> scheduleList = courseDTO.getScheduleList();
        List<CourseSchedule> schedules = scheduleList.stream()
                .map(schedule -> mapper.map(schedule, CourseSchedule.class))
                .collect(Collectors.toList());
        schedules.forEach(schedule -> schedule.setCourseCode(courseCode));

        courseRepo.save(course);

        scheduleRepo.saveAll(schedules);
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


}
