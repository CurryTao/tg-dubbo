package com.tg.fyc.search.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tg.fyc.search.api.ItemSearchApi;

@Component
public class ItemDeleteListener implements MessageListener {

	@Autowired
	private ItemSearchApi itemSearchApi;
	
	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage=(ObjectMessage) message;
		
		try {
			String  string=(String) objectMessage.getObject();
			System.out.println("监听到删除="+string);
			itemSearchApi.deleteList(string);
			System.out.println("删除索引");
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
