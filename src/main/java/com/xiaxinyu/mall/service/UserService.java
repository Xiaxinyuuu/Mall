package com.xiaxinyu.mall.service;

import com.xiaxinyu.mall.exception.MyException;
import com.xiaxinyu.mall.model.pojo.User;

public interface UserService {
    User getUser();
    void register(String username,String password) throws MyException;

    User login(String username, String password) throws MyException;

    void updateSignature(User user) throws MyException;

    boolean checkAdminRole(User user);
}
