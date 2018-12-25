package com.tg.fyc.task.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tg.fyc.dao.ItemMapper;
import com.tg.fyc.pojo.Item;

@Component
public class GoodsService {

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private ItemMapper itemMapper;

	/**
	 * 每分钟更新商品数据
	 */
	@Scheduled(cron="0 * * * * ?")
	public void refreshSeckillGoods(){

		//查询缓存中的秒杀商品ID集合
		List<Long> goodsIdListForRedis =  new ArrayList(redisTemplate.boundHashOps("item").keys());

		//查询mysql中合格数据
		List<Item> goodsListForMySql = this.itemMapper.findAll();

		//redis中存在数据
		if(goodsIdListForRedis!=null && goodsIdListForRedis.size()>0){

			//mySql中存在数据
			if(goodsListForMySql!=null && goodsListForMySql.size()>0){

				//筛选出redis中没有的合格数据放入到redis
				for (Item item : goodsListForMySql) {
					//循环的该数据存在状态
					boolean haveData=false;
					//遍历redis中的数据，查询是否在redis中已经存在
					for(Long seckillIds:goodsIdListForRedis){
						//数据存在，更改数据状态值，结束循环
						if(item.getId() == seckillIds) {
							haveData=true;
							break;
						}
					}
					//如果mysql中存在而redis中不存在的合格数据，则放入到redis中
					if(!haveData) this.redisTemplate.boundHashOps("item").put(item.getId(), item);
				}
			}
		}else{//redis中不存在数据，将mysql合格全部数据放入到redis
			//mysql存在数据
			if(goodsListForMySql!=null && goodsListForMySql.size()>0){
				for (Item item : goodsListForMySql) {
					this.redisTemplate.boundHashOps("item").put(item.getId(), item);
				}
			}
		}
	}	

	/**
	 * 每秒调用删除过期/无库存商品
	 */
	@Scheduled(cron="* * * * * ?")
	public void removeSeckillGoods(){

		List<Item> goodsList= redisTemplate.boundHashOps("item").values();

		for(Item item :goodsList){
			if(item.getNum()==0){
				//同步到数据库
				itemMapper.updateByPrimaryKeySelective(item);			
				//清除缓存
				redisTemplate.boundHashOps("item").delete(item.getId());
			}			
		}		
	}

}
