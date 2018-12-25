package com.tg.fyc.seckill.api;

import java.util.List;

import com.tg.fyc.pojo.SeckillGoods;

public interface SeckillApi{
	
	public List<SeckillGoods> findList();
	
	public SeckillGoods findOneFromRedis(Long id);
	
	public void AddSeckillGoodsToRedis(Long SeckillGoodsId);
}
