package com.tg.fyc.search.service;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.tg.fyc.pojo.Item;
import com.tg.fyc.search.api.ItemSearchApi;
@Component
public class ItemSearchListener implements MessageListener {

	@Autowired
	private ItemSearchApi itemSearchApi;
	
	
	@Override
	public void onMessage(Message message) {
		TextMessage textMessage=(TextMessage) message;
		try {
			String text = textMessage.getText();
			System.out.println("输出信息="+text);
			List<Item> list = JSON.parseArray(text, Item.class);
			
			itemSearchApi.importList(list);
			
			System.out.println("导入到solr");
			
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

}
