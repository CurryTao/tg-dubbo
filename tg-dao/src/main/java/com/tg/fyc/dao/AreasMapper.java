package com.tg.fyc.dao;

import com.tg.fyc.pojo.Areas;

public interface AreasMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Areas record);

    int insertSelective(Areas record);

    Areas selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Areas record);

    int updateByPrimaryKey(Areas record);
}