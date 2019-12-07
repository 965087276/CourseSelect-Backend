package cn.ict.course.repo;

import cn.ict.course.entity.db.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
    Course findByCourseCode(String courseCode);

    List<Course> findByCourseCode(List<String> courseCode);

}
