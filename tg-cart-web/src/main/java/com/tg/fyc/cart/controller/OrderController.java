package com.tg.fyc.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tg.fyc.common.PageResult;
import com.tg.fyc.order.api.OrderApi;
import com.tg.fyc.pojo.Order;

@RestController
@RequestMapping("order")
public class OrderController {

	@Autowired
	private OrderApi orderApi;
	
	@RequestMapping("toAddOrder")
	public PageResult toAddOrder(@RequestBody Order order){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		order.setUserId(username);
		order.setSourceType("2");
		try {
			this.orderApi.addToOrder(order);
			return PageResult.success(null);
		} catch (Exception e) {
			e.printStackTrace();
			return PageResult.error(500, "添加订单失败");
		}
	}
	
}
