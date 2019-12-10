package cn.ict.course.repo;

import cn.ict.course.entity.db.CoursePreselect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 965087276@qq.com
 * @date 2019/11/9 18:16
 */
@Repository
public interface CoursePreselectRepo extends JpaRepository<CoursePreselect, Long> {
    CoursePreselect findByUsernameAndCourseCode(String username, String courseCode);

    void deleteByUsernameAndCourseCode(String username, String courseCode);

    void deleteAllByUsername(String username);

    List<CoursePreselect> findAllByUsername(String username);

    @Transactional
    @Modifying
    @Query("update CoursePreselect t set t.addToTable = :addToTable where t.username = :username and t.courseCode = :courseCode")
    void modifyAddToTable(@Param("username") String username, @Param("courseCode") String courseCode, @Param("addToTable") boolean addToTable);
}
