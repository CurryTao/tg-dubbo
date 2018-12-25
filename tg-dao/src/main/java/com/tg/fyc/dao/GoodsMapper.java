package com.tg.fyc.dao;

import java.util.List;

import com.tg.fyc.pojo.Goods;

public interface GoodsMapper {
	
    int deleteByPrimaryKey(Long id);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);

	List<Goods> selectFindPage(Goods goods);

	void updateStatus(Long long1);

	List<Goods> selectFindPage2(Goods goods);

	List<Goods> findAll(Goods goods);
}