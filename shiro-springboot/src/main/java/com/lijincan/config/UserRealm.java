package com.lijincan.config;

import com.lijincan.pojo.User;
import com.lijincan.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义Realm类
 * 只需要继承AuthorizingRealm
 *
 * @author: wangfei
 * @date: 2020年02月26日 13:19
 * @Description: TODO
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;

    /**
     * 授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了=>授权doGetAuthorizationInfo");

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //info.addStringPermission("user:add");
        //获取当前登录的对象
        Subject subject = SecurityUtils.getSubject();

        /**
         * 从下边的认证过程传过来的user,user是根据用户名从数据库中拿到的，包含权限信息
         */
        User currentUser = (User) subject.getPrincipal();
        /**
         * 授权操作
         */
        info.addStringPermission(currentUser.getPrams());

        return info;
    }

    /**
     * 认证
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行了=>认证doGetAuthenticationInfo");

        /**
         * 转换令牌Token的类型
         */
        UsernamePasswordToken userToken = (UsernamePasswordToken) token;

        /**
         * 从token中取到用户名再去查用户密码
         * 数据库中取密码
         */
        User user = userService.queryUserByName(userToken.getUsername());

        if (user == null) {
            return null;//UnknownAccountException
        }
        /**
         * 获取当前的用户对象
         */
        Subject currentSubject = SecurityUtils.getSubject();
        Session session = currentSubject.getSession();
        session.setAttribute("loginUser", user);

        /**
         * 认证，密码可以加密 MD5
         */
        return new SimpleAuthenticationInfo(user, user.getPwd(), "");
    }
}
