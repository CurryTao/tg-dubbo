package com.tg.fyc.promotion.api;

import java.util.List;

import com.tg.fyc.common.DataGrid;
import com.tg.fyc.common.PageResult;
import com.tg.fyc.pojo.Promotion;

public interface PromotionApi{

	//List<Map<String, String>> list();

	void insertSelective(Promotion promotion);

	DataGrid findPage(Integer currentPage, Integer pageSize, Promotion promotion);
	
	DataGrid findPageForManger(Integer currentPage, Integer pageSize,Promotion Promotion);
	
	List<Promotion> findAll(Long goodsId);
	
	Promotion selectByPrimaryKey(Long id);

	void update(Promotion promotion);

	PageResult del(String ids) throws Exception;
	
	PageResult updatestatus(String[] ids, String status);
	
}
