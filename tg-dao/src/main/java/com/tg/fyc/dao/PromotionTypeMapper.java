package com.tg.fyc.dao;

import java.util.List;

import com.tg.fyc.pojo.PromotionType;

public interface PromotionTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionType record);

    int insertSelective(PromotionType record);

    PromotionType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PromotionType record);

    int updateByPrimaryKey(PromotionType record);

	List<PromotionType> getPromotionTypeList();
}