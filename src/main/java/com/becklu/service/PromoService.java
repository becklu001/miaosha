package com.becklu.service;

import com.becklu.service.model.PromoModel;

public interface PromoService {
    // 查找正在进行或者即将进行的秒杀活动 服务接口
    PromoModel getPromoByItemId(Integer itemId);
}
