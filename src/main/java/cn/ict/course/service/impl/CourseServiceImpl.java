package cn.ict.course.service.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.ict.course.constants.CourseConflictConst;
import cn.ict.course.entity.db.Course;
import cn.ict.course.entity.db.CourseSchedule;
import cn.ict.course.entity.dto.CourseDTO;
import cn.ict.course.entity.dto.CourseExcelDTO;
import cn.ict.course.entity.dto.ScheduleDTO;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.CourseVO;
import cn.ict.course.entity.vo.TeacherCourseTableVO;
import cn.ict.course.mapper.CourseMapper;
import cn.ict.course.repo.CoursePreselectRepo;
import cn.ict.course.repo.CourseRepo;
import cn.ict.course.repo.CourseScheduleRepo;
import cn.ict.course.repo.CourseSelectRepo;
import cn.ict.course.service.CourseService;
import cn.ict.course.utils.CourseCodeUtil;
import cn.ict.course.utils.CourseConflictUtil;
import cn.ict.course.utils.ListMapUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dozermapper.core.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Jianyong Feng
 **/
@Service
public class CourseServiceImpl implements CourseService {

    private final Mapper mapper;
    private final CourseRepo courseRepo;
    private final CourseScheduleRepo scheduleRepo;
    private final CourseSelectRepo courseSelectRepo;
    private final CoursePreselectRepo coursePreselectRepo;
    private final CourseMapper courseMapper;


