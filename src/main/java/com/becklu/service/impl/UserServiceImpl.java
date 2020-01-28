package com.becklu.service.impl;

import com.becklu.dao.UserDOMapper;
import com.becklu.dao.UserPasswordDOMapper;
import com.becklu.dataobject.UserDO;
import com.becklu.dataobject.UserPasswordDO;
import com.becklu.error.BusinessException;
import com.becklu.error.EmBusinessError;
import com.becklu.service.UserService;
import com.becklu.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
    public class UserServiceImpl implements UserService{
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Override
    public UserModel getUser(Integer id) {
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if(null == userDO){
            return null;
        }

        //通过用户id 获取对应的用户加密密码
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        return convertFromUserObject(userDO,userPasswordDO);
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if(null == userModel){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        if(StringUtils.isEmpty(userModel.getName())||
                StringUtils.isEmpty(userModel.getTelephone())||
                StringUtils.isEmpty(userModel.getEncrptPassword())||
                userModel.getGender() == null ||
                userModel.getAge() == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //userModel --》 userDO
        UserDO userDO = convertFromModel(userModel);
        try {
            userDOMapper.insertSelective(userDO);
        } catch (DuplicateKeyException ex) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户已存在");
        }
        //userModel --> userPasswordDO
        //此时userModel中并没有Id字段的值，虽然userDO中已经有了
        //所以需要手动设置 userModel.id 才能传给 userPasswordDO使用
        userModel.setId(userDO.getId());
        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);


    }

    @Override
    public UserModel validateLogin(String telephone, String encrptPassword) throws BusinessException {
        //通过手机号查找用户信息
        UserDO userDO = userDOMapper.selectByTelephone(telephone);
        if(userDO == null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        //对比登录密码 是否和 数据库内对应的密码一致
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        if(!userPasswordDO.getEncrptPassword().equals(encrptPassword)){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }

        UserModel userModel = convertFromUserObject(userDO,userPasswordDO);
        return userModel;
    }

    private UserModel convertFromUserObject(UserDO userDO, UserPasswordDO userPasswordDO){
        UserModel userModel = new UserModel();
        if(null == userDO){
            return null;
        }
        BeanUtils.copyProperties(userDO,userModel);
        userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        return userModel;
    }

    private UserDO convertFromModel(UserModel userModel){
        if(null == userModel){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
    }

    private UserPasswordDO convertPasswordFromModel(UserModel userModel){
        if(null == userModel){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setUserId(userModel.getId());
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        return userPasswordDO;
    }
}
