package com.becklu.service;

import com.becklu.error.BusinessException;
import com.becklu.service.model.OrderModel;

public interface OrderService {
    //创建订单项（下单/购买）接口
    public OrderModel createOrder(Integer userId,Integer itemId,Integer amount) throws BusinessException;

}
