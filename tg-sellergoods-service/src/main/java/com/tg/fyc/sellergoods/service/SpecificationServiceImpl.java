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
import com.tg.fyc.dao.SpecificationMapper;
import com.tg.fyc.dao.SpecificationOptionMapper;
import com.tg.fyc.pojo.Brand;
import com.tg.fyc.pojo.Specification;
import com.tg.fyc.pojo.SpecificationOption;
import com.tg.fyc.pojo.SpecificationVO;
import com.tg.fyc.sellergoods.api.BrandApi;
import com.tg.fyc.sellergoods.api.SpecificationApi;
@Service
public class SpecificationServiceImpl implements SpecificationApi{
	@Autowired
	SpecificationMapper specificationMapper;
	@Autowired
	SpecificationOptionMapper specificationOptionMapper;
	
	private Logger log=LoggerFactory.getLogger(BrandApi.class);
	

	@Override
	public DataGrid findPage(Integer currentPage, Integer pageSize,Specification specification) {
		// TODO Auto-generated method stub
		PageHelper.startPage(currentPage, pageSize);
		List<Brand> list=specificationMapper.findlist(specification);
		PageInfo<Brand> pageInfo=new PageInfo<Brand>(list);
		return new DataGrid(pageInfo.getTotal(), pageInfo.getList());
	}

	@Override
	public List<Map<String, String>> list() {
		// TODO Auto-generated method stub
		return specificationMapper.list();
	}

	@Override
	public PageResult insertSelective(SpecificationVO specificationVO) throws Exception{
		// TODO Auto-generated method stub
		try {
			//先添加单表的
			Specification specification = specificationVO.getSpecification();
			specificationMapper.insertSelective(specification);
			
			//在添加多的
			List<SpecificationOption> list = specificationVO.getSpecificationOptionList();
			for (SpecificationOption specificationOption : list) {
				specificationOption.setSpecId(specification.getId());
				specificationOptionMapper.insertSelective(specificationOption);
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException();
		}
		return PageResult.success(null);
	}

	@Override
	public DataGrid findgui(Long id) {
		List<SpecificationOption> findgui = specificationOptionMapper.findgui(id);
		
		
		return new DataGrid(100l,findgui);
	}

	@Override
	public PageResult update(SpecificationVO specificationVO) throws Exception{
		try {
			Specification specification = specificationVO.getSpecification();
			specificationMapper.updateByPrimaryKeySelective(specification);
			Long id = specification.getId();
			specificationOptionMapper.delshanid(id);
			List<SpecificationOption> specificationOptionList = specificationVO.getSpecificationOptionList();
			for (SpecificationOption specificationOption : specificationOptionList) {
				specificationOption.setSpecId(specification.getId());
				specificationOptionMapper.insertSelective(specificationOption);
			}
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
				specificationMapper.deleteByPrimaryKey(Long.valueOf(id));
				log.info("删除----》id={}",id);
				specificationOptionMapper.deleteByPrimaryKeyfor(Long.valueOf(id));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return PageResult.success(null);
		
	}


	
}
