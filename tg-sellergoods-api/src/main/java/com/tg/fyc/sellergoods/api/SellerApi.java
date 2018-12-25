package com.tg.fyc.sellergoods.api;

import java.util.List;
import java.util.Map;

import com.tg.fyc.common.DataGrid;
import com.tg.fyc.common.PageResult;
import com.tg.fyc.pojo.Brand;
import com.tg.fyc.pojo.Seller;

public interface SellerApi {
	
	PageResult insertSelective(Seller seller) throws Exception;

	Seller findList(String username);

	DataGrid findPage(Integer currentPage, Integer pageSize, Seller seller);

	PageResult update(Seller seller) throws Exception;

	DataGrid findPage1(Integer currentPage, Integer pageSize, Seller seller);

}
