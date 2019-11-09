package cn.ict.course.repo;

import cn.ict.course.entity.db.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 965087276@qq.com
 * @date 2019/11/9 18:15
 */
@Repository
public interface CourseRepo extends JpaRepository<Course, Long> {
}
