package com.tg.fyc.sellergoods.api;

import java.util.List;
import java.util.Map;

import com.tg.fyc.common.DataGrid;
import com.tg.fyc.common.PageResult;
import com.tg.fyc.pojo.Brand;

public interface BrandApi {
	
	List<Map<String, String>> list();

	void insertSelective(Brand brand);

	DataGrid findPage(Integer currentPage, Integer pageSize, Brand brand);

	Brand selectByPrimaryKey(Long id);

	void update(Brand brand);

	PageResult del(String ids) throws Exception;

}
