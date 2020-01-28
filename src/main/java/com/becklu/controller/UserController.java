package com.becklu.controller;

import com.becklu.controller.viewobject.UserVO;
import com.becklu.error.BusinessException;
import com.becklu.error.EmBusinessError;
import com.becklu.response.CommonReturnType;
import com.becklu.service.UserService;
import com.becklu.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController{
    @Autowired
    private UserService userService;

    //用户登录接口
    //出参，入参
    @RequestMapping(value = "/login",method={RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED} )
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telephone") String telephone,
                                  @RequestParam(name = "password") String password,
                                  HttpServletRequest request) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if(StringUtils.isEmpty(telephone)||
                StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户名和密码不能为空");
        }
        //调用用户登录服务，用来校验用户登录是否合法
        String encrptPassword = this.encodeByMd5(password);
        System.out.println(encrptPassword);
        UserModel userModel = userService.validateLogin(telephone,encrptPassword);

        //将登录凭证加入到用户登录成功的session内
        //一般用户登录凭证会使用token这样级别的，而不使用session
        request.getSession().setAttribute("IS_LOGIN",true);
        request.getSession().setAttribute("LOGIN_USER",userModel);

        return CommonReturnType.create(null);
    }

    //用户注册接口
    @RequestMapping(value = "/register",method={RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED} )
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "name")String name,
                                     @RequestParam(name = "gender")Byte gender,
                                     @RequestParam(name = "age") Integer age,
                                     @RequestParam(name = "telephone") String telephone,
                                     @RequestParam(name = "otpCode") String otpCode,
                                     @RequestParam(name = "password") String password,
                                     HttpServletRequest request) throws BusinessException, NoSuchAlgorithmException, UnsupportedEncodingException {
        //1.根据telephone和otpCode判断 注册是否合法
        HttpSession session = request.getSession();
        String inSessionOtpCode = (String)session.getAttribute(telephone);
        System.out.println(inSessionOtpCode);
        if(null == otpCode || otpCode.equals("")||
                !otpCode.equals(inSessionOtpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //2.用户注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setAge(age);
        userModel.setTelephone(telephone);
        userModel.setRegisterMode("byPhone");
//        userModel.setEncrptPassword(MD5Encoder.encode(password.getBytes()));
        userModel.setEncrptPassword(encodeByMd5(password));
        System.out.println(userModel.getEncrptPassword());

        userService.register(userModel);

        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/getOtp",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    //用户获取otp短信验证码的接口
    public CommonReturnType getOtp(@RequestParam(name = "telephone") String telephone,HttpServletRequest request){
        //1.按照一定的方式生成otp验证码
            Random random = new Random();
            //设置随机数的上界
            int randomInt = random.nextInt(999999);
            String otpCode = String.valueOf(randomInt);
        //2.将otp验证码和对应用户的手机号相关联
        HttpSession session = request.getSession();
        session.setAttribute(telephone,otpCode);
        //3.将otp验证码通过短信通道发送给用户
        System.out.println(telephone+":验证码："+otpCode);
        //如果不带status参数，则默认添加 success状态
        // 而且，也不需要返回任何实际的data，所以，data设置为null
        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        //调用service返回对应id的用户对象并返回给前端
        UserModel userModel = userService.getUser(id);
        if(null == userModel){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
//            userModel.setEncrptPassword("1234566");
        }
        UserVO userVO = convertFromModel(userModel);

        //返回通用对象
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel){
        if(null == userModel){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }

    public String encodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest messageDigest = MessageDigest.getInstance("md5");
        BASE64Encoder base64Encoder = new BASE64Encoder();

        //加密字符串
        String newStr = base64Encoder.encode(messageDigest.digest(str.getBytes("utf-8")));
        return newStr;
    }

}
