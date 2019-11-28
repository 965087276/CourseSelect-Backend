package cn.ict.course.repo;

import cn.ict.course.entity.db.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Jianyong Feng
 **/
@Repository
public interface CollegeRepo extends JpaRepository<College, Long> {
}
