# shiro-springboot
跟着b站狂神视频写的shiro整合spring boot的入门demo。
## 1. 导包
~~~xml
        <!--Shrio核心包-->
        <dependency>
            <groupId>org.apache.shiro</groupId>
            <artifactId>shiro-core</artifactId>
            <version>1.4.1</version>
        </dependency>
        <!--Shrio整合Spring的包-->
        <dependency>
            <groupId>org.apache.shiro</groupId>
            <artifactId>shiro-spring</artifactId>
            <version>1.4.1</version>
        </dependency>
~~~
## 2. 编写配置类
com.lijincan.config.ShiroConfig
~~~java
@Configuration
public class ShiroConfig {

    //shiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager")DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(defaultWebSecurityManager);

        //添加shiro的内置过滤器
        /*
            anon: 无需认证就可访问
            authc：必须认证才能访问
            user：必须拥有记住我功能才能访问      要实现记住我，实体类必须继承系列化接口Serializable
            perms: 拥有对某个资源的权限才能访问
            role:拥有某个角色权限才能访问
       */

        /**
         * 拦截
         */
        Map<String, String> filterMap = new LinkedHashMap<>();

        /*授权*/
        filterMap.put("/login","anon");
        filterMap.put("/user/add","authc,perms[user:add]");
        filterMap.put("/user/update","user");

//        filterMap.put("/user/add","authc");
//        filterMap.put("/user/update","authc");
//        filterMap.put("/user/*","authc");
        /*设置登出*/
        filterMap.put("/logout", "logout");

        /**
         * 设置一个过滤器的链
         */
        bean.setFilterChainDefinitionMap(filterMap);

        //设置登录请求
        bean.setLoginUrl("/toLogin");
        //设置未授权页面
        bean.setUnauthorizedUrl("/noauth");

        return bean;
    }

    /**
     * DafaultWebSecurituManager
     * 获得默认的安全管理器，管理所有用户
     * @param userRealm
     * @return
     */
    @Bean(name="securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联UserRealm
        securityManager.setRealm(userRealm);

        return securityManager;
    }
    /**
     * 创建realm对象 ，需要自定义
     * */
    @Bean(name = "userRealm")
    public UserRealm userRealm(){
        return new UserRealm();
    }

    //整合thymeleaf
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }
}
~~~
