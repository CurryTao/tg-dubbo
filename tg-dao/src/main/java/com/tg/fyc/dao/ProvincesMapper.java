package com.tg.fyc.dao;

import com.tg.fyc.pojo.Provinces;

public interface ProvincesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Provinces record);

    int insertSelective(Provinces record);

    Provinces selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Provinces record);

    int updateByPrimaryKey(Provinces record);
}