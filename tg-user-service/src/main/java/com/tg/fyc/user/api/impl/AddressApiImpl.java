package com.tg.fyc.user.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tg.fyc.dao.AddressMapper;
import com.tg.fyc.pojo.Address;
import com.tg.fyc.user.api.AddressApi;

@Service
public class AddressApiImpl implements AddressApi{

	@Autowired
	private AddressMapper addressMapper;
	
	@Override
	public List<Address> findListByLoginUser(String username) {
		return this.addressMapper.findListByLoginUser(username);
	}
	
}
