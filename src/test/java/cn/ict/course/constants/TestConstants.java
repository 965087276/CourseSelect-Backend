package cn.ict.course.constants;

/**
 * @author Jianyong Feng
 **/
public class TestConstants {
    public static final String TEST_USER_INFO = "{" +
            "  \"college\": \"网络空间安全学院\"," +
            "  \"email\": \"90388@qq.com\"," +
            "  \"major\": \"网络安全技术\"," +
            "  \"password\": \"123456\"," +
            "  \"phoneNumber\": \"18106275581\"," +
            "  \"realName\": \"乔布斯\"," +
            "  \"role\": \"student\"," +
            "  \"unit\": \"信息工程研究所\"," +
            "  \"username\": \"201903\"" +
            "}";
    public static final String TEST_USERNAME_AND_PWD_RIGHT = "{" +
            "  \"password\": \"123456\"," +
            "  \"username\": \"201901\"" +
            "}";
    public static final String TEST_USERNAME_AND_PWD_ERROR = "{" +
            "  \"password\": \"12345\"," +
            "  \"username\": \"201901\"" +
            "}";
    
    private static final String TEST_COURSE_ADD = "{ " +
            "  \"college\": \"计算机科学与技术学院\", " +
            "  \"courseHour\": 60, " +
            "  \"courseName\": \"模式识别与机器学习\", " +
            "  \"courseTeacher\": \"黄\", " +
            "  \"courseType\": \"专业核心课\", " +
            "  \"credit\": 3, " +
            "  \"limitNum\": 300, " +
            "  \"scheduleList\": [ " +
            "    { " +
            "      \"classroom\": \"教1-102\", " +
            "      \"day\": 2, " +
            "      \"endWeek\": 20, " +
            "      \"startWeek\": 2, " +
            "      \"time\": 3 " +
            "    } " +
            "  ], " +
            "  \"teacherId\": \"201904\" " +
            "}";

}
