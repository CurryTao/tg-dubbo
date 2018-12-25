package com.tg.fyc.page.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tg.fyc.page.api.ItemPageApi;

@Component
public class PageDeleteListener implements MessageListener {
	
	@Autowired
	private ItemPageApi itemPageApi;
	
	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage=(ObjectMessage) message;
		try {
			
			String goodsId = (String) objectMessage.getObject();

			itemPageApi.deleteItemHtml(goodsId);
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
