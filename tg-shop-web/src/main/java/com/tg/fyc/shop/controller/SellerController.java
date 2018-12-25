package com.tg.fyc.shop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tg.fyc.common.GloablErrorMessageEnum;
import com.tg.fyc.common.PageResult;
import com.tg.fyc.pojo.Seller;
import com.tg.fyc.sellergoods.api.BrandApi;
import com.tg.fyc.sellergoods.api.SellerApi;
@Controller
@RequestMapping("seller")
public class SellerController {
	
	@Autowired
	private SellerApi sellerApi;
	private Logger log=LoggerFactory.getLogger(SellerController.class);
	
	@RequestMapping(value="zhuce",method=RequestMethod.POST)
	public String zhuce(Seller seller) {
		PageResult pageResult=null;
		try {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String password = passwordEncoder.encode(seller.getPassword());
			seller.setPassword(password);
			
			pageResult=sellerApi.insertSelective(seller);
		} catch (Exception e) {
			log.error(GloablErrorMessageEnum.ERROR_SELLER_SAVA.getMessage(),e,e);
			return "redirect:../error.html";
		}
		return "redirect:../shoplogin.html";
		
	}
	@RequestMapping("updatePwd")
	public String updatePwd(String oldPwd,String newPwd,String confirmPwd) {
		UserDetails details =(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String password = details.getPassword();
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		boolean matches = passwordEncoder.matches(oldPwd, password);
		if (!passwordEncoder.matches(oldPwd, password)) {
			return "redirect:http://localhost:8082/admin/password.html";
		}
		if (!newPwd.equals(confirmPwd)) {
			return "redirect:http://localhost:8082/admin/password.html";
		}
		Seller seller = new Seller();
		String newPwd1 = passwordEncoder.encode(newPwd);
		seller.setSellerId(details.getUsername());
		seller.setPassword(newPwd1);
		try {
			PageResult pageResult=sellerApi.update(seller);
			//清除登陆的对象
			SecurityContextHolder.clearContext();
		} catch (Exception e) {
			return "redirect:http://localhost:8082/admin/password.html";
		}
		
		return "redirect:http://localhost:8082/shoplogin.html";
	}
	
	
	
	
}
