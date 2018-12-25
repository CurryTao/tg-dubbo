package com.tg.fyc.page.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tg.fyc.page.api.ItemPageApi;

@Component
public class PageListener implements MessageListener {
	
	@Autowired
	private ItemPageApi itemPageApi;
	
	@Override
	public void onMessage(Message message) {
		TextMessage textMessage=(TextMessage)message;
		try {
			String goodsId = textMessage.getText();
			itemPageApi.genItemHtml(Long.valueOf(goodsId));
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
