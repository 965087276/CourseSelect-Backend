package cn.ict.course.repo;

import cn.ict.course.entity.db.CourseSchedule;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 965087276@qq.com
 * @date 2019/11/9 18:16
 */
@Repository
public interface CourseScheduleRepo extends JpaRepository<CourseSchedule, Long> {
    /**
     * @param courseCode 课程编码
     * @return 该课程的所有时刻表
     */
    List<CourseSchedule> findByCourseCode(String courseCode);

    @Query(value = "SELECT * FROM course_schedule WHERE course_code IN (SELECT course_code FROM course WHERE teacher_id = :teacherId)", nativeQuery = true)
    List<CourseSchedule> findByTeacherId(@Param("teacherId") String teacherId);

    List<CourseSchedule> findByClassroom(@Param(value = "classrooms") List<String> classrooms);

    @Query(value = "SELECT B.* FROM course_select A RIGHT JOIN course_schedule B on A.course_code = B.course_code WHERE A.username = :username", nativeQuery = true)
    List<CourseSchedule> findByUsername(@Param("username") String username);

    void deleteByCourseCode(String courseCode);
}
