package com.tg.fyc.manager.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tg.fyc.common.DataGrid;
import com.tg.fyc.common.GloablErrorMessageEnum;
import com.tg.fyc.common.PageResult;
import com.tg.fyc.content.api.ContentCategoryApi;
import com.tg.fyc.pojo.ContentCategory;

@Controller
@RequestMapping("contentCategory")
public class ContentCategoryController {
	@Autowired
	private ContentCategoryApi contentCategoryApi;
	private Logger log=LoggerFactory.getLogger(TypeTemplateController.class);
	@RequestMapping("findPage")
	@ResponseBody
	public DataGrid findPage(Integer currentPage,Integer pageSize,@RequestBody ContentCategory contentCategory) {
		
		return contentCategoryApi.findPage(currentPage,pageSize,contentCategory);
	}
	
	@RequestMapping("list1")
	@ResponseBody
	public List<Map<String, Object>> list() {
		
		
		return contentCategoryApi.list();
	}
	
	@RequestMapping("add")
	@ResponseBody
	public PageResult add(@RequestBody ContentCategory contentCategory) {
		PageResult pageResult=null;
		try {
			pageResult=contentCategoryApi.insertSelective(contentCategory);
			
		} catch (Exception e) {
			//log.error(GloablErrorMessageEnum.ERROR_GUIGE_SAVA.getMessage(),e,e);
			return pageResult.error(GloablErrorMessageEnum.ERROR_GUIGE_SAVA.getCode(), GloablErrorMessageEnum.ERROR_GUIGE_SAVA.getMessage());
		}
		return pageResult.success(null);
		
	}
	
	@RequestMapping("update")
	@ResponseBody
	public PageResult update(@RequestBody ContentCategory contentCategory) {
		PageResult pageResult=null;
		try {
			pageResult=contentCategoryApi.update(contentCategory);
			
		} catch (Exception e) {
			log.error(GloablErrorMessageEnum.ERROR_MOBAN_UPDATE.getMessage(),e,e);
			return pageResult.error(GloablErrorMessageEnum.ERROR_MOBAN_UPDATE.getCode(), GloablErrorMessageEnum.ERROR_MOBAN_UPDATE.getMessage());
		}
		return pageResult.success(null);
	}
	@RequestMapping("del")
	@ResponseBody
	public PageResult del(String ids) {
		PageResult pageResult=null;
		try {
			pageResult=contentCategoryApi.del(ids);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(GloablErrorMessageEnum.ERROR_PUSH_REDIS.getMessage(),e,e);
			return PageResult.error(GloablErrorMessageEnum.ERROR_PUSH_REDIS.getCode(), GloablErrorMessageEnum.ERROR_PUSH_REDIS.getMessage());
		}
		return pageResult.success(null);
	}
	
	
}
