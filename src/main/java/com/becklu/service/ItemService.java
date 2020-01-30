package com.becklu.service;

import com.becklu.error.BusinessException;
import com.becklu.service.model.ItemModel;

import java.util.List;

public interface ItemService {
    //创建商品接口
    ItemModel createItem(ItemModel itemModel) throws BusinessException;
    //商品列表浏览接口
    List<ItemModel> listItems();
    //商品详情浏览接口
    ItemModel getItemById(Integer id);
    //扣减库存接口
    boolean decreaseStock(Integer itemId,Integer amount) throws BusinessException;
    //增加商品销量接口
    void increaseSales(Integer itemId,Integer amount) throws BusinessException;
}
