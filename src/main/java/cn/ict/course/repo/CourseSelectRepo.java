package cn.ict.course.repo;

import cn.ict.course.entity.db.Course;
import cn.ict.course.entity.db.CourseSelect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 965087276@qq.com
 * @date 2019/11/9 18:16
 */
@Repository
public interface CourseSelectRepo extends JpaRepository<CourseSelect, Long> {
    @Query(value = "select A from Course A inner join CourseSelect B on A.courseCode = B.courseCode and B.username = ?1")
    List<Course> listSelectedCoursesByUsername(String username);

    @Query(value = "select A from Course A inner join CourseSelect B on A.courseCode = B.courseCode and B.username = ?1 and B.finished = true and B.grade >= 60")
    List<Course> listPassedCoursesByUsername(String username);

    CourseSelect findByUsernameAndCourseCode(String username, String courseCode);

    void deleteByUsernameAndCourseCode(String username, String courseCode);
}
