package com.tg.fyc.sellergoods.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tg.fyc.common.DataGrid;
import com.tg.fyc.common.PageResult;
import com.tg.fyc.dao.BrandMapper;
import com.tg.fyc.pojo.Brand;
import com.tg.fyc.sellergoods.api.BrandApi;
@Service
public class BrandServiceImpl implements BrandApi{
	
	@Autowired
	BrandMapper brandMapper;
	
	private Logger log=LoggerFactory.getLogger(BrandApi.class);
	
	public List<Map<String, String>> list() {
		return brandMapper.list();
	}


	@Override
	public void insertSelective(Brand brand) {
		// TODO Auto-generated method stub
		brandMapper.insertSelective(brand);
	}


	@Override
	public DataGrid findPage(Integer currentPage, Integer pageSize,Brand brand) {
		// TODO Auto-generated method stub
		PageHelper.startPage(currentPage, pageSize);
		List<Brand> list=brandMapper.findlist(brand);
		PageInfo<Brand> pageInfo=new PageInfo<Brand>(list);
		return new DataGrid(pageInfo.getTotal(), pageInfo.getList());
	}


	@Override
	public Brand selectByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return brandMapper.selectByPrimaryKey(id);
	}


	@Override
	public void update(Brand brand) {
		// TODO Auto-generated method stub
		brandMapper.updateByPrimaryKeySelective(brand);
	}

	@Override
	public PageResult del(String ids) throws Exception{
		try {
			String[] split = ids.split(",");
			for (String id : split) {
				brandMapper.deleteByPrimaryKey(Long.valueOf(id));
				log.info("删除----》id={}",id);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return PageResult.success(null);
		
	}


	
}
