package com.tg.fyc.content.api;

import java.util.List;
import java.util.Map;

import com.tg.fyc.common.DataGrid;
import com.tg.fyc.common.PageResult;
import com.tg.fyc.pojo.ContentCategory;

public interface ContentCategoryApi {

	DataGrid findPage(Integer currentPage, Integer pageSize, ContentCategory contentCategory);

	List<Map<String, Object>> list();

	PageResult insertSelective(ContentCategory contentCategory) throws Exception;

	PageResult update(ContentCategory contentCategory) throws Exception;

	PageResult del(String ids) throws Exception;

}
