package com.tg.fyc.seckill.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.tg.fyc.common.IdWorker;
import com.tg.fyc.dao.SeckillGoodsMapper;
import com.tg.fyc.dao.SeckillOrderMapper;
import com.tg.fyc.pojo.SeckillGoods;
import com.tg.fyc.pojo.SeckillOrder;
import com.tg.fyc.seckill.api.OrderApi;

@Service
public class OrderApiImpl implements OrderApi{

	@Autowired
	private SeckillGoodsMapper seckillGoodsMapper;

	@Autowired
	private SeckillOrderMapper seckillOrderMapper;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private IdWorker idWorker;

	@Override
	public void submitOrder(Long seckillId, String userId) {

		//从缓存中查询秒杀商品		
		SeckillGoods seckillGoods =(SeckillGoods) redisTemplate.boundHashOps("seckillGoods").get(seckillId);
		if(seckillGoods==null){
			throw new RuntimeException("商品不存在");
		}
		if(seckillGoods.getStockCount()<=0){
			throw new RuntimeException("商品已抢购一空");
		}	
		//扣减（redis）库存		
		seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
		redisTemplate.boundHashOps("seckillGoods").put(seckillId, seckillGoods);//放回缓存
		if(seckillGoods.getStockCount()==0){//如果已经被秒光
			seckillGoodsMapper.updateByPrimaryKey(seckillGoods);//同步到数据库	
			redisTemplate.boundHashOps("seckillGoods").delete(seckillId);		
		}
		//保存（redis）订单
		long orderId = idWorker.nextId();
		SeckillOrder seckillOrder=new SeckillOrder();
		seckillOrder.setId(orderId);
		seckillOrder.setCreateTime(new Date());
		seckillOrder.setMoney(seckillGoods.getCostPrice());//秒杀价格
		seckillOrder.setSeckillId(seckillId);
		seckillOrder.setSellerId(seckillGoods.getSellerId());
		seckillOrder.setUserId(userId);//设置用户ID
		seckillOrder.setStatus("0");//状态
		redisTemplate.boundHashOps("seckillOrder").put(userId, seckillOrder);
	}

	@Override
	public SeckillOrder searchOrderFromRedisByUserId(String userId) {
		return (SeckillOrder) redisTemplate.boundHashOps("seckillOrder").get(userId);
	}

	@Override
	public void saveOrderFromRedisToDb(String userId, Long orderId, String transactionId) {
		//根据用户ID查询日志
		SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps("seckillOrder").get(userId);
		if(seckillOrder==null){
			throw new RuntimeException("订单不存在");
		}
		//如果与传递过来的订单号不符
		if(seckillOrder.getId().longValue()!=orderId.longValue()){
			throw new RuntimeException("订单不相符");
		}		
		seckillOrder.setTransactionId(transactionId);//交易流水号
		seckillOrder.setPayTime(new Date());//支付时间
		seckillOrder.setStatus("1");//状态
		seckillOrderMapper.insertSelective(seckillOrder);//保存到数据库
		redisTemplate.boundHashOps("seckillOrder").delete(userId);//从redis中清除
	}

	@Override
	public void deleteOrderFromRedis(String userId, Long orderId) {
		//根据用户ID查询日志
		SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps("seckillOrder").get(userId);
		if(seckillOrder!=null && seckillOrder.getId().longValue()== orderId.longValue() ){
			redisTemplate.boundHashOps("seckillOrder").delete(userId);//删除缓存中的订单
			//恢复库存
			//1.从缓存中提取秒杀商品	
			SeckillGoods seckillGoods=(SeckillGoods)redisTemplate.boundHashOps("seckillGoods").get(seckillOrder.getSeckillId());
			if(seckillGoods!=null){
				seckillGoods.setStockCount(seckillGoods.getStockCount()+1);	
				redisTemplate.boundHashOps("seckillGoods").put(seckillOrder.getSeckillId(), seckillGoods);//存入缓存
			}else{//商品抢购完
				SeckillGoods selectByPrimaryKey = this.seckillGoodsMapper.selectByPrimaryKey(orderId);
				seckillGoods.setStockCount(1);
				redisTemplate.boundHashOps("seckillGoods").put(seckillOrder.getSeckillId(), seckillGoods);//存入缓存
			}
		}	
	}
}
