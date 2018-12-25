package com.tg.fyc.seckill.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tg.fyc.common.PageResult;
import com.tg.fyc.pay.api.WeiXinPayAPi;
import com.tg.fyc.pojo.SeckillOrder;
import com.tg.fyc.seckill.api.OrderApi;

@RestController
@RequestMapping("pay")
public class PayController {

	@Autowired
	private OrderApi orderApi;
	
	@Autowired
	private WeiXinPayAPi weiXinPayAPi;
	
	@RequestMapping("/createNative")
	public Map createNative(){
		//获取当前用户		
		String userId=SecurityContextHolder.getContext().getAuthentication().getName();
		//到redis查询秒杀订单
		SeckillOrder seckillOrder = orderApi.searchOrderFromRedisByUserId(userId);
		//判断秒杀订单存在
		if(seckillOrder!=null){
			long fen=  (long)(seckillOrder.getMoney().doubleValue()*100);//金额（分）
			return weiXinPayAPi.createNative(seckillOrder.getId()+"",+fen+"");
		}else{
			return new HashMap();
		}		
	}

	@RequestMapping("/queryPayStatus")
	public PageResult queryPayStatus(String out_trade_no){
		//获取当前用户	
		String userId=SecurityContextHolder.getContext().getAuthentication().getName();
		PageResult result=null;
		int x=0;
		while(true){
			//调用查询接口
			Map<String,String> map = weiXinPayAPi.queryPayStatus(out_trade_no);
			if(map==null){//出错	
				result=PageResult.error(500, "支付出错");
				break;
			}			
			if(map.get("trade_state").equals("SUCCESS")){//如果成功				
				result=PageResult.success(null);
				//支付成功保存订单
				orderApi.saveOrderFromRedisToDb(userId, Long.valueOf(out_trade_no), map.get("transaction_id"));
				break;
			}			
			try {
				Thread.sleep(3000);//间隔三秒
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
			x++;//设置超时时间为5分钟
			if(x>20){
				result=PageResult.error(500, "二维码超时");//超时自动退单
				
				Map<String,String> payresult = weiXinPayAPi.closePay(out_trade_no);	
				
				if(!"SUCCESS".equals(payresult.get("result_code")) ){//如果返回结果是非正常关闭
					if("ORDERPAID".equals(payresult.get("err_code"))){
						result=PageResult.success(null);
						orderApi.saveOrderFromRedisToDb(userId, Long.valueOf(out_trade_no), map.get("transaction_id"));
					}					
				}				
				if(result.getStatus()==false){
					System.out.println("超时，取消订单");
					//2.调用删除
					orderApi.deleteOrderFromRedis(userId, Long.valueOf(out_trade_no));	
				}				
				break;
			}			
		}
		return result;
	}

	
}
