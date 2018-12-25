package com.tg.fyc.sellergoods.api;

import java.util.List;
import java.util.Map;

import com.tg.fyc.common.DataGrid;
import com.tg.fyc.common.PageResult;
import com.tg.fyc.pojo.Brand;
import com.tg.fyc.pojo.ItemCat;

public interface ItemCatApi {

	DataGrid findPage(Integer currentPage, Integer pageSize, ItemCat ItemCat);

	List<Map<String, Object>> list();

	PageResult add(ItemCat itemCat) throws Exception;

	PageResult update(ItemCat itemCat);

	PageResult del(String ids) throws Exception;

	PageResult ByParentId(Long parentId);

	PageResult findOne(Long id);

	List<Map<String, Object>> list2();



}
