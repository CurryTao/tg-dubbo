package com.tg.fyc.sellergoods.api;

import java.util.List;
import java.util.Map;

import com.tg.fyc.common.DataGrid;
import com.tg.fyc.common.PageResult;
import com.tg.fyc.pojo.Brand;
import com.tg.fyc.pojo.Specification;
import com.tg.fyc.pojo.SpecificationVO;

public interface SpecificationApi {
	List<Map<String, String>> list();


	DataGrid findPage(Integer currentPage, Integer pageSize, Specification specification);

	PageResult insertSelective(SpecificationVO specificationVO) throws Exception;

	DataGrid findgui(Long id);

	PageResult update(SpecificationVO specificationVO) throws Exception;

	PageResult del(String ids) throws Exception;

}
