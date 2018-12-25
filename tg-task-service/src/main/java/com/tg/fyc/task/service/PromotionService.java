package com.tg.fyc.task.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tg.fyc.dao.PromotionMapper;
import com.tg.fyc.pojo.Promotion;

@Component
public class PromotionService {

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private PromotionMapper promotionMapper;

	/**
	 * 每分钟更新促销信息
	 */
	@Scheduled(cron="0 * * * * ?")
	public void refreshPromotion(){

		List<Long> promotinGoodsIdsListForRedis = new ArrayList<>(redisTemplate.boundHashOps("promotion").keys());

		List<Promotion> PromoionListForMySql = promotionMapper.findListByGoodsId(null);

		//redis中不存在数据
		if(promotinGoodsIdsListForRedis!=null && promotinGoodsIdsListForRedis.size()>0){

			//mySql中存在数据
			if(PromoionListForMySql!=null && PromoionListForMySql.size()>0){

				//筛选出redis中没有的合格数据放入到redis
				for (Promotion promotion : PromoionListForMySql) {
					//循环的该数据存在状态
					boolean haveData=false;
					//遍历redis中的数据，查询是否在redis中已经存在
					for(Long seckillIds:promotinGoodsIdsListForRedis){
						//数据存在，更改数据状态值，结束循环
						if(promotion.getGoodsIds().equals(seckillIds.toString())) {
							haveData=true;
							break;
						}
					}
					//如果mysql中存在而redis中不存在的合格数据，则放入到redis中
					if(!haveData) this.redisTemplate.boundHashOps("promotion").put(promotion.getGoodsIds(), promotion);
				}
			}

		}else{//redis中不存在数据，将mysql合格全部数据放入到redis
			//mysql存在数据
			if(PromoionListForMySql!=null && PromoionListForMySql.size()>0){
				for (Promotion promotion : PromoionListForMySql) {
					this.redisTemplate.boundHashOps("promotion").put(promotion.getGoodsIds(), promotion);
				}
			}
		}

	}	

	/**
	 * 每秒调用删除过期促销信息
	 */
	@Scheduled(cron="* * * * * ?")
	public void removePromotion(){

		//查询出缓存中的数据，扫描每条记录，判断时间，如果当前时间超过了截止时间，移除此记录
		List<Promotion> promotionList = redisTemplate.boundHashOps("promotion").values();

		if(promotionList!=null && promotionList.size()>0){
			for (Promotion promotion : promotionList) {
				if(promotion.getEndtime().getTime() < new Date().getTime() ){
					//清除缓存
					redisTemplate.boundHashOps("promotion").delete(promotion.getGoodsIds());
				}			
			}
		}

	}

}
