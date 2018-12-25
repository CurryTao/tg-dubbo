package com.tg.fyc.dao;

import java.util.List;

import com.tg.fyc.pojo.Promotion;

public interface PromotionMapper {
	
    int deleteByPrimaryKey(Long id);

    int insert(Promotion record);

    int insertSelective(Promotion record);

    Promotion selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Promotion record);

    int updateByPrimaryKey(Promotion record);

	List<Promotion> findlistForSeller(Promotion promotion);
	
	List<Promotion> findlistForManger(Promotion promotion);

	List<Promotion> findAll(Long sellerId);

	List<Promotion> findListByGoodsId(Long goodsId);
}