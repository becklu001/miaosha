package com.becklu.service.impl;

import com.becklu.dao.OrderDOMapper;
import com.becklu.dao.SequenceDOMapper;
import com.becklu.dataobject.OrderDO;
import com.becklu.dataobject.SequenceDO;
import com.becklu.error.BusinessException;
import com.becklu.error.EmBusinessError;
import com.becklu.service.ItemService;
import com.becklu.service.OrderService;
import com.becklu.service.UserService;
import com.becklu.service.model.ItemModel;
import com.becklu.service.model.OrderModel;
import com.becklu.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderDOMapper orderDOMapper;
    @Autowired
    private SequenceDOMapper sequenceDOMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer promoId,Integer amount) throws BusinessException {
        //1.校验下单状态：
        //下单的商品是否存在，用户是否合法，购买数量是否正确
        ItemModel itemModel = itemService.getItemById(itemId);
        if(null == itemModel){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"商品信息不存在");
        }
        UserModel userModel = userService.getUser(userId);
        if(null == userModel){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户不存在");
        }
        if(amount <= 0){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"数量信息不正确");
        }
        //校验活动信息
        if( promoId != null){
            //校验秒杀活动是否适用于该商品
            if( promoId.intValue()!= itemModel.getPromoModel().getId()){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"活动信息不正确");
            }
            //活动是否正在进行中
            if(itemModel.getPromoModel().getStatus()!=2){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"活动信息不正确");
            }
        }
        //2.落单减库存
        boolean result = itemService.decreaseStock(itemId,amount);
        if(!result){
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }
        //3.订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setItemId(itemId);
        orderModel.setUserId(userId);
        if(promoId != null){
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else{
            orderModel.setItemPrice(itemModel.getPrice());
        }
        orderModel.setPromoId(promoId);
        orderModel.setAmount(amount);
        orderModel.setPayment(itemModel.getPrice().multiply(new BigDecimal(amount)));

        OrderDO orderDO = convertOrderDOFromOrderModel(orderModel);
        //如何生成订单号呢？
        //生成交易流水号以及订单号
        orderDO.setId(generateOrderNo());
        orderDOMapper.insertSelective(orderDO);

        //更新商品销量
        itemService.increaseSales(itemId,amount);
        //4.返回前端

        return orderModel;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generateOrderNo(){
        //订单号有16位
        //前8位为时间序列 yyyy-mm-dd
        StringBuilder stringBuilder = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-","");
        stringBuilder.append(nowDate);
        //中间6位为自增序列
        //获取当前订单号的sequence
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_sequence");
        int sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue()+sequenceDO.getStep());
        //更新sequence数据库表
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);
        String sequenceStr = String.valueOf(sequence);
        for(int i=0;i<6-sequenceStr.length();i++){
            //不足的位数用0填充
            stringBuilder.append("0");
        }
        stringBuilder.append(sequenceStr);
        //最后2位为分库分表位,暂时写死为00
        stringBuilder.append("00");
        return stringBuilder.toString();
    }
    private OrderDO convertOrderDOFromOrderModel(OrderModel orderModel){
        if(null == orderModel){
            return null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel,orderDO);
        return orderDO;
    }
}
