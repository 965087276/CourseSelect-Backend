package cn.ict.course.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jianyong Feng
 **/
public class ListMapUtil {

    public static Map<String, String> getStudentUserMap() {
        Map<String, String> map = new HashMap<>();
        map.put("所在学院", "college");
        map.put("学工号", "username");
        map.put("学生姓名", "realName");
        map.put("邮箱", "email");
        map.put("联系方式", "phoneNumber");
        return map;
    }

    public static Map<String, String> getTeacherUserMap() {
        Map<String, String> map = new HashMap<>();
        map.put("所在学院", "college");
        map.put("教师编号", "username");
        map.put("教师姓名", "realName");
        map.put("邮箱", "email");
        map.put("联系方式", "phoneNumber");
        return map;
    }

    public static Map<String, String> getCourseExcelMap() {
        Map<String, String> map = new HashMap<>();
        map.put("开课学院", "college");
        map.put("课程编码", "courseCode");
        map.put("课程名称", "courseName");
        map.put("课程属性", "courseType");
        map.put("课时", "courseHour");
        map.put("学分", "credit");
        map.put("限选", "limitNum");
        map.put("主讲教师", "courseTeacher");
        map.put("教工号", "teacherId");

        map.put("起始周", "startWeek");
        map.put("结束周", "endWeek");
        map.put("星期", "day");
        map.put("节次", "time");
        map.put("教室", "classroom");
        return map;
    }

    public static Map<String, Object> mapTransform(Map<String, Object> origin, Map<String, String> map) {
        Map<String, Object> ans = new HashMap<>();
        origin.forEach((key, value) -> ans.put(map.get(key), value));
        return ans;
    }
}
