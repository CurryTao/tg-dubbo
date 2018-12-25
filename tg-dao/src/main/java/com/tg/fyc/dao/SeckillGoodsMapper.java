package com.tg.fyc.dao;

import java.util.List;

import com.tg.fyc.pojo.SeckillGoods;

public interface SeckillGoodsMapper {
	
    int deleteByPrimaryKey(Long id);

    int insert(SeckillGoods record);

    int insertSelective(SeckillGoods record);

    SeckillGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SeckillGoods record);

    int updateByPrimaryKey(SeckillGoods record);

	List<SeckillGoods> getseckillGoodsList();
	
}