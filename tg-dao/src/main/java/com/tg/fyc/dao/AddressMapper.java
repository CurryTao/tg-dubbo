package com.tg.fyc.dao;

import java.util.List;

import com.tg.fyc.pojo.Address;

public interface AddressMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Address record);

    int insertSelective(Address record);

    Address selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Address record);

    int updateByPrimaryKey(Address record);

	List<Address> findListByLoginUser(String username);
}