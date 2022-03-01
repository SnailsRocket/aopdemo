package com.xubo.aop.demo.controller;

import com.xubo.aop.demo.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author xubo
 * @Date 2022/3/1 16:37
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @GetMapping(value = "/")
    public String queryAllUser() {
        return userService.queryAllUser();
    }

    @GetMapping(value = "/{id}")
    public String queryById(@PathVariable("id") String id) {
        return userService.queryUserById(id);
    }


}
