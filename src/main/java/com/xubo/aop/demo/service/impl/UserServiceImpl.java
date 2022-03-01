package com.xubo.aop.demo.service.impl;

import com.xubo.aop.demo.annotation.TestServiceWatch;
import com.xubo.aop.demo.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Author xubo
 * @Date 2022/3/1 16:39
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    @TestServiceWatch(annoArg0 = "参数0", annoArg1 = "参数1")
    public String queryAllUser() {
        return "Druid";
    }

    @Override
    @TestServiceWatch(annoArg0 = "参数0", annoArg1 = "参数1")
    public String queryUserById(String id) {
        return id + " : Druid";
    }
}
