package cn.ict.course.shiro;

import cn.ict.course.entity.db.User;
import cn.ict.course.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Jianyong Feng
 **/
@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserRepo userRepo;

    /**
     * 权限管理
     * @return 权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 获取用户Principal对象
        User user = (User) principalCollection.getPrimaryPrincipal();

        info.addRole(user.getRole());
        log.info("username: " + user.getUsername() + " role: " + user.getRole());
        return info;
    }

    /**
     * 用户登录验证
     * @param authenticationToken 验证带有用户登录信息的令牌
     * @return 登录验证结果
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token =(UsernamePasswordToken) authenticationToken;

        String username = token.getUsername();
        User user = userRepo.findByUsername(username);
        if(user == null) {
            throw new UnknownAccountException();
        }
        String passwordInDB = user.getPassword();
        // 对盐进行加密处理
        ByteSource salt = ByteSource.Util.bytes(user.getSalt());

        /* 传入密码自动判断是否正确
         * 参数1：传入对象给Principal
         * 参数2：正确的用户密码
         * 参数3：加盐处理
         * 参数4：固定写法
         */
        return new SimpleAuthenticationInfo(user, passwordInDB, salt, getName());
    }
}
