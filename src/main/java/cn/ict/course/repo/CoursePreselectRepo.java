package cn.ict.course.repo;

import cn.ict.course.entity.db.CoursePreselect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 965087276@qq.com
 * @date 2019/11/9 18:16
 */
@Repository
public interface CoursePreselectRepo extends JpaRepository<CoursePreselect, Long> {
    CoursePreselect findByUsernameAndCourseCode(String username, String courseCode);

    void deleteByUsernameAndCourseCode(String username, String courseCode);

    List<CoursePreselect> findAllByUsername(String username);
}
