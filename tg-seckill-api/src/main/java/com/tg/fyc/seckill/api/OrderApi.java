package com.tg.fyc.seckill.api;

import com.tg.fyc.pojo.SeckillOrder;

public interface OrderApi{
	
	public void submitOrder(Long seckillId,String userId);
	
	public SeckillOrder searchOrderFromRedisByUserId(String userId);
	
	public void saveOrderFromRedisToDb(String userId,Long orderId,String transactionId);
	
	public void deleteOrderFromRedis(String userId,Long orderId);
	
}
