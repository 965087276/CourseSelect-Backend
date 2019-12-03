package cn.ict.course.repo;

import cn.ict.course.entity.db.CoursePreSelect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 965087276@qq.com
 * @date 2019/11/9 18:16
 */
@Repository
public interface CoursePreselectRepo extends JpaRepository<CoursePreSelect, Long> {
    CoursePreSelect findByUsernameAndCourseCode(String username, String courseCode);

    void deleteByUsernameAndCourseCode(String username, String courseCode);

    List<CoursePreSelect> findAllByUsername(String username);
}
