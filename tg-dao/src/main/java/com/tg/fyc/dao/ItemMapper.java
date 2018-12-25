package com.tg.fyc.dao;

import java.util.List;

import com.tg.fyc.pojo.Item;

public interface ItemMapper {
	
    int deleteByPrimaryKey(Long id);

    int insert(Item record);

    int insertSelective(Item record);

    Item selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Item record);

    int updateByPrimaryKey(Item record);

	List<Item> selectByExample(Long id);

	void deleteByGoodsId(Long id);

	List<Item> selectByExample1(Item item);

	List<Item> selectByExample2(Item item);

	List<Item> selectByExample3(Item item);

	List<Item> findAll();
}