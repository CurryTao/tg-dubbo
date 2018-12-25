package com.tg.fyc.cart.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.tg.fyc.cart.api.CartApi;
import com.tg.fyc.pojo.Item;
import com.tg.fyc.pojo.OrderItem;
import com.tg.fyc.pojo.Promotion;
import com.tg.fyc.pojo.VO.Cart;

@Service
public class CartApiImpl implements CartApi{
	
	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {
		
		//Item item = itemMapper.selectByPrimaryKey(itemId);
		Item item=(Item) redisTemplate.boundHashOps("item").get(itemId);

		if(item==null) throw new RuntimeException("商品不存在");

		if(!item.getStatus().equals("1")) throw new RuntimeException("商品已过期");

		//2.获取商家ID		
		String sellerId = item.getSellerId();
		
		//3.根据商家ID判断购物车列表中是否存在该商家的购物车	
		Cart cart = searchCartBySellerId(cartList,sellerId);

		//4.如果购物车列表中不存在该商家的购物车
		if(cart == null){		
			//4.1 新建购物车对象 ，
			cart=new Cart();
			cart.setSellerId(sellerId);
			cart.setSellerName(item.getSeller());						
			OrderItem orderItem = createOrderItem(item,num);
			List orderItemList=new ArrayList();
			orderItemList.add(orderItem);
			cart.setOrderItemList(orderItemList);
			
			//将促销信息放入到购物车
			List<Promotion> promotionList = (List<Promotion>) redisTemplate.boundHashOps("promotion").get(orderItem.getGoodsId());
			
			if(promotionList!=null && promotionList.size()>0) {
				cart.setPromotionList(promotionList);
			}
			
			//4.2将购物车对象添加到购物车列表
			cartList.add(cart);
		}else{
			//5.如果购物车列表中存在该商家的购物车	
			// 判断购物车明细列表中是否存在该商品
			OrderItem orderItem = searchOrderItemByItemId(cart.getOrderItemList(),itemId);

			if(orderItem==null){
				//5.1. 如果没有，新增购物车明细				
				orderItem=createOrderItem(item,num);
				cart.getOrderItemList().add(orderItem);
				
				//将促销放入到购物车
				List<Promotion> promotionListForRedis = (List<Promotion>) redisTemplate.boundHashOps("promotion").get(orderItem.getGoodsId());
				if(promotionListForRedis!=null && promotionListForRedis.size()>0) {
					List<Promotion> promotionList = cart.getPromotionList();
					if(promotionList==null || promotionList.size()==0){
						promotionList=promotionListForRedis;
					}else{
						for (Promotion promotion : promotionList) {
							if(promotion.getGoodsIds()!=promotionListForRedis.get(0).getGoodsIds())
								promotionList.addAll(promotionListForRedis);
						}
					}
					cart.setPromotionList(promotionList);
				}
				
			}else{
				//5.2. 如果有，在原购物车明细上添加数量，更改金额
				orderItem.setNum(orderItem.getNum()+num);			
				orderItem.setTotalFee(new BigDecimal(orderItem.getNum()*orderItem.getPrice().doubleValue())  );
				//如果数量操作后小于等于0，则移除
				if(orderItem.getNum()<=0){
					cart.getOrderItemList().remove(orderItem);//移除购物车明细
					
					//移除促销
					List<Promotion> promotionList = cart.getPromotionList();
					for (Promotion promotion : promotionList) {
						if(promotion.getGoodsIds().equals(orderItem.getGoodsId())){
							promotionList.remove(promotion);
						}
					}
					cart.setPromotionList(promotionList);
					
				}
				//如果移除后cart的明细数量为0，则将cart移除
				if(cart.getOrderItemList().size()==0){
					cartList.remove(cart);
				}
			}			
		}			
		
		return cartList;
	}
	
	private Cart searchCartBySellerId(List<Cart> cartList, String sellerId){
		for(Cart cart:cartList){
			if(cart.getSellerId().equals(sellerId)){
				return cart;
			}		
		}
		return null;
	}

	private OrderItem searchOrderItemByItemId(List<OrderItem> orderItemList ,Long itemId ){
		for(OrderItem orderItem :orderItemList){
			if(orderItem.getItemId().longValue()==itemId.longValue()){
				return orderItem;				
			}			
		}
		return null;
	}

	private OrderItem createOrderItem(Item item,Integer num){
		if(num<=0){
			throw new RuntimeException("数量非法");
		}
		OrderItem orderItem=new OrderItem();
		orderItem.setGoodsId(item.getGoodsId());
		orderItem.setItemId(item.getId());
		orderItem.setNum(num);
		orderItem.setPicPath(item.getImage());
		orderItem.setPrice(item.getPrice());
		orderItem.setSellerId(item.getSellerId());
		orderItem.setTitle(item.getTitle());
		orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue()*num));
		return orderItem;
	}

	@Override
	public void savaCartListToRedis(List<Cart> cartList, String username) {
		redisTemplate.boundHashOps("cartList").put(username, cartList);
	}

	@Override
	public List<Cart> findCartListFromRedis(String username) {
		List<Cart> cartList=(List<Cart>) redisTemplate.boundHashOps("cartList").get(username);
		if(cartList==null) cartList=new ArrayList<>();
		return cartList;
	}
	
	@Override
	public List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2) {//合并购物车
		for (Cart cart : cartList2) {
			for (OrderItem orderItem : cart.getOrderItemList()) {
				this.addGoodsToCartList(cartList1, orderItem.getItemId(), orderItem.getNum());
			}
		}
		return cartList1;
	}
}
