package com.tg.fyc.promotion.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tg.fyc.common.DataGrid;
import com.tg.fyc.common.PageResult;
import com.tg.fyc.dao.PromotionMapper;
import com.tg.fyc.pojo.Goods;
import com.tg.fyc.pojo.Promotion;
import com.tg.fyc.promotion.api.PromotionApi;

@Service
public class PromotionApiImpl implements PromotionApi {

	@Autowired
	private PromotionMapper promotionMapper;

	@Override
	public void insertSelective(Promotion Promotion) {
		String goodsIds = Promotion.getGoodsIds();
		String[] split = goodsIds.split(",");
		if(split!=null && split.length>0){
			for (String goods : split) {
				Promotion.setGoodsIds(goods);
				Promotion.setStatus(0);
				Promotion.setCreattime(new Date());
				Promotion.setCreatby(Promotion.getSellerId());
				promotionMapper.insertSelective(Promotion);
			}
		}else{
			Promotion.setStatus(0);
			Promotion.setCreattime(new Date());
			Promotion.setCreatby(Promotion.getSellerId());
			promotionMapper.insertSelective(Promotion);
		}
	}

	@Override
	public DataGrid findPageForManger(Integer currentPage, Integer pageSize,Promotion Promotion) {
		PageHelper.startPage(currentPage, pageSize);
		List<Promotion> list=promotionMapper.findlistForSeller(Promotion);
		PageInfo<Promotion> pageInfo=new PageInfo<Promotion>(list);
		return new DataGrid(pageInfo.getTotal(), pageInfo.getList());
	}

	@Override
	public DataGrid findPage(Integer currentPage, Integer pageSize,Promotion Promotion) {
		PageHelper.startPage(currentPage, pageSize);
		List<Promotion> list=promotionMapper.findlistForSeller(Promotion);
		PageInfo<Promotion> pageInfo=new PageInfo<Promotion>(list);
		return new DataGrid(pageInfo.getTotal(), pageInfo.getList());
	}

	@Override
	public List<Promotion> findAll(Long sellerId) {
		return this.promotionMapper.findAll(sellerId);
	}

	@Override
	public Promotion selectByPrimaryKey(Long id) {
		return promotionMapper.selectByPrimaryKey(id);
	}

	@Override
	public void update(Promotion Promotion) {

		String goodsIds = Promotion.getGoodsIds();
		List<Goods> parseArray = JSON.parseArray(goodsIds, Goods.class);
		if(parseArray!=null && !parseArray.isEmpty()){
			for (Goods goods : parseArray) {
				Promotion.setGoodsIds(String.valueOf(goods.getId()));
				Promotion.setUpdatetime(new Date());
				Promotion.setUpdateby(Promotion.getSellerId());
				promotionMapper.updateByPrimaryKeySelective(Promotion);
			}
		}else{
			Promotion.setUpdatetime(new Date());
			Promotion.setUpdateby(Promotion.getSellerId());
			promotionMapper.insertSelective(Promotion);
		}
	}

	@Override
	public PageResult del(String ids) throws Exception{
		try {
			String[] split = ids.split(",");
			for (String id : split) {
				promotionMapper.deleteByPrimaryKey(Long.valueOf(id));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return PageResult.success(null);
	}

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public PageResult updatestatus(String[] ids, String status) {
		for (String string : ids) {
			Promotion Promotion=promotionMapper.selectByPrimaryKey(Long.valueOf(string));
			Promotion.setStatus(Integer.valueOf(status));
			promotionMapper.updateByPrimaryKeySelective(Promotion);

			//如果审核通过则促销商品放入到redis中
			if(status.equals("2")){
				redisTemplate.boundHashOps("promotion").put(Promotion.getGoodsIds(), Promotion);
			}
			
			//如果 驳回/结束活动 则促销商品放入到redis中
			if(status.equals("4") || status.equals("5")){
				redisTemplate.boundHashOps("promotion").delete(Promotion.getGoodsIds());
			}
		}
		return PageResult.success(null);
	}
	
	
}
