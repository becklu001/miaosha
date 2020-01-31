package com.becklu.controller;

import com.becklu.error.BusinessException;
import com.becklu.error.EmBusinessError;
import com.becklu.response.CommonReturnType;
import com.becklu.service.OrderService;
import com.becklu.service.model.OrderModel;
import com.becklu.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController("order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class OrderController extends BaseController{
    @Autowired
    private OrderService orderService;

    //创建订单接口
    @RequestMapping(value = "/createOrder",method = {RequestMethod.POST})
    public CommonReturnType createOrder(@RequestParam(name = "itemId") Integer itemId,
                                        @RequestParam(name = "amount") Integer amount,
                                        @RequestParam(name = "promoId") Integer promoId,
                                        HttpServletRequest request) throws BusinessException {
        //用户id并不是前端页面送过来的，用户必须登录然后才能下单，我们要先验证用户是否登录
        //用户的登录信息我们是放在 session中的
        Boolean isLogin = (Boolean) request.getSession().getAttribute("IS_LOGIN");
        if(isLogin == null || isLogin.equals("false")){
            throw  new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户还未登录");
        }
        UserModel userModel = (UserModel) request.getSession().getAttribute("LOGIN_USER");

        //调用orderService创建订单接口
        OrderModel orderModel = orderService.createOrder(userModel.getId(),itemId,promoId,amount);
        //下单成功后，前端页面不需要展示订单信息，所以并没有返回OrderVO
        //复杂的案例中是会返回这些信息的
        return CommonReturnType.create(null);
    }
}
