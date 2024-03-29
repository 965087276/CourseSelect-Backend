package cn.ict.course.service;

import cn.ict.course.entity.db.Course;
import cn.ict.course.entity.dto.CourseDTO;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.CourseVO;
import cn.ict.course.entity.vo.TeacherCourseTableVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Jianyong Feng
 **/
public interface CourseService {
    ResponseEntity addCourse(CourseDTO courseDTO);

    ResponseEntity<List<CourseVO>> getCourseList(String college, String courseType, String CourseName, Integer day, Integer time);

    /**
     * 通过课程编码删除课程
     * @param courseCode 课程编码
     * @return 删除结果
     */
    ResponseEntity deleteCourseByCourseCode(String courseCode);


    ResponseEntity<List<TeacherCourseTableVO>> getTeacherCourseTable(String teacherId);

    /**
     * 获取教师上课课程信息
     * @param teacherId 教师用户名
     * @return 课程信息
     */
    ResponseEntity<List<Course>> getCoursesInfoByTeacherId(String teacherId);

    /**
     * 获取教师所授课程列表
     *
     * @param teacherId 教师用户名
     * @return 该教师教授的课程信息
     */
    ResponseEntity getTeacherCourseInfoByTeacherId(String teacherId);

    /**
     * 通过excel文件批量导入课程
     * @param file excel文件
     * @return 是否导入成功
     */
    ResponseEntity addCoursesByExcel(MultipartFile file) throws IOException;

}
