package com.xiaxinyu.mall.controller;

import com.xiaxinyu.mall.model.pojo.User;
import com.xiaxinyu.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 用户控制器
 * @author: xiaxinyu
 * @Email: xiaxinyuxy@163.com
 * @date: 2022年12月02日 17:06
 * @Copyright:
 * @version: 1.0.0
 */

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/test")
    public User personalPage(){
        return userService.getUser();
    }
}