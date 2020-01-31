package com.becklu.service.model;

import java.math.BigDecimal;

public class OrderModel {
    private String id;
    //下单的用户
    private Integer UserId;
    //商品Id
    private Integer ItemId;
    //商品原价
//    private BigDecimal price;
    //是否以秒杀商品下单，不为空表示秒杀下单
    private Integer promoId;
    //商品实际成交价（考虑促销活动）
    private BigDecimal itemPrice;
    //购买的件数
    private Integer amount;
    //购买的金额
    private BigDecimal payment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

/*    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }*/

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getUserId() {
        return UserId;
    }

    public void setUserId(Integer userId) {
        UserId = userId;
    }

    public Integer getItemId() {
        return ItemId;
    }

    public void setItemId(Integer itemId) {
        ItemId = itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }
}
