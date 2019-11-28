package cn.ict.course.service;

import cn.ict.course.entity.db.College;

import java.util.List;

/**
 * @author Jianyong Feng
 **/
public interface CollegeService {
    List<College> findAllColleges();
}
