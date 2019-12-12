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
}
