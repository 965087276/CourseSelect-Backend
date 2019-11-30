package cn.ict.course.repo;

import cn.ict.course.entity.db.CourseSchedule;
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
    public List<CourseSchedule> findByCourseCode(String courseCode);
}
