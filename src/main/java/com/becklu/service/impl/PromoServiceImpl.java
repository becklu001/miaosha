package com.becklu.service.impl;

import com.becklu.dao.PromoDOMapper;
import com.becklu.dataobject.PromoDO;
import com.becklu.service.PromoService;
import com.becklu.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromoServiceImpl implements PromoService{
    @Autowired
    private PromoDOMapper promoDOMapper;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        PromoDO promoDO = promoDOMapper.getPromoByItemId(itemId);
        PromoModel promoModel = convertPromoModelFromPromoDO(promoDO);
        if(promoModel == null){
            return null;
        }

        //判断秒杀活动是否即将开始 或者正在进行中
        if(promoModel.getStartDate().isAfterNow()){
            //秒杀开始时间晚于现在，表示即将开始，status设置为1
            promoModel.setStatus(1);
        }else if(promoModel.getEndDate().isBeforeNow()){
            //开始时间早于现在，而且结束时间早于现在，则表示活动已经结束
            promoModel.setStatus(3);
        }else{
            //开始时间早于等于现在，而且结束时间晚于现在，表示活动进行中
            promoModel.setStatus(2);
        }

        return promoModel;
    }

    private PromoModel convertPromoModelFromPromoDO(PromoDO promoDO){
        if(promoDO == null){
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO,promoModel);
        promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
        return promoModel;
    }
}
