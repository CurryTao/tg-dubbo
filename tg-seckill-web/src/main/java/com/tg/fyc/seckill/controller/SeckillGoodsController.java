package com.tg.fyc.seckill.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tg.fyc.pojo.SeckillGoods;
import com.tg.fyc.seckill.api.SeckillApi;

@RestController
@RequestMapping("goods")
public class SeckillGoodsController {

	@Autowired
	private SeckillApi seckillApi;
	
	@RequestMapping("/findList")
	public List<SeckillGoods> findList(){
		return seckillApi.findList();
	}
	
	@RequestMapping("/findOneFromRedis")
	public SeckillGoods findOneFromRedis(Long id){
		return seckillApi.findOneFromRedis(id);	
	}
	
}
