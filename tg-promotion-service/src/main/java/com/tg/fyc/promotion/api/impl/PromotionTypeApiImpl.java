package com.tg.fyc.promotion.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tg.fyc.dao.PromotionTypeMapper;
import com.tg.fyc.pojo.PromotionType;
import com.tg.fyc.promotion.api.PromotionTypeApi;

@Service
public class PromotionTypeApiImpl implements PromotionTypeApi {

	@Autowired
	private PromotionTypeMapper promotionTypeMapper;
	
	@Override
	public List<PromotionType> getPromotionTypeList() {
		return this.promotionTypeMapper.getPromotionTypeList();
	}
	
}
