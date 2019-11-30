package cn.ict.course.service.impl;

import cn.ict.course.entity.db.College;
import cn.ict.course.repo.CollegeRepo;
import cn.ict.course.service.CollegeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jianyong Feng
 **/
@Service
public class CollegeServiceImpl implements CollegeService {

    private final CollegeRepo collegeRepo;

    public CollegeServiceImpl(CollegeRepo collegeRepo) {
        this.collegeRepo = collegeRepo;
    }

    @Override
    public List<String> getCollegeList() {
        List<College> colleges = collegeRepo.findAll();
        return colleges
                .stream()
                .map(College::getName)
                .collect(Collectors.toList());
    }
}
