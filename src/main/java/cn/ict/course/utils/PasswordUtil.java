package cn.ict.course.utils;

import cn.ict.course.constants.PasswordConst;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * @author Jianyong Feng
 **/
public class PasswordUtil {

    public static String passwordGenerateByUsername(String username) {
        return username.substring(username.length() - 6);
    }

    public static String passwordEncode(String password, String salt, String egyptName, int times) {
        return new SimpleHash(egyptName,password,salt,times).toString();
    }

    public static String passwordEncode(String password, String salt) {
        return new SimpleHash(
                PasswordConst.PASSWORD_EGYPT_NAME,
                password,
                salt,
                PasswordConst.PASSWORD_EGYPT_TIMES
        ).toString();
    }

    public static String saltGenerate() {
        return new SecureRandomNumberGenerator().nextBytes().toString();
    }
}
