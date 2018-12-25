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
import com.tg.fyc.sellergoods.api.BrandApi;

@Controller
@RequestMapping("brand")
public class BrandController {
	@Autowired
	BrandApi brandApi;
	private Logger log=LoggerFactory.getLogger(BrandController.class);
	/**
	 * 
	 * @author fuyuchuang
	 * @param name
	 * @return
	 */
	@RequestMapping("list")
	@ResponseBody
	public String list(String name) {
		
		return name;
	}
	
	@RequestMapping("list1")
	@ResponseBody
	public List<Map<String, String>> list1() {
		
		return brandApi.list();
	}
	@RequestMapping("add")
	@ResponseBody
	public boolean add(@RequestBody Brand brand) {
		try {
			brandApi.insertSelective(brand);
			return true;
			
		} catch (Exception e) {
			return false;
			// TODO: handle exception
		}
		
	}
	
	@RequestMapping("findPage")
	@ResponseBody
	public DataGrid findPage(Integer currentPage,Integer pageSize,@RequestBody Brand brand) {
		
		return brandApi.findPage(currentPage,pageSize,brand);
	}
	@RequestMapping("findOne")
	@ResponseBody
	public Brand findOne(Long id) {
		
		return  brandApi.selectByPrimaryKey(id);
	}
	
	@RequestMapping("update")
	@ResponseBody
	public PageResult update(@RequestBody Brand brand) {
			try {
				brandApi.update(brand);
				return  PageResult.success(null);
				
			} catch (Exception e) {
				// TODO: handle exception
				return PageResult.error(GloablErrorMessageEnum.ERROR_SYSTEM_ERROR.getCode(), GloablErrorMessageEnum.ERROR_SYSTEM_ERROR.getMessage());
			}
		
	}
	@RequestMapping("del")
	@ResponseBody
	public PageResult del(String ids) {
		PageResult pageResult=null;
		try {
			pageResult=brandApi.del(ids);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(GloablErrorMessageEnum.ERROR_PUSH_REDIS.getMessage(),e,e);
			return PageResult.error(GloablErrorMessageEnum.ERROR_PUSH_REDIS.getCode(), GloablErrorMessageEnum.ERROR_PUSH_REDIS.getMessage());
		}
		return pageResult.success(null);
	}
	
	
}
