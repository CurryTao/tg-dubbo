package com.tg.fyc.dao;

import java.util.List;
import java.util.Map;

import com.tg.fyc.pojo.Brand;
import com.tg.fyc.pojo.Specification;

public interface SpecificationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Specification record);

    int insertSelective(Specification record);

    Specification selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Specification record);

    int updateByPrimaryKey(Specification record);

	List<Brand> findlist(Specification specification);

	List<Map<String, String>> list();
}