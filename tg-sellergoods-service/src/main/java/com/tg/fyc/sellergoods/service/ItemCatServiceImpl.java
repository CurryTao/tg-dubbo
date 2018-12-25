package com.tg.fyc.sellergoods.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tg.fyc.common.DataGrid;
import com.tg.fyc.common.PageResult;
import com.tg.fyc.dao.ItemCatMapper;
import com.tg.fyc.pojo.ItemCat;
import com.tg.fyc.sellergoods.api.BrandApi;
import com.tg.fyc.sellergoods.api.ItemCatApi;

@Service
public class ItemCatServiceImpl implements ItemCatApi{
	@Autowired
	ItemCatMapper itemCatMapper;
	
	private Logger log=LoggerFactory.getLogger(BrandApi.class);
	
	@Override
	public DataGrid findPage(Integer currentPage, Integer pageSize,ItemCat ItemCat) {
		// TODO Auto-generated method stub
		PageHelper.startPage(currentPage, pageSize);
		List<ItemCat> list=itemCatMapper.findlist(ItemCat);
		PageInfo<ItemCat> pageInfo=new PageInfo<ItemCat>(list);
		
		//放方法
		List<ItemCat> list2=itemCatMapper.findAll();
		for (ItemCat itemCat : list2) {
				redisTemplate.boundHashOps("itemcat").put(itemCat.getName(),itemCat.getTypeId());
		}
		System.out.println("将ItemCat放入到缓存");
		return new DataGrid(pageInfo.getTotal(), pageInfo.getList());
	}

	@Override
	public List<Map<String, Object>> list() {
		// TODO Auto-generated method stub
		return itemCatMapper.list();
	}

	@Override
	public PageResult add(ItemCat itemCat) throws Exception{
		try {
			//先添加单表的
			itemCatMapper.insertSelective(itemCat);
			//在添加多的
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException();
		}
		return PageResult.success(null);
	}

	@Override
	public PageResult update(ItemCat itemCat) {
		try {
			//先添加单表的
			itemCatMapper.updateByPrimaryKeySelective(itemCat);
			//在添加多的
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException();
		}
		return PageResult.success(null);
	}

	@Override
	public PageResult del(String ids) throws Exception{
		try {
			String[] split = ids.split(",");
			for (String id : split) {
				ItemCat itemCat = itemCatMapper.selectByPrimaryKey(Long.valueOf(id));
				itemCatMapper.updateByupdate(itemCat);
				itemCatMapper.deleteByPrimaryKey(Long.valueOf(id));
				log.info("删除----》id={}",id);
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
	public PageResult ByParentId(Long parentId) {
		List<ItemCat> list=itemCatMapper.ByParentId(parentId);
		return PageResult.success(list);
	}

	@Override
	public PageResult findOne(Long id) {
		ItemCat itemCat=itemCatMapper.selectByPrimaryKey(id);
		return PageResult.success(itemCat);
	}

	@Override
	public List<Map<String, Object>> list2() {
		return itemCatMapper.list2();
	}
	
}
