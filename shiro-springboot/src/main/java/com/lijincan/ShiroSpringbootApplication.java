package com.lijincan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Shrio的三大组件：
 *      Subject:         用户
 *      SecurityManager: 管理所有用户
 *      Realm:           链接数据库
 */
@SpringBootApplication
public class ShiroSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroSpringbootApplication.class, args);
    }

}
