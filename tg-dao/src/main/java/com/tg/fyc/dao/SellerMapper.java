package com.tg.fyc.dao;

import java.util.List;

import com.tg.fyc.pojo.Brand;
import com.tg.fyc.pojo.Seller;

public interface SellerMapper {
    int deleteByPrimaryKey(String sellerId);

    int insert(Seller record);

    int insertSelective(Seller record);

    Seller selectByPrimaryKey(String sellerId);

    int updateByPrimaryKeySelective(Seller record);

    int updateByPrimaryKey(Seller record);

	List<Seller> findlist(Seller seller);

	List<Seller> findlist1(Seller seller);
}