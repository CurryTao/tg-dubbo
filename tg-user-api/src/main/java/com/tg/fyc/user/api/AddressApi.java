package com.tg.fyc.user.api;

import java.util.List;

import com.tg.fyc.pojo.Address;

public interface AddressApi {

	List<Address> findListByLoginUser(String username);

}
