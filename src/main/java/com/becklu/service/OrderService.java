package com.becklu.service;

import com.becklu.error.BusinessException;
import com.becklu.service.model.OrderModel;

public interface OrderService {
    //创建订单项（下单/购买）接口
    //1.通过前端url传递过来秒杀活动id，如果为null则表示不是秒杀下单
    //下单接口内需要校验是否该活动和商品是否匹配，并且活动已经开始
    //2.直接在接口内判断商品是否存在，而且参加了正在进行的秒杀活动（如果一个商品存在多个促销活动，这样的
    // 方式当然是不可取的，而且如果对于平销的商品也要做数据库访问查询，对于性能的影响很大）
    public OrderModel createOrder(Integer userId,Integer itemId,Integer promoId,Integer amount) throws BusinessException;

}
