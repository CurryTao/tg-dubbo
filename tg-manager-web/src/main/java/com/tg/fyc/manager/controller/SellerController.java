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
import com.tg.fyc.pojo.Seller;
import com.tg.fyc.sellergoods.api.BrandApi;
import com.tg.fyc.sellergoods.api.SellerApi;

@Controller
@RequestMapping("seller")
public class SellerController {
	@Autowired
	SellerApi sellerApi;
	private Logger log=LoggerFactory.getLogger(SellerController.class);
	/**
	 * 
	 * @author fuyuchuang
	 * @param name
	 * @return
	 */
	
	
	@RequestMapping("findPage")
	@ResponseBody
	public DataGrid findPage(Integer currentPage,Integer pageSize,@RequestBody Seller seller) {
		
		return sellerApi.findPage1(currentPage,pageSize,seller);
	}
	@RequestMapping("update")
	@ResponseBody
	public PageResult update(@RequestBody Seller seller) {
		PageResult pageResult=null;
			try {
				pageResult=sellerApi.update(seller);
			} catch (Exception e) {
				// TODO: handle exception
				return PageResult.error(GloablErrorMessageEnum.ERROR_SELLER_UPDATE.getCode(), GloablErrorMessageEnum.ERROR_SELLER_UPDATE.getMessage());
			}
			return  PageResult.success(null);
	}
	
	
}
