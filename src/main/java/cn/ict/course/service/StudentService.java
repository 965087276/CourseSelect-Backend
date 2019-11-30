package cn.ict.course.service;

import cn.ict.course.entity.vo.CourseSelectStatsVO;

import java.util.List;

/**
 * @author 965087276@qq.com
 * @date 2019/11/30 21:39
 */
public interface StudentService {
    List<CourseSelectStatsVO> getCoursesSelectedStats(String username);
}
