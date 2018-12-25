package com.tg.fyc.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tg.fyc.common.PageResult;
import com.tg.fyc.common.PhoneFormatCheckUtils;
import com.tg.fyc.pojo.User;
import com.tg.fyc.user.api.UserApi;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserApi userApi;
	
	@RequestMapping("/register")
	public PageResult register(@RequestBody User user,String smscode){
		if(!userApi.checkSmsCode(user.getPhone(),smscode)) return PageResult.error(500,"验证码不正确");
		try {
			userApi.register(user);
			return PageResult.success(null);
		} catch (Exception e) {
			e.printStackTrace();
			return PageResult.error(500, "注册失败");
		}
	}
	
	@RequestMapping("/sendCode")
	public PageResult register(String phone){
		if(!PhoneFormatCheckUtils.isPhoneLegal(phone)){
			return PageResult.error(500, "手机号不存在");
		}
		try {
			userApi.createSmsCode(phone);
			return PageResult.success(null);
		} catch (Exception e) {
			e.printStackTrace();
			return PageResult.error(500, "信息发送失败");
		}
	}
	
	@RequestMapping("/showName")
	public Map showName(String phone){
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		Map map=new HashMap();
		map.put("loginName", name);
		return map;
	}
}
