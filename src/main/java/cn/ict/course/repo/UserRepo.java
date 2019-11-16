package cn.ict.course.repo;

import cn.ict.course.entity.db.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Jianyong Feng
 **/

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByUserCode(String userCode);
}
