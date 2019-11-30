package cn.ict.course.service.impl;

import cn.ict.course.entity.vo.CourseSelectStatsVO;
import cn.ict.course.repo.CourseRepo;
import cn.ict.course.repo.CourseSelectRepo;
import cn.ict.course.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 965087276@qq.com
 * @date 2019/11/30 21:44
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private CourseSelectRepo courseSelectRepo;

    @Override
    public List<CourseSelectStatsVO> getCoursesSelectedStats(String username) {
        // 获取学生选的所有课
        return null;
    }
}
