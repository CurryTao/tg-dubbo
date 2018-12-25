package com.tg.fyc.manager.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tg.fyc.common.PageResult;
@Controller
@RequestMapping("indexlogin")
public class IndexLogin {
	
	@RequestMapping("showname")
	@ResponseBody
	public PageResult showname() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		return PageResult.success(name);
	}
		
	
}
