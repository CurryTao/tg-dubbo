package com.tg.fyc.dao;

import com.tg.fyc.pojo.PayLog;

public interface PayLogMapper {
	
    int deleteByPrimaryKey(String outTradeNo);

    int insert(PayLog record);

    int insertSelective(PayLog record);

    PayLog selectByPrimaryKey(String outTradeNo);

    int updateByPrimaryKeySelective(PayLog record);

    int updateByPrimaryKey(PayLog record);
}