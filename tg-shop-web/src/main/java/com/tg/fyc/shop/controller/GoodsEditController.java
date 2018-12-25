package com.tg.fyc.shop.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tg.fyc.common.DataGrid;
import com.tg.fyc.common.FastDFSClient;
import com.tg.fyc.common.GloablErrorMessageEnum;
import com.tg.fyc.common.PageResult;
import com.tg.fyc.pojo.Goods;
import com.tg.fyc.pojo.GoodsDeitVO;
import com.tg.fyc.sellergoods.api.GoodsEditApi;

@Controller
@RequestMapping("goodsEdit")
public class GoodsEditController {
	
	@Autowired
	private GoodsEditApi goodsEditApi;
	
	private Logger log=LoggerFactory.getLogger(GoodsEditController.class);
	
	@RequestMapping("add")
	@ResponseBody
	public PageResult add(@RequestBody GoodsDeitVO goodsDeitVO) {
		
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		goodsDeitVO.getGoods().setSellerId(name);
		
		PageResult pageResult=null;
		try {
			pageResult=goodsEditApi.add(goodsDeitVO);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(GloablErrorMessageEnum.ERROR_SELLER_SAVA.getMessage(),e,e);
			return pageResult.error(GloablErrorMessageEnum.ERROR_SELLER_SAVA.getCode(), GloablErrorMessageEnum.ERROR_SELLER_SAVA.getMessage());
		}
		return pageResult.success(null);
	}
	
	@Value("${FILE_SERVER_URL}")
	private String file_server_url;
	
	@RequestMapping("upload")
	@ResponseBody
	public PageResult upload(MultipartFile file) {
		String filename = file.getOriginalFilename();
		String string = filename.substring(filename.lastIndexOf(".")+1);
		try {
			FastDFSClient client=new FastDFSClient("classpath:fdfs_client.conf");
			 String file2 = client.uploadFile(file.getBytes(),string);
			 String url=file_server_url+file2;
			return PageResult.success(url);
		} catch (Exception e) {
			return PageResult.error(GloablErrorMessageEnum.ERROR_FILE_UPLOAD.getCode(), GloablErrorMessageEnum.ERROR_FILE_UPLOAD.getMessage());
		}
	}
	
	@RequestMapping("findPage")
	@ResponseBody
	public DataGrid findPage(Integer currentPage,Integer pageSize,@RequestBody Goods goods) {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		goods.setSellerId(name);
		return goodsEditApi.findPage(currentPage,pageSize,goods);
	}
	
	@RequestMapping("findAll")
	@ResponseBody
	public List<Goods> findPage() {
		Goods goods=new Goods();
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		goods.setSellerId(name);
		return goodsEditApi.findAll(goods);
	}
	
	@RequestMapping("findOne")
	@ResponseBody
	public GoodsDeitVO findOne(Long id) {
		return goodsEditApi.findOne(id);
	}
	
	@RequestMapping("update")
	@ResponseBody
	public PageResult update(@RequestBody GoodsDeitVO goodsDeitVO) {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		
		GoodsDeitVO findOne = goodsEditApi.findOne(goodsDeitVO.getGoods().getId());
		PageResult pageResult=null;
		if (!findOne.getGoods().getSellerId().equals(name)) {
			return pageResult.error(GloablErrorMessageEnum.ERROR_PARAM_ILLEGAL.getCode(), GloablErrorMessageEnum.ERROR_PARAM_ILLEGAL.getMessage());
		}
		try {
			pageResult=goodsEditApi.update(goodsDeitVO);
		} catch (Exception e) {
			log.error(GloablErrorMessageEnum.ERROR_GUIGE_UPDATE.getMessage(),e,e);
			return pageResult.error(GloablErrorMessageEnum.ERROR_GUIGE_UPDATE.getCode(), GloablErrorMessageEnum.ERROR_GUIGE_UPDATE.getMessage());
		}
		return pageResult.success(null);
	}
	
	@RequestMapping("pltijiao")
	@ResponseBody
	public PageResult pltijiao(String ids) {
		return goodsEditApi.pltijiao(ids);
	}
	
}
