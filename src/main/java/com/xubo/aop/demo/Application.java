package com.xubo.aop.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author xubo
 * @Date 2022/3/1 16:36
 * 这个 UserServiceImpl 里面的很多方法都是用 protected 修饰的
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
