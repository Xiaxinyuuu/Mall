package com.xiaxinyu.mall.controller;

import com.xiaxinyu.mall.common.ApiRestResponse;
import com.xiaxinyu.mall.common.Constant;
import com.xiaxinyu.mall.exception.ExceptionEnum;
import com.xiaxinyu.mall.exception.MyException;
import com.xiaxinyu.mall.model.pojo.User;
import com.xiaxinyu.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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

    @PostMapping("/user/register")
    public ApiRestResponse register(@RequestParam Map<String,String> map) throws MyException {
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


    @PostMapping("/user/login")
    public ApiRestResponse login(@RequestParam Map<String,String> map, HttpSession session) throws MyException {
        String username = map.get("userName");
        String password = map.get("password");
        if(StringUtils.isEmpty(username)){
            return ApiRestResponse.error(ExceptionEnum.NEED_USER_NAME);
        }
        if(StringUtils.isEmpty(password)){
            return ApiRestResponse.error(ExceptionEnum.NEED_PASSWORD);
        }

        User user = userService.login(username, password);
        //保存用户信息时，不保存密码
        user.setPassword(null);
        session.setAttribute(Constant.MALL_USER,user);
        return ApiRestResponse.success(user);
    }

    @PostMapping("/user/update")
    public ApiRestResponse updateUserInfo(@RequestParam Map<String,String> map,HttpSession session) throws MyException {
        String signature = map.get("signature");
        User curUser = (User)session.getAttribute(Constant.MALL_USER);
        System.out.println(curUser);
        if(curUser == null)
            return ApiRestResponse.error(ExceptionEnum.NEED_LOGIN);
        User user = new User();
        user.setId(curUser.getId());
        user.setPersonalizedSignature(signature);
        userService.updateSignature(user);
        return ApiRestResponse.success();
    }

    @PostMapping("/user/logout")
    public ApiRestResponse logout(HttpSession session){
        session.removeAttribute(Constant.MALL_USER);
        return ApiRestResponse.success();
    }

    @PostMapping("/user/adminLogin")
    public ApiRestResponse adminLogin(@RequestParam Map<String,String> map, HttpSession session) throws MyException {
        String username = map.get("userName");
        String password = map.get("password");
        if(StringUtils.isEmpty(username)){
            return ApiRestResponse.error(ExceptionEnum.NEED_USER_NAME);
        }
        if(StringUtils.isEmpty(password)){
            return ApiRestResponse.error(ExceptionEnum.NEED_PASSWORD);
        }

        User user = userService.login(username, password);
        if (userService.checkAdminRole(user)) {
            //保存用户信息时，不保存密码
            user.setPassword(null);
            session.setAttribute(Constant.MALL_USER,user);
            return ApiRestResponse.success(user);
        }else
            return ApiRestResponse.error(ExceptionEnum.NEED_ADMIN);

    }


}