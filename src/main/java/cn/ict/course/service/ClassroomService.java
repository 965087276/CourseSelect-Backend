package cn.ict.course.service;

import cn.ict.course.entity.http.ResponseEntity;

import java.util.List;

/**
 * @author Jianyong Feng
 **/
public interface ClassroomService {

    /**
     * 获取所有教室
     * @return 教室列表
     */
    ResponseEntity<List<String>> getAllClassrooms();
}
