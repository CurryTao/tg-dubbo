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
import com.tg.fyc.pojo.Brand;
import com.tg.fyc.pojo.Specification;
import com.tg.fyc.pojo.SpecificationVO;
import com.tg.fyc.sellergoods.api.BrandApi;
import com.tg.fyc.sellergoods.api.SpecificationApi;

@Controller
@RequestMapping("specification")
public class SpecificationController {
	@Autowired
	SpecificationApi specificationApi;
	
	private Logger log=LoggerFactory.getLogger(SpecificationController.class);
	@RequestMapping("list1")
	@ResponseBody
	public List<Map<String, String>> list1() {
		
		return specificationApi.list();
	}
	@RequestMapping("add")
	@ResponseBody
	public PageResult add(@RequestBody SpecificationVO specificationVO) {
		PageResult pageResult=null;
		try {
			pageResult=specificationApi.insertSelective(specificationVO);
			
		} catch (Exception e) {
			log.error(GloablErrorMessageEnum.ERROR_GUIGE_SAVA.getMessage(),e,e);
			return pageResult.error(GloablErrorMessageEnum.ERROR_GUIGE_SAVA.getCode(), GloablErrorMessageEnum.ERROR_GUIGE_SAVA.getMessage());
		}
		return pageResult.success(null);
	}
	
	@RequestMapping("findPage")
	@ResponseBody
	public DataGrid findPage(Integer currentPage,Integer pageSize,@RequestBody Specification specification) {
		
		return specificationApi.findPage(currentPage,pageSize,specification);
	}
	@RequestMapping("findgui")
	@ResponseBody
	public DataGrid findgui(Long id) {
		
		return specificationApi.findgui(id);
	}
	@RequestMapping("update")
	@ResponseBody
	public PageResult update(@RequestBody SpecificationVO specificationVO) {
		PageResult pageResult=null;
		try {
			pageResult=specificationApi.update(specificationVO);
			
		} catch (Exception e) {
			log.error(GloablErrorMessageEnum.ERROR_GUIGE_SAVA.getMessage(),e,e);
			return pageResult.error(GloablErrorMessageEnum.ERROR_GUIGE_UPDATE.getCode(), GloablErrorMessageEnum.ERROR_GUIGE_UPDATE.getMessage());
		}
		return pageResult.success(null);
	}
	@RequestMapping("del")
	@ResponseBody
	public PageResult del(String ids) {
		PageResult pageResult=null;
		try {
			pageResult=specificationApi.del(ids);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(GloablErrorMessageEnum.ERROR_PUSH_REDIS.getMessage(),e,e);
			return PageResult.error(GloablErrorMessageEnum.ERROR_PUSH_REDIS.getCode(), GloablErrorMessageEnum.ERROR_PUSH_REDIS.getMessage());
		}
		return pageResult.success(null);
	}
	
	
}
