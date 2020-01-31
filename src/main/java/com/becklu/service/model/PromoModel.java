package com.becklu.service.model;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public class PromoModel {
    private Integer id;

    //秒杀活动名称
    private String promoName;
    //秒杀活动开始时间
    private DateTime startDate;
    //秒杀活动结束时间
    private DateTime endDate;
    //秒杀活动的适用商品，为了简单起见，这里一个活动只对应一个商品，一个商品也只参加一个活动
    private Integer itemId;
    //秒杀活动商品的价格，因为只对应一个商品，可以这么表示
    private BigDecimal promoItemPrice;

    //冗余字段，秒杀活动状态，为1表示还未开始，2表示进行中，3表示已经结束
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getPromoItemPrice() {
        return promoItemPrice;
    }

    public void setPromoItemPrice(BigDecimal promoItemPrice) {
        this.promoItemPrice = promoItemPrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
