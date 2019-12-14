package cn.ict.course.mapper;

import cn.ict.course.entity.bo.MyPreCourseBO;
import cn.ict.course.entity.dto.CourseWithScheduleDTO;
import cn.ict.course.entity.dto.TeacherCourseInfoDTO;
import cn.ict.course.entity.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 965087276@qq.com
 * @date 2019/12/3 15:55
 */
@Mapper
@Repository
public interface CourseMapper {

    @Select("SELECT A.course_name courseName," +
            "       A.course_code courseCode," +
            "       A.course_teacher courseTeacher," +
            "       A.course_type courseType," +
            "       A.credit credit," +
            "       B.start_week startWeek," +
            "       B.end_week endWeek," +
            "       B.day day," +
            "       B.time time," +
            "       C.add_to_table addToTable," +
            "       B.classroom classroom " +
            "FROM course_schedule B " +
            "LEFT JOIN course A on A.course_code = B.course_code " +
            "LEFT JOIN course_preselect C on C.course_code = B.course_code " +
            "WHERE C.username = #{username}")
    List<MyPreCourseBO> listMyPreCourse(@Param("username") String username);

    @Select("SELECT A.course_name courseName," +
            " A.course_code courseCode," +
            " A.course_teacher courseTeacher," +
            " B.start_week startWeek," +
            " B.end_week endWeek," +
            " B.time time," +
            " B.classroom classroom," +
            " B.day day " +
            "FROM course_schedule B " +
            "LEFT JOIN course A on A.course_code = B.course_code " +
            "LEFT JOIN course_select C on C.course_code = B.course_code " +
            "WHERE C.username = #{username}")
    List<CurriculumVO> getSelectedCourses(@Param("username") String username);

    @Select("SELECT B.course_code courseCode, " +
            "    B.course_name courseName, " +
            "    B.credit credit, " +
            "    B.course_type courseType, " +
            "    B.course_teacher courseTeacher, " +
            "    B.course_hour courseHour " +
            "FROM course_select A " +
            "LEFT JOIN course B on A.course_code = B.course_code " +
            "WHERE A.username = #{username}")
    List<MyCourseVO> getMyCourses(@Param("username") String username);

    @Select("SELECT B.username studentUsername, " +
            "    B.real_name studentRealName, " +
            "    B.college college, " +
            "    A.grade grade, " +
            "    A.is_finish finished " +
            "FROM course_select A " +
            "LEFT JOIN `user` B on A.username = B.username " +
            "WHERE A.course_code = #{courseCode}")
    List<CourseStudentInfoVO> getStudentInfoByCourseCode(@Param("courseCode") String courseCode);
    
    @Select("SELECT A.course_code courseCode, " +
            "    A.start_week startWeek, " +
            "    A.end_week endWeek, " +
            "    A.time time, " +
            "    A.classroom classroom, " +
            "    A.`day` day, " +
            "    B.course_name courseName " +
            "FROM course_schedule A " +
            "LEFT JOIN course B on A.course_code = B.course_code " +
            "WHERE B.teacher_id = #{teacherId}")
    List<TeacherCourseTableVO> getTeacherCourseTable(@Param("teacherId") String teacherId);
    
    @Select("SELECT B.course_code courseCode, " +
            "    B.course_name courseName, " +
            "    B.credit credit, " +
            "    B.course_type courseType, " +
            "    B.course_teacher courseTeacher, " +
            "    A.grade grade " +
            "FROM course_select A " +
            "LEFT JOIN course B on A.course_code = B.course_code " +
            "WHERE A.username = #{username} AND A.is_finish = 1")
    List<StudentGradesVO> getStudentGradesByUsername(@Param("username") String username);

    @Select("SELECT A.course_code courseCode, " +
            "    A.start_week startWeek, " +
            "    A.end_week endWeek, " +
            "    A.`day` `day`, " +
            "    A.time time, " +
            "    A.classroom classroom, " +
            "    B.course_name courseName, " +
            "    B.college college, " +
            "    B.limit_num limitNum, " +
            "    B.selected_num selectedNum, " +
            "    B.course_hour courseHour, " +
            "    B.credit credit, " +
            "    B.course_type courseType, " +
            "    B.course_teacher courseTeacher  " +
            "FROM course_schedule A " +
            "LEFT JOIN course B on A.course_code = B.course_code " +
            "WHERE B.teacher_id = #{teacherId} " +
            "ORDER BY A.course_code")
    List<TeacherCourseInfoDTO> getTeacherCourseInfoByTeacherId(@Param("teacherId") String teacherId);

    @Select("SELECT A.start_week startWeek, " +
            "    A.end_week endWeek, " +
            "    A.`day` `day`, " +
            "    A.time time, " +
            "    A.classroom classroom, " +
            "    B.course_code courseCode, " +
            "    B.course_name courseName, " +
            "    B.college college, " +
            "    B.limit_num limitNum, " +
            "    B.selected_num selectedNum, " +
            "    B.course_hour courseHour, " +
            "    B.credit credit, " +
            "    B.course_type courseType, " +
            "    B.course_teacher courseTeacher " +
            "FROM course_schedule A " +
            "LEFT JOIN course B on A.course_code = B.course_code")
    List<CourseWithScheduleDTO> getAllCourseWithSchedule();
}
