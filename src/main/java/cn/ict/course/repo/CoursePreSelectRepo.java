package cn.ict.course.repo;

import cn.ict.course.entity.db.CoursePreSelect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 965087276@qq.com
 * @date 2019/11/9 18:16
 */
@Repository
public interface CoursePreSelectRepo extends JpaRepository<CoursePreSelect, Long> {
    CoursePreSelect findByCourseCode(String courseCode);
}
