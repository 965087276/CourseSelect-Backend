package cn.ict.course.repo;

import cn.ict.course.entity.db.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Jianyong Feng
 **/
@Repository
public interface ClassroomRepo extends JpaRepository<Classroom, Long> {

}
