package com.tg.fyc.sellergoods.api;

import java.util.List;
import java.util.Map;

import com.tg.fyc.common.DataGrid;
import com.tg.fyc.common.PageResult;
import com.tg.fyc.pojo.Brand;
import com.tg.fyc.pojo.Specification;
import com.tg.fyc.pojo.SpecificationVO;
import com.tg.fyc.pojo.TypeTemplate;

public interface TypeTemplateApi {
	List<TypeTemplate> list();


	DataGrid findPage(Integer currentPage, Integer pageSize, TypeTemplate typeTemplate);


	PageResult insertSelective(TypeTemplate typeTemplate) throws Exception;


	PageResult update(TypeTemplate typeTemplate) throws Exception;


	PageResult del(String ids) throws Exception;


	PageResult findOne(Long id);
	
	List<Map> findSpecList(Long id);
	
}
