package com.tg.fyc.cart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tg.fyc.pojo.Address;
import com.tg.fyc.user.api.AddressApi;

@RestController
@RequestMapping("address")
public class AddressController {

	@Autowired
	private AddressApi addressApi;
	
	@RequestMapping("findListByLoginUser")
	public List<Address> findListByLoginUser(){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		List<Address> findListByLoginUser = this.addressApi.findListByLoginUser(username);
		return findListByLoginUser;
	}
	
}
