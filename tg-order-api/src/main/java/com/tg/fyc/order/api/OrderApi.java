package com.tg.fyc.order.api;

import com.tg.fyc.pojo.Order;
import com.tg.fyc.pojo.PayLog;

public interface OrderApi{
   
	public void addToOrder(Order order);
	
	public PayLog searchPayLogFromRedis(String userId);
	
	public void updateOrderStatus(String out_trade_no,String transaction_id);
}
