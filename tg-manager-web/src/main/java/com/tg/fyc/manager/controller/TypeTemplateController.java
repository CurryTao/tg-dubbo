package com.tg.fyc.manager.controller;

import java.util.List;

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
import com.tg.fyc.pojo.Brand;
import com.tg.fyc.pojo.Specification;
import com.tg.fyc.pojo.SpecificationVO;
import com.tg.fyc.pojo.TypeTemplate;
import com.tg.fyc.sellergoods.api.BrandApi;
import com.tg.fyc.sellergoods.api.SpecificationApi;
import com.tg.fyc.sellergoods.api.TypeTemplateApi;

@Controller
@RequestMapping("typeTemplate")
public class TypeTemplateController {
	@Autowired
	TypeTemplateApi typeTemplateApi;
	
	private Logger log=LoggerFactory.getLogger(TypeTemplateController.class);
	@RequestMapping("list1")
	@ResponseBody
	public List<TypeTemplate> list1() {
		
		return typeTemplateApi.list();
	}
	@RequestMapping("add")
	@ResponseBody
	public PageResult add(@RequestBody TypeTemplate typeTemplate) {
		PageResult pageResult=null;
		try {
			pageResult=typeTemplateApi.insertSelective(typeTemplate);
			
		} catch (Exception e) {
			log.error(GloablErrorMessageEnum.ERROR_GUIGE_SAVA.getMessage(),e,e);
			return pageResult.error(GloablErrorMessageEnum.ERROR_GUIGE_SAVA.getCode(), GloablErrorMessageEnum.ERROR_GUIGE_SAVA.getMessage());
		}
		return pageResult.success(null);
	}
	
	@RequestMapping("findPage")
	@ResponseBody
	public DataGrid findPage(Integer currentPage,Integer pageSize,@RequestBody TypeTemplate typeTemplate) {
		
		return typeTemplateApi.findPage(currentPage,pageSize,typeTemplate);
	}
	@RequestMapping("update")
	@ResponseBody
	public PageResult update(@RequestBody TypeTemplate typeTemplate) {
		PageResult pageResult=null;
		try {
			pageResult=typeTemplateApi.update(typeTemplate);
			
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
			pageResult=typeTemplateApi.del(ids);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(GloablErrorMessageEnum.ERROR_PUSH_REDIS.getMessage(),e,e);
			return PageResult.error(GloablErrorMessageEnum.ERROR_PUSH_REDIS.getCode(), GloablErrorMessageEnum.ERROR_PUSH_REDIS.getMessage());
		}
		return pageResult.success(null);
	}
	
	
}
