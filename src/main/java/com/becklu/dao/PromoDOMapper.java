package com.becklu.dao;

import com.becklu.dataobject.PromoDO;

public interface PromoDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromoDO record);

    int insertSelective(PromoDO record);

    PromoDO selectByPrimaryKey(Integer id);

    PromoDO getPromoByItemId(Integer itemId);

    int updateByPrimaryKeySelective(PromoDO record);

    int updateByPrimaryKey(PromoDO record);
}