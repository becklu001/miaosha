package com.becklu.service.impl;

import com.becklu.dao.ItemDOMapper;
import com.becklu.dao.ItemStockDOMapper;
import com.becklu.dataobject.ItemDO;
import com.becklu.dataobject.ItemStockDO;
import com.becklu.error.BusinessException;
import com.becklu.error.EmBusinessError;
import com.becklu.service.ItemService;
import com.becklu.service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemDOMapper itemDOMapper;
    @Autowired
    private ItemStockDOMapper itemStockDOMapper;

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        //1.入参校验
        if(itemModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //2.ItemModel -->dataObject
        //3.写入数据库
        ItemDO itemDO = convertItemDOFromModel(itemModel);
        itemDOMapper.insertSelective(itemDO);

        ItemStockDO itemStockDO = convertItemStockDOFromModel(itemModel);
        itemStockDO.setItemId(itemDO.getId());
        itemStockDOMapper.insertSelective(itemStockDO);

        //4.返回创建好的对象
//        return itemModel;
        return this.getItemById(itemDO.getId());
    }

    //商品浏览服务接口
    @Override
    public List<ItemModel> listItems() {
        List<ItemDO> itemDOList = itemDOMapper.listItems();
        //java8的stream api 好好掌握一下
        List<ItemModel> itemModelList = itemDOList.stream().map(itemDO ->{
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
            ItemModel itemModel = convertItemModelFromDO(itemDO,itemStockDO);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(id);
        if(null == itemDO){
            return null;
        }
        //查找库存信息
        ItemModel itemModel = convertItemModelFromDO(itemDO,itemStockDO);

        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException {
        int affectedRow = itemStockDOMapper.decreaseStock(itemId,amount);
        //如果减库存不成功，则返回值小于0
        if(affectedRow > 0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {
        itemDOMapper.increaseSales(itemId,amount);
    }

    private ItemDO convertItemDOFromModel(ItemModel itemModel){
        if(null == itemModel){
            return null;
        }
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel,itemDO);
        return itemDO;
    }

    private ItemStockDO convertItemStockDOFromModel(ItemModel itemModel){
        if(itemModel == null){
            return null;
        }
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setStock(itemModel.getStock());
        return itemStockDO;
    }

    private ItemModel convertItemModelFromDO(ItemDO itemDO,ItemStockDO itemStockDO){
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO,itemModel);
        itemModel.setStock(itemStockDO.getStock());
        return itemModel;
    }
}