    @Autowired
    public CourseServiceImpl(Mapper mapper,
                             CourseRepo courseRepo,
                             CourseScheduleRepo scheduleRepo,
                             CourseMapper courseMapper,
                             CourseSelectRepo courseSelectRepo,
                             CoursePreselectRepo coursePreselectRepo
    ) {
        this.mapper = mapper;
        this.courseRepo = courseRepo;
        this.scheduleRepo = scheduleRepo;
        this.courseMapper = courseMapper;
        this.courseSelectRepo = courseSelectRepo;
        this.coursePreselectRepo = coursePreselectRepo;
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
        List<ScheduleDTO> schedulesDTO = courseDTO.getSchedules();
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
    public ResponseEntity<List<CourseVO>> getCourseList(String college, String courseType, String CourseName, Integer day, Integer time) {
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
            course.setCourseSchedule(schedules);
        }

        courses = courses
                .stream()
                .filter(course -> isOk(course, college, courseType, CourseName, day, time))
                .collect(Collectors.toList());

        return ResponseEntity.ok(courses);
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
            scheduleRepo.deleteByCourseCode(courseCode);
            courseSelectRepo.deleteAllByCourseCode(courseCode);
            coursePreselectRepo.deleteAllByUsername(courseCode);
            return ResponseEntity.ok();
        } catch (JpaSystemException e) {
            e.printStackTrace();
            return ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR,
                                          "删除失败，课程可能已被删除");
        }
    }

    @Override
    public ResponseEntity<List<TeacherCourseTableVO>> getTeacherCourseTable(String teacherId) {
        List<TeacherCourseTableVO> courseTables = courseMapper.getTeacherCourseTable(teacherId);
        return ResponseEntity.ok(courseTables);
    }

    /**
     * 获取教师上课课程信息
     *
     * @param teacherId 教师用户名
     * @return 课程信息
     */
    @Override
    public ResponseEntity<List<Course>> getCoursesInfoByTeacherId(String teacherId) {
        List<Course> courses = courseRepo.findByTeacherId(teacherId);
        return ResponseEntity.ok(courses);
    }

    /**
     * 获取教师所授课程列表
     *
     * @param teacherId 教师用户名
     * @return 该教师教授的课程信息
     */
    @Override
    public ResponseEntity getTeacherCourseInfoByTeacherId(String teacherId) {
        List<Course> courseList = courseRepo.findAllByTeacherId(teacherId);
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
            course.setCourseSchedule(schedules);
        }
        return ResponseEntity.ok(courses);
    }

    /**
     * 通过excel文件批量导入课程
     *
     * @param file excel文件
     * @return 是否导入成功
     */
    @Override
    public ResponseEntity addCoursesByExcel(MultipartFile file) throws IOException {
        ExcelReader excelReader = ExcelUtil.getReader(file.getInputStream());
        List<Map<String, Object>> reader = excelReader.readAll();
        Map<String, String> map = ListMapUtil.getCourseExcelMap();

        List<Map<String, Object>> readerTransformed = reader.stream()
                .map(list -> ListMapUtil.mapTransform(list, map))
                .collect(Collectors.toList());

        ObjectMapper objectMapper = new ObjectMapper();

        List<CourseExcelDTO> coursesDTOExcel = readerTransformed.stream()
                .map(course -> objectMapper.convertValue(course, CourseExcelDTO.class))
                .collect(Collectors.toList());

        List<CourseSchedule> schedulesExcel = coursesDTOExcel.stream()
                .map(course -> mapper.map(course, CourseSchedule.class))
                .collect(Collectors.toList());

        // 根据CourseCode去重
        List<Course> coursesExcel = coursesDTOExcel.stream()
                .map(course -> mapper.map(course, Course.class))
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.toCollection(
                                        () -> new TreeSet<>(
                                                Comparator.comparing(Course::getCourseCode)
                                        )
                                ), ArrayList::new
                        )
                );

        List<String> excelClassrooms = coursesDTOExcel.stream()
                .map(CourseExcelDTO::getClassroom)
                .collect(Collectors.toList());

        List<Course> courses = courseRepo.findAll();
        courses.addAll(coursesExcel);

        List<CourseSchedule> schedules = scheduleRepo.findAll();

        schedules.addAll(schedulesExcel);

        Map<String, List<CourseSchedule>> schedulesByClassroom =
            schedules.stream().collect(Collectors.groupingBy(CourseSchedule::getClassroom));

        for (String classroom : schedulesByClassroom.keySet()) {
            List<CourseSchedule> scheduleList = schedulesByClassroom.get(classroom);
            String conflictedCourseCode = CourseConflictUtil.getConflictedCourseCode(scheduleList);
            if (!conflictedCourseCode.equals(CourseConflictConst.NO_COURSE_SCHEDULE_CONFLICT)) {
                String conflictedCourseName = courseRepo.findByCourseCode(conflictedCourseCode).getCourseName();
                return ResponseEntity.error(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "教室" + classroom + "中的课程" + conflictedCourseName
                        + "存在时间冲突"
                );
            }
        }

        Map<String, List<Course>> courseByTeacherId =
                courses.stream().collect(Collectors.groupingBy(Course::getTeacherId));

        for (String teacherId : courseByTeacherId.keySet()) {
            List<Course> courseList = courseByTeacherId.get(teacherId);
            List<String> courseCodeList =
                    courseList.stream().map(Course::getCourseCode).collect(Collectors.toList());
            List<CourseSchedule> scheduleList =
                    schedules.stream()
                            .filter(schedule -> courseCodeList.contains(schedule.getCourseCode()))
                            .collect(Collectors.toList());
            String conflictedCourseCode = CourseConflictUtil.getConflictedCourseCode(scheduleList);
            if (!conflictedCourseCode.equals(CourseConflictConst.NO_COURSE_SCHEDULE_CONFLICT)) {
                String conflictedCourseName = courseRepo.findByCourseCode(conflictedCourseCode).getCourseName();
                return ResponseEntity.error(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "教师用户名：" + teacherId + "的课程："
                        + conflictedCourseName + "存在课程冲突"
                );
            }
        }

        courseRepo.saveAll(courses);
        scheduleRepo.saveAll(schedules);
        return ResponseEntity.ok();
    }


    private boolean isOk(CourseVO course, String college, String courseType, String courseName, int day, int time) {

        if ((!college.equals("none") && !course.getCollege().equals(college))
                || (!courseType.equals("none") && !course.getCourseType().equals(courseType))
                || (!courseName.equals("none") && !course.getCourseName().contains(courseName))) {
            return false;
        }
        return (day == -1 || course.getCourseSchedule().stream().anyMatch(o -> o.getDay() == day))
                && (time == -1 || course.getCourseSchedule().stream().anyMatch(o -> o.getTime() == time));

    }

    private String timeConflict(CourseDTO coursesDTO) {
        List<ScheduleDTO> schedulesDTO = coursesDTO.getSchedules();
        List<CourseSchedule> schedulesCurrent = schedulesDTO.stream()
                .map(schedule -> mapper.map(schedule, CourseSchedule.class))
                .collect(Collectors.toList());

        String teacherId = coursesDTO.getTeacherId();
        List<CourseSchedule> schedulesPrevious = scheduleRepo.findByTeacherId(teacherId);
        String CourseCodeScheduleConflicted = CourseConflictUtil.getConflictedCourseCode(
                schedulesPrevious,
                schedulesCurrent
        );
        if (!CourseCodeScheduleConflicted.equals(CourseConflictConst.NO_COURSE_SCHEDULE_CONFLICT)) {
            String conflictedCourseName = courseRepo.findByCourseCode(CourseCodeScheduleConflicted).getCourseName();
            return "当前添加的课程与" + conflictedCourseName + "冲突";
        }

        // 判断教室时间是否冲突
        List<String> classrooms = schedulesDTO
                .stream()
                .map(ScheduleDTO::getClassroom)
                .collect(Collectors.toList());
        List<CourseSchedule> schedulesPreviousByClassrooms = scheduleRepo.findByClassroomIn(classrooms);
        CourseCodeScheduleConflicted = CourseConflictUtil.getConflictedCourseCode(
                schedulesPreviousByClassrooms,
                schedulesCurrent
        );
        if (!CourseCodeScheduleConflicted.equals(CourseConflictConst.NO_COURSE_SCHEDULE_CONFLICT)) {
            String conflictedCourseName = courseRepo.findByCourseCode(CourseCodeScheduleConflicted).getCourseName();
            return "当前添加的课程与" + conflictedCourseName + "冲突";
        }

        return null;
    }


}
