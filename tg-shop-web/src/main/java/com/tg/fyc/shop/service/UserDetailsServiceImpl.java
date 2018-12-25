package com.tg.fyc.shop.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tg.fyc.common.Constant;
import com.tg.fyc.pojo.Seller;
import com.tg.fyc.sellergoods.api.SellerApi;

public class UserDetailsServiceImpl implements UserDetailsService {
	
	SellerApi sellerApi;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Seller seller=sellerApi.findList(username);
		if (seller!=null) {
			if (Constant.SELLER_STATUS_PASS.equals(seller.getStatus())) {
				List<GrantedAuthority> grantedAuths=new ArrayList<GrantedAuthority>();
				grantedAuths.add(new SimpleGrantedAuthority("ROLE_SELLER"));
				return new User(username,seller.getPassword(),grantedAuths); 
			}
			return null;
		} 
		return null;
	}

	public void setSellerApi(SellerApi sellerApi) {
		this.sellerApi = sellerApi;
	}

}
