package com.tg.fyc.dao;

import com.tg.fyc.pojo.FreightTemplate;

public interface FreightTemplateMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FreightTemplate record);

    int insertSelective(FreightTemplate record);

    FreightTemplate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FreightTemplate record);

    int updateByPrimaryKey(FreightTemplate record);
}