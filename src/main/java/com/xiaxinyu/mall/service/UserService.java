package com.xiaxinyu.mall.service;

import com.xiaxinyu.mall.exception.MyException;
import com.xiaxinyu.mall.model.pojo.User;

public interface UserService {
    User getUser();
    void register(String username,String password) throws MyException;
}
