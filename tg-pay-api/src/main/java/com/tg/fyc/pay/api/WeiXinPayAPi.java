package com.tg.fyc.pay.api;

import java.util.Map;

public interface WeiXinPayAPi{
	
	public Map createNative(String out_trade_no,String total_fee);
	
	public Map queryPayStatus(String out_trade_no);
	
	public Map closePay(String out_trade_no);
}

