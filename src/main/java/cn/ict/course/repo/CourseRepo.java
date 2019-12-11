package cn.ict.course.repo;

import cn.ict.course.entity.db.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * @author 965087276@qq.com
 * @date 2019/11/9 18:15
 */
@Repository
public interface CourseRepo extends JpaRepository<Course, Long> {

    /**
     * 根据课程编码查询课程
     * @param courseCode 课程编码
     * @return 课程数据
     */
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Course findByCourseCode(String courseCode);

    List<Course> findByCourseCode(List<String> courseCode);

    List<Course> findByTeacherId(String teacherId);

    List<Course> findAllByTeacherId(String teacherId);

    /**
     * 通过课程编码删除课程
     * @param courseCode 课程编码
     */
    void deleteByCourseCode(String courseCode);
}
