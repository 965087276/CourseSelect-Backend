package cn.ict.course.mapper;

import cn.ict.course.entity.bo.MyPreCourseBO;
import cn.ict.course.entity.vo.CurriculumVO;
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
}
