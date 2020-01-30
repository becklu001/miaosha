package com.becklu.dao;

import com.becklu.dataobject.ItemStockDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

public interface ItemStockDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ItemStockDO record);

    int insertSelective(ItemStockDO record);

    ItemStockDO selectByPrimaryKey(Integer id);

    ItemStockDO selectByItemId(Integer itemId);

    //返回值为影响的条目数，因为有两个参数，需要指定Param
//    int decreaseStock(@RequestParam(name = "itemId") Integer itemId,
//                      @RequestParam(name = "amount") Integer amount);
    int decreaseStock(@Param("itemId") Integer itemId,
                      @Param("amount") Integer amount);

    int updateByPrimaryKeySelective(ItemStockDO record);

    int updateByPrimaryKey(ItemStockDO record);
}