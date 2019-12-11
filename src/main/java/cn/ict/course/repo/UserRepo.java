package cn.ict.course.repo;

import cn.ict.course.entity.db.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Jianyong Feng
 **/

@Repository
public interface UserRepo extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {
    User findByUsername(String username);

    void deleteByUsername(String username);
}
