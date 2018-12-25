package com.tg.fyc.dao;

import java.util.List;

import com.tg.fyc.pojo.Brand;
import com.tg.fyc.pojo.ItemCat;
import com.tg.fyc.pojo.TypeTemplate;

public interface TypeTemplateMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TypeTemplate record);

    int insertSelective(TypeTemplate record);

    TypeTemplate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TypeTemplate record);

    int updateByPrimaryKey(TypeTemplate record);

	List<Brand> findlist(TypeTemplate typeTemplate);

	TypeTemplate findOneList(Long id);

	List<TypeTemplate> findAll();
}