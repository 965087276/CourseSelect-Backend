package cn.ict.course.repo;

import cn.ict.course.entity.db.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 965087276@qq.com
 * @date 2019/11/9 18:17
 */
@Repository
public interface TeacherRepo extends JpaRepository<Teacher, Long> {
}
