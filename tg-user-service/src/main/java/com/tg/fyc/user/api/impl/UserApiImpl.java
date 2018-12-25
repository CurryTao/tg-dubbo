package com.tg.fyc.user.api.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tg.fyc.dao.UserMapper;
import com.tg.fyc.pojo.User;
import com.tg.fyc.user.api.UserApi;

@Service
public class UserApiImpl implements UserApi{

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private Destination smsDestination;

	@Value("${templateCode}")
	private String templateCode;

	@Value("${signName}")
	private String signName;

	@Override
	public boolean checkSmsCode(String phone,String smsCode) {
		String systemcode= (String) redisTemplate.boundHashOps("smscode").get(phone);
		if(systemcode==null) return false;
		if(!systemcode.equals(smsCode)) return false;
		return true;
	}

	@Override
	public void register(User user) throws Exception {
		user.setCreated(new Date());
		user.setUpdated(new Date());
		user.setSourceType("1");		
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));
		userMapper.insertSelective(user);	
	}

	@Override
	public void createSmsCode(final String phone) {
		
		final String smscode=(long)(Math.random()*1000000)+"";

		redisTemplate.boundHashOps("smscode").put(phone, smscode);

		jmsTemplate.send(smsDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage message = session.createMapMessage();
				message.setString("phoneNumber", phone);
				message.setString("templateCode", templateCode);
				message.setString("signName", signName);
				Map map=new HashMap();
				map.put("code", smscode);			
				message.setString("templateParam", JSON.toJSONString(map));
				return message;
			}
		});
	}

}
