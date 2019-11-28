package cn.ict.course.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.ict.course.entity.db.Course;
import cn.ict.course.entity.db.CourseSchedule;
import cn.ict.course.entity.dto.CourseDTO;
import cn.ict.course.repo.CourseRepo;
import cn.ict.course.repo.CourseScheduleRepo;
import cn.ict.course.service.TeacherService;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

/**
 * @author Jianyong Feng
 **/
@Service
public class TeacherServiceImpl implements TeacherService {

    private final Mapper mapper;
    private final CourseRepo courseRepo;
    private final CourseScheduleRepo scheduleRepo;

    public TeacherServiceImpl(Mapper mapper, CourseRepo courseRepo, CourseScheduleRepo scheduleRepo) {
        this.mapper = mapper;
        this.courseRepo = courseRepo;
        this.scheduleRepo = scheduleRepo;
    }
    @Override
    public void saveCourse(CourseDTO courseDTO) {
        Course course = mapper.map(courseDTO, Course.class);

        String courseCode = generateCourseCode();
        course.setCourseCode(courseCode);
        course.setSelectedNum(0);

        CourseSchedule schedule = mapper.map(courseDTO.getScheduleDTO(), CourseSchedule.class);
        schedule.setCourseCode(courseCode);

        courseRepo.save(course);

        scheduleRepo.save(schedule);
    }

    private static String generateCourseCode() {

        return "GKD" + RandomUtil.randomNumbers(8);
    }
}
