package com.tg.fyc.dao;

import java.util.List;
import java.util.Map;

import com.tg.fyc.pojo.Brand;

public interface BrandMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Brand record);

    int insertSelective(Brand record);

    Brand selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Brand record);

    int updateByPrimaryKey(Brand record);
    
    List<Map<String, String>> list();
    
	List<Brand> findlist(Brand brand);
}