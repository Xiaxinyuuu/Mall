package com.xiaxinyu.mall.service.impl;

import com.xiaxinyu.mall.exception.ExceptionEnum;
import com.xiaxinyu.mall.exception.MyException;
import com.xiaxinyu.mall.model.dao.UserMapper;
import com.xiaxinyu.mall.model.pojo.User;
import com.xiaxinyu.mall.service.UserService;
import com.xiaxinyu.mall.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

/**
 * @Description:
 * @author: xiaxinyu
 * @Email: xiaxinyuxy@163.com
 * @date: 2022年12月02日 17:10
 * @Copyright:
 * @version: 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User getUser() {
        return userMapper.selectByPrimaryKey(1);
    }

    @Override
    public void register(String username, String password) throws MyException {
        //查询用户名是否存在，不允许重名
        User result = userMapper.selectByName(username);
        if(result != null){
            throw new MyException(ExceptionEnum.NAME_EXISTED);
        }

        //写到数据库
        User user = new User();
        user.setUsername(username);
        try {
            user.setPassword(MD5Utils.getMD5Str(password));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        int count = userMapper.insertSelective(user);
        if(count == 0)
            throw new MyException(ExceptionEnum.INSERT_FAILED);

    }

    @Override
    public User login(String username, String password) throws MyException {
        String md5Password = null;
        try{
            md5Password = MD5Utils.getMD5Str(password);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        User user = userMapper.selectLogin(username,md5Password);

        if(user == null){
            throw new MyException(ExceptionEnum.WRONG_PASSWORD);
        }

        return user;
    }


    @Override
    public void updateSignature(User user) throws MyException {
        //更新个性签名
        int count = userMapper.updateByPrimaryKeySelective(user);
        if(count != 1)
            throw new MyException(ExceptionEnum.UPDATE_FAILED);
    }

    @Override
    public boolean checkAdminRole(User user){
        //1——普.通用户，2——管理员
        return user.getRole().equals(2);
    }
}