package com.tg.fyc.cart.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tg.fyc.common.PageResult;
import com.tg.fyc.order.api.OrderApi;
import com.tg.fyc.pay.api.WeiXinPayAPi;
import com.tg.fyc.pojo.PayLog;

@RestController
@RequestMapping("pay")
public class PayController {

	@Autowired
	private WeiXinPayAPi weiXinPayAPi;
	
	@Autowired
	private OrderApi orderApi;
	
	@RequestMapping("createNative")
	public Map createNative(){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		PayLog payLog = this.orderApi.searchPayLogFromRedis(username);
		if(payLog!=null){
			return weiXinPayAPi.createNative(payLog.getOutTradeNo(),payLog.getTotalFee()+"");		
		}else{
			return new HashMap<>();
		}
	}
	
	@RequestMapping("queryPayStatus")
	public PageResult queryPayStatus(String out_trade_no){
		PageResult result=null;	
		int x=0;
		while(true){
			Map<String,String> map = weiXinPayAPi.queryPayStatus(out_trade_no);
			if(map==null){//出错			
				result=PageResult.error(500, "验证码生成失败");
				break;
			}			
			if(map.get("trade_state").equals("SUCCESS")){//如果成功				
				result=PageResult.success(null);
				this.orderApi.updateOrderStatus(out_trade_no, map.get("transaction_id"));
				break;
			}			
			try {
				Thread.sleep(3000);//间隔三秒
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
			x++;
			if(x==99){
				result=PageResult.error(500, "二维码超时");
				break;
			}
		}
		return result;
	}
	
}
