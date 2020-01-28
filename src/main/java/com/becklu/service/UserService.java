package com.becklu.service;

import com.becklu.error.BusinessException;
import com.becklu.service.model.UserModel;
import org.springframework.stereotype.Service;

public interface UserService {
    //通过用户id获取用户对象
    public UserModel getUser(Integer id);
    //用户注册流程
    public void register(UserModel userModel) throws BusinessException;
    //用户登录验证服务
    public UserModel validateLogin(String telephone,String encrptPassword) throws BusinessException;
}
