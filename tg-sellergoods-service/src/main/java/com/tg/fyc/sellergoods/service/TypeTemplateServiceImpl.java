package com.tg.fyc.sellergoods.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tg.fyc.common.DataGrid;
import com.tg.fyc.common.PageResult;
import com.tg.fyc.dao.SpecificationOptionMapper;
import com.tg.fyc.dao.TypeTemplateMapper;
import com.tg.fyc.pojo.Brand;
import com.tg.fyc.pojo.SpecificationOption;
import com.tg.fyc.pojo.TypeTemplate;
import com.tg.fyc.sellergoods.api.BrandApi;
import com.tg.fyc.sellergoods.api.TypeTemplateApi;

@Service
public class TypeTemplateServiceImpl implements TypeTemplateApi{
	@Autowired
	TypeTemplateMapper typeTemplateMapper;
	private Logger log=LoggerFactory.getLogger(BrandApi.class);

	@Override
	public DataGrid findPage(Integer currentPage, Integer pageSize,TypeTemplate typeTemplate) {
		// TODO Auto-generated method stub
		PageHelper.startPage(currentPage, pageSize);
		List<Brand> list=typeTemplateMapper.findlist(typeTemplate);
		PageInfo<Brand> pageInfo=new PageInfo<Brand>(list);
		
		saveToredis();
		
		return new DataGrid(pageInfo.getTotal(), pageInfo.getList());
	}
	
	@Autowired
	private RedisTemplate  redisTemplate;
	
	private void saveToredis() {
		List<TypeTemplate> templateList=typeTemplateMapper.findAll();
		for (TypeTemplate typeTemplate : templateList) {
			//得到了品牌列表
			List brandList = JSON.parseArray(typeTemplate.getBrandIds(), Map.class);
			//放入redis缓存
			redisTemplate.boundHashOps("brandList").put(typeTemplate.getId(), brandList);
			//调用下面的方法 通过模板id获取规格通过规格获取规格选项
			List<Map> specList = findSpecList(typeTemplate.getId());
			
			redisTemplate.boundHashOps("specList").put(typeTemplate.getId(), specList);
		}
		System.out.println("将模板放入缓存");
		
	}
	
	
	
	
	
	@Override
	public List<TypeTemplate> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageResult insertSelective(TypeTemplate typeTemplate) throws Exception{
		// TODO Auto-generated method stub
		try {
			//先添加单表的
			typeTemplateMapper.insertSelective(typeTemplate);
			//在添加多的
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException();
		}
		return PageResult.success(null);
	}

	@Override
	public PageResult update(TypeTemplate typeTemplate) throws Exception{
		try {
			typeTemplateMapper.updateByPrimaryKeySelective(typeTemplate);
			
		} catch (Exception e) {
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
				typeTemplateMapper.deleteByPrimaryKey(Long.valueOf(id));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return PageResult.success(null);
		
	}

	@Override
	public PageResult findOne(Long id) {
		// TODO Auto-generated method stub
		TypeTemplate typeTemplate=typeTemplateMapper.selectByPrimaryKey(id);
		return PageResult.success(typeTemplate);
	}

	@Autowired
	private SpecificationOptionMapper specificationOptionMapper;
	
	
	@Override
	public List<Map> findSpecList(Long id) {
		// TODO Auto-generated method stub
		TypeTemplate typeTemplate = typeTemplateMapper.selectByPrimaryKey(id);
		List<Map> list = JSON.parseArray(typeTemplate.getSpecIds(),Map.class);
		for (Map map : list) {
			SpecificationOption example=new SpecificationOption();
			List<SpecificationOption> options = specificationOptionMapper.selectByExample(new Long( (Integer)map.get("id")));
			map.put("options", options);
		}
		
		return list;
	}
	
	
	
	
	
	
}
