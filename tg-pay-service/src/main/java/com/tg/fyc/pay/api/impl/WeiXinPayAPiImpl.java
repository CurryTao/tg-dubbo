package com.tg.fyc.pay.api.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.wxpay.sdk.WXPayUtil;
import com.tg.fyc.common.HttpClient;
import com.tg.fyc.pay.api.WeiXinPayAPi;

@Service
public class WeiXinPayAPiImpl implements WeiXinPayAPi {

	@Value("${appid}")
	private String appid;

	@Value("${partner}")
	private String partner;

	@Value("${partnerkey}")
	private String partnerkey;

	@Override
	public Map createNative(String out_trade_no, String total_fee) {

		Map param=new HashMap<>();
		param.put("appid", appid);//公众号
		param.put("mch_id", partner);//商户号
		param.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串		
		param.put("body", "天天购物");//商品描述
		param.put("out_trade_no", out_trade_no);//商户订单号
		param.put("total_fee",total_fee);//总金额（分）
		param.put("spbill_create_ip", "127.0.0.1");//IP
		param.put("notify_url", "http://www.tg.com");//回调地址(随便写)
		param.put("trade_type", "NATIVE");//交易类型

		try {
			String paramXml=WXPayUtil.generateSignedXml(param, partnerkey);

			HttpClient httpClient=new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
			httpClient.setHttps(true);
			httpClient.setXmlParam(paramXml);
			httpClient.post();

			String resultXml = httpClient.getContent();
			Map resultMap = WXPayUtil.xmlToMap(resultXml);

			Map map=new HashMap<>();
			map.put("code_url", resultMap.get("code_url"));//支付地址
			map.put("total_fee", total_fee);//总金额
			map.put("out_trade_no",out_trade_no);//订单号

			return map;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap<>();
	}

	@Override
	public Map queryPayStatus(String out_trade_no) {

		Map param=new HashMap<>();
		param.put("appid", appid);//公众号
		param.put("mch_id", partner);//商户号
		param.put("out_trade_no", out_trade_no);//商户订单号
		param.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串		

		try {
			String paramXml=WXPayUtil.generateSignedXml(param, partnerkey);

			HttpClient httpClient=new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
			httpClient.setHttps(true);
			httpClient.setXmlParam(paramXml);
			httpClient.post();

			String resultXml = httpClient.getContent();
			Map resultMap = WXPayUtil.xmlToMap(resultXml);

			return resultMap;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap<>();
	}

	@Override
	public Map closePay(String out_trade_no) {
		Map param=new HashMap();
		param.put("appid", appid);//公众账号ID
		param.put("mch_id", partner);//商户号
		param.put("out_trade_no", out_trade_no);//订单号
		param.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串
		try {
			String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
			HttpClient client=new HttpClient("https://api.mch.weixin.qq.com/pay/closeorder");
			client.setHttps(true);
			client.setXmlParam(xmlParam);
			client.post();
			String result = client.getContent();
			Map<String, String> map = WXPayUtil.xmlToMap(result);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
}
