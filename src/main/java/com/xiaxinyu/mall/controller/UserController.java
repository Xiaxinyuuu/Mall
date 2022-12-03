package com.xiaxinyu.mall.controller;

import com.xiaxinyu.mall.common.ApiRestResponse;
import com.xiaxinyu.mall.exception.ExceptionEnum;
import com.xiaxinyu.mall.exception.MyException;
import com.xiaxinyu.mall.model.pojo.User;
import com.xiaxinyu.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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


    @PostMapping("/register")
    public ApiRestResponse register(@RequestParam Map<String,String> map) throws MyException {
        System.out.println(map.toString());
        String username = map.get("userName");
        String password = map.get("password");
        if(StringUtils.isEmpty(username)){
            return ApiRestResponse.error(ExceptionEnum.NEED_USER_NAME);
        }

        if(StringUtils.isEmpty(password)){
            return ApiRestResponse.error(ExceptionEnum.NEED_PASSWORD);
        }

        //密码长度不能少于8位
        if(password.length() < 8){
            return ApiRestResponse.error(ExceptionEnum.PASSWORD_TOO_SHORT);
        }


        userService.register(username,password);

        return ApiRestResponse.success();
    }
}