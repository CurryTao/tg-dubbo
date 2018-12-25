package com.tg.fyc.content.api;

import java.util.List;

import com.tg.fyc.common.DataGrid;
import com.tg.fyc.common.PageResult;
import com.tg.fyc.pojo.Content;

public interface ContentApi {

	DataGrid findPage(Integer currentPage, Integer pageSize, Content content);

	PageResult insertSelective(Content content) throws Exception;

	PageResult update(Content content) throws Exception;

	PageResult del(String ids) throws Exception;

	public List<Content> findByCategoryId(Long categoryId);
	
	
}
