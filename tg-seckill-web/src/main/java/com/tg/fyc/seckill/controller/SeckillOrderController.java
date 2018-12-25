package com.tg.fyc.seckill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tg.fyc.common.PageResult;
import com.tg.fyc.seckill.api.OrderApi;

@RestController
@RequestMapping("order")
public class SeckillOrderController {

	@Autowired
	private OrderApi orderApi;
	
	@RequestMapping("/submitOrder")
	public PageResult submitOrder(Long seckillId){
			String userId = SecurityContextHolder.getContext().getAuthentication().getName();
			if(userId.equals("anonymousUser")) return PageResult.error(500, "哎呀，手滑了");
			try {
				orderApi.submitOrder(seckillId, userId);
				return PageResult.success(null);
			}catch (RuntimeException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return PageResult.error(500, "哎呀，手滑了");
	}

}
