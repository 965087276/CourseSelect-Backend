package cn.ict.course.service.impl;

import cn.ict.course.entity.db.Course;
import cn.ict.course.entity.db.CourseSchedule;
import cn.ict.course.entity.dto.CourseDTO;
import cn.ict.course.repo.CourseRepo;
import cn.ict.course.repo.CourseScheduleRepo;
import cn.ict.course.service.CourseService;
import cn.ict.course.utils.CourseCodeUtil;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

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
    @Override
    public void addCourse(CourseDTO courseDTO) {
        Course course = mapper.map(courseDTO, Course.class);

        String courseCode = CourseCodeUtil.generateCourseCode();

        // 设置课程编码
        course.setCourseCode(courseCode);
        // 初始选课人数为0
        course.setSelectedNum(0);

        // 课程时间表
        CourseSchedule schedule = mapper.map(courseDTO.getScheduleDTO(), CourseSchedule.class);
        schedule.setCourseCode(courseCode);

        courseRepo.save(course);

        scheduleRepo.save(schedule);
    }
}
