package com.tg.fyc.sellergoods.api;

import java.util.List;

import com.tg.fyc.common.DataGrid;
import com.tg.fyc.common.PageResult;
import com.tg.fyc.pojo.Goods;
import com.tg.fyc.pojo.GoodsDeitVO;
import com.tg.fyc.pojo.Item;

public interface GoodsEditApi {

	PageResult add(GoodsDeitVO goodsDeitVO) throws Exception;

	DataGrid findPage(Integer currentPage, Integer pageSize, Goods goods);

	GoodsDeitVO findOne(Long id);

	PageResult update(GoodsDeitVO goodsDeitVO) throws Exception;

	PageResult pltijiao(String ids);

	DataGrid findPage2(Integer currentPage, Integer pageSize, Goods goods);

	PageResult updatestatus(String ids, String status);

	PageResult del(String ids);
	
	public List<Item> findItemListByGoodsIdListAndStatus(String goodsIds,String status);

	List<Goods> findAll(Goods goods);

}
