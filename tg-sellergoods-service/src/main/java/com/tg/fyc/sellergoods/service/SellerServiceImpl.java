package com.tg.fyc.sellergoods.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tg.fyc.common.DataGrid;
import com.tg.fyc.common.GloablErrorMessageEnum;
import com.tg.fyc.common.PageResult;
import com.tg.fyc.dao.SellerMapper;
import com.tg.fyc.pojo.Seller;
import com.tg.fyc.sellergoods.api.SellerApi;

@Service
public class SellerServiceImpl implements SellerApi {
	@Autowired
	private SellerMapper sellermapper;
		
	private Logger log=LoggerFactory.getLogger(SellerServiceImpl.class);
	
	@Override
	public PageResult insertSelective(Seller seller) throws Exception {
		
		try {
			sellermapper.insertSelective(seller);
			
		} catch (Exception e) {
			log.error(GloablErrorMessageEnum.ERROR_SELLER_SAVA.getMessage(),e,e);
			throw new RuntimeException();
		}
		
		return PageResult.success(null);
	}

	@Override
	public Seller findList(String username) {
		// TODO Auto-generated method stub
		return sellermapper.selectByPrimaryKey(username);
	}

	@Override
	public DataGrid findPage(Integer currentPage, Integer pageSize, Seller seller) {
		// TODO Auto-generated method stub
		PageHelper.startPage(currentPage, pageSize);
		List<Seller> list=sellermapper.findlist(seller);
		PageInfo<Seller> pageInfo=new PageInfo<Seller>(list);
		return new DataGrid(pageInfo.getTotal(), pageInfo.getList());
	}

	@Override
	public PageResult update(Seller seller) throws Exception{
		// TODO Auto-generated method stub
		try {
			sellermapper.updateByPrimaryKeySelective(seller);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return PageResult.success(null);
		
		
	}

	@Override
	public DataGrid findPage1(Integer currentPage, Integer pageSize, Seller seller) {
		// TODO Auto-generated method stub
		PageHelper.startPage(currentPage, pageSize);
		List<Seller> list=sellermapper.findlist1(seller);
		PageInfo<Seller> pageInfo=new PageInfo<Seller>(list);
		return new DataGrid(pageInfo.getTotal(), pageInfo.getList());
	}

}
