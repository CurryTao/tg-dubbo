package com.tg.fyc.seckill.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.tg.fyc.dao.SeckillGoodsMapper;
import com.tg.fyc.pojo.SeckillGoods;
import com.tg.fyc.seckill.api.SeckillApi;

@Service
public class SeckillApiImpl implements SeckillApi{

	@Autowired
	private SeckillGoodsMapper seckillGoodsMapper;

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public List<SeckillGoods> findList() {

		List<SeckillGoods> seckillGoodsList = redisTemplate.boundHashOps("seckillGoods").values();

		if(seckillGoodsList==null || seckillGoodsList.size()==0){
			seckillGoodsList=seckillGoodsMapper.getseckillGoodsList();
			for (SeckillGoods seckillGoods : seckillGoodsList) {
				redisTemplate.boundHashOps("seckillGoods").put(seckillGoods.getId(), seckillGoods);
			}
		}
		return seckillGoodsList;
	}

	@Override
	public SeckillGoods findOneFromRedis(Long id) {
		return (SeckillGoods) redisTemplate.boundHashOps("seckillGoods").get(id);
	}
	
	@Override
	public void AddSeckillGoodsToRedis(Long SeckillGoodsId) {
		SeckillGoods selectByPrimaryKey = seckillGoodsMapper.selectByPrimaryKey(SeckillGoodsId);
		redisTemplate.boundHashOps("seckillGoods").putIfAbsent(SeckillGoodsId, selectByPrimaryKey);
	}

	
}
