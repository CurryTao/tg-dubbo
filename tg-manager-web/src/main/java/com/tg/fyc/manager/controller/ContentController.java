package com.tg.fyc.manager.controller;

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
import com.tg.fyc.content.api.ContentApi;
import com.tg.fyc.pojo.Content;

@Controller
@RequestMapping("content")
public class ContentController {
	@Autowired
	private ContentApi contentApi;
	
	private Logger log=LoggerFactory.getLogger(TypeTemplateController.class);
	
	@RequestMapping("findPage")
	@ResponseBody
	public DataGrid findPage(Integer currentPage,Integer pageSize,@RequestBody Content content) {
		
		return contentApi.findPage(currentPage,pageSize,content);
	}
	
	@RequestMapping("add")
	@ResponseBody
	public PageResult add(@RequestBody Content content) {
		PageResult pageResult=null;
		try {
			pageResult=contentApi.insertSelective(content);
			
		} catch (Exception e) {
			//log.error(GloablErrorMessageEnum.ERROR_GUIGE_SAVA.getMessage(),e,e);
			return pageResult.error(GloablErrorMessageEnum.ERROR_GUIGE_SAVA.getCode(), GloablErrorMessageEnum.ERROR_GUIGE_SAVA.getMessage());
		}
		return pageResult.success(null);
		
	}
	
	@RequestMapping("update")
	@ResponseBody
	public PageResult update(@RequestBody Content content) {
		PageResult pageResult=null;
		try {
			pageResult=contentApi.update(content);
			
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
			pageResult=contentApi.del(ids);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(GloablErrorMessageEnum.ERROR_PUSH_REDIS.getMessage(),e,e);
			return PageResult.error(GloablErrorMessageEnum.ERROR_PUSH_REDIS.getCode(), GloablErrorMessageEnum.ERROR_PUSH_REDIS.getMessage());
		}
		return pageResult.success(null);
	}
	
}
