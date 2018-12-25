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
import com.tg.fyc.pojo.ItemCat;
import com.tg.fyc.sellergoods.api.ItemCatApi;

@Controller
@RequestMapping("itemCat")
public class ItemCatController {
	
	@Autowired
	ItemCatApi itemCatApi;
	private Logger log=LoggerFactory.getLogger(ItemCatController.class);

	@RequestMapping("list1")
	@ResponseBody
	public List<Map<String, Object>> list() {
		return itemCatApi.list();
	}

	@RequestMapping("list2")
	@ResponseBody
	public List<Map<String, Object>> list2() {
		return itemCatApi.list2();
	}

	@RequestMapping("add")
	@ResponseBody
	public PageResult add(@RequestBody ItemCat itemCat) {
		PageResult pageResult=null;
		try {
			pageResult=itemCatApi.add(itemCat);
		} catch (Exception e) {
			return PageResult.error(GloablErrorMessageEnum.ERROR_SELLER_UPDATE.getCode(), GloablErrorMessageEnum.ERROR_SELLER_UPDATE.getMessage());
		}
		return  PageResult.success(null);

	}

	@RequestMapping("findPage")
	@ResponseBody
	public DataGrid findPage(Integer currentPage,Integer pageSize,@RequestBody ItemCat ItemCat) {
		return itemCatApi.findPage(currentPage,pageSize,ItemCat);
	}

	@RequestMapping("update")
	@ResponseBody
	public PageResult update(@RequestBody ItemCat itemCat) {
		PageResult pageResult=null;
		try {
			pageResult= itemCatApi.update(itemCat);
		} catch (Exception e) {
			return PageResult.error(GloablErrorMessageEnum.ERROR_SYSTEM_ERROR.getCode(), GloablErrorMessageEnum.ERROR_SYSTEM_ERROR.getMessage());
		}
		return  PageResult.success(null);
	}
	
	@RequestMapping("del")
	@ResponseBody
	public PageResult del(String ids) {
		PageResult pageResult=null;
		try {
			pageResult=itemCatApi.del(ids);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(GloablErrorMessageEnum.ERROR_PUSH_REDIS.getMessage(),e,e);
			return PageResult.error(GloablErrorMessageEnum.ERROR_PUSH_REDIS.getCode(), GloablErrorMessageEnum.ERROR_PUSH_REDIS.getMessage());
		}
		return pageResult.success(null);
	}
	
	@RequestMapping("ByParentId")
	@ResponseBody
	public PageResult ByParentId(Long parentId) {
		return itemCatApi.ByParentId(parentId);
	}

}
