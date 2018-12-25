package com.tg.fyc.user.api;

import com.tg.fyc.pojo.User;

public interface UserApi{

	boolean checkSmsCode(String phone, String smscode);

	void register(User user) throws Exception;

	void createSmsCode(String phone);
    
}
