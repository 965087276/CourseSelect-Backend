package cn.ict.course.service.impl;

import cn.ict.course.entity.db.Classroom;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.repo.ClassroomRepo;
import cn.ict.course.service.ClassroomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jianyong Feng
 **/
@Service
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepo classroomRepo;

    public ClassroomServiceImpl(ClassroomRepo classroomRepo) {
        this.classroomRepo = classroomRepo;
    }

    /**
     * 获取所有教室
     *
     * @return 教室列表
     */
    @Override
    public ResponseEntity<List<String>> getAllClassrooms() {
        List<String> classrooms = classroomRepo
                .findAll()
                .stream()
                .map(Classroom::getName)
                .collect(Collectors.toList());
        return ResponseEntity.ok(classrooms);
    }
}
