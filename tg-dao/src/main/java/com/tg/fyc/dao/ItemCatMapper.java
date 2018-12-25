package com.tg.fyc.dao;

import java.util.List;
import java.util.Map;

import com.tg.fyc.common.PageResult;
import com.tg.fyc.pojo.Brand;
import com.tg.fyc.pojo.ItemCat;

public interface ItemCatMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ItemCat record);

    int insertSelective(ItemCat record);

    ItemCat selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ItemCat record);

    int updateByPrimaryKey(ItemCat record);

	List<ItemCat> findlist(ItemCat itemCat);

	List<Map<String, Object>> list();

	void updateByupdate(ItemCat itemCat);

	List<ItemCat> ByParentId(Long parentId);

	ItemCat findOne(Long id);

	List<Map<String, Object>> list2();

	List<ItemCat> findAll();

}