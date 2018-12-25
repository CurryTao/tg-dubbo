package com.tg.fyc.task.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tg.fyc.dao.SeckillGoodsMapper;
import com.tg.fyc.pojo.SeckillGoods;

@Component
public class SeckillGoodsService {

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private SeckillGoodsMapper seckillGoodsMapper;

	/**
	 * 每分钟更新秒杀数据
	 */
	@Scheduled(cron="0 * * * * ?")
	public void refreshSeckillGoods(){

		//查询缓存中的秒杀商品ID集合
		List<Long> seckillGoodsIdListForRedis =  new ArrayList(redisTemplate.boundHashOps("seckillGoods").keys());

		//查询mysql中合格数据
		List<SeckillGoods> seckillGoodsListForMySql = this.seckillGoodsMapper.getseckillGoodsList();

		//redis中不存在数据
		if(seckillGoodsIdListForRedis!=null && seckillGoodsIdListForRedis.size()>0){
			
			//mySql中存在数据
			if(seckillGoodsListForMySql!=null && seckillGoodsListForMySql.size()>0){
				
				//筛选出redis中没有的合格数据放入到redis
				for (SeckillGoods seckillGoods : seckillGoodsListForMySql) {
					//循环的该数据存在状态
					boolean haveData=false;
					//遍历redis中的数据，查询是否在redis中已经存在
					for(Long seckillIds:seckillGoodsIdListForRedis){
						//数据存在，更改数据状态值，结束循环
						if(seckillGoods.getId() == seckillIds) {
							haveData=true;
							break;
						}
					}
					
					//如果mysql中存在而redis中不存在的合格数据，则放入到redis中
					if(!haveData) this.redisTemplate.boundHashOps("seckillGoods").put(seckillGoods.getId(), seckillGoods);
				}
			}

		}else{//redis中不存在数据，将mysql合格全部数据放入到redis
			
			//mysql存在数据
			if(seckillGoodsListForMySql!=null && seckillGoodsListForMySql.size()>0){
				for (SeckillGoods seckillGoods : seckillGoodsListForMySql) {
					this.redisTemplate.boundHashOps("seckillGoods").put(seckillGoods.getId(), seckillGoods);
				}
			}
		}

	}	

	/**
	 * 每秒调用删除过期/无库存秒杀数据
	 */
	@Scheduled(cron="* * * * * ?")
	public void removeSeckillGoods(){

		//查询出缓存中的数据，扫描每条记录，判断时间，如果当前时间超过了截止时间，移除此记录
		List<SeckillGoods> seckillGoodsList= redisTemplate.boundHashOps("seckillGoods").values();
		
		for(SeckillGoods seckillGoods :seckillGoodsList){
			if(seckillGoods.getEndTime().getTime() < new Date().getTime() ){
				//同步到数据库
				seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);				
				//清除缓存
				redisTemplate.boundHashOps("seckillGoods").delete(seckillGoods.getId());
			}			
		}		
	}

}
