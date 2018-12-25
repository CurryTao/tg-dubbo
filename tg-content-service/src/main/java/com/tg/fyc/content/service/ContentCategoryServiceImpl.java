package com.tg.fyc.content.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tg.fyc.common.DataGrid;
import com.tg.fyc.common.PageResult;
import com.tg.fyc.content.api.ContentApi;
import com.tg.fyc.content.api.ContentCategoryApi;
import com.tg.fyc.dao.ContentCategoryMapper;
import com.tg.fyc.dao.ContentMapper;
import com.tg.fyc.pojo.Content;
import com.tg.fyc.pojo.ContentCategory;
@Service
public class ContentCategoryServiceImpl implements ContentCategoryApi{
	
	@Autowired
	ContentCategoryMapper contentCategoryMapper;
	
	@Override
	public DataGrid findPage(Integer currentPage, Integer pageSize, ContentCategory contentCategory) {
		// TODO Auto-generated method stub
		PageHelper.startPage(currentPage, pageSize);
		List<Content> list=contentCategoryMapper.findList(contentCategory);
		PageInfo<Content> pageInfo=new PageInfo<>(list);
		
		return new DataGrid(pageInfo.getTotal(),pageInfo.getList());
	}

	@Override
	public List<Map<String, Object>> list() {
		// TODO Auto-generated method stub
		return contentCategoryMapper.listall();
	}
	
	@Override
	public PageResult insertSelective(ContentCategory contentCategory) throws Exception{
		try {
			//先添加单表的
			contentCategoryMapper.insertSelective(contentCategory);
			//在添加多的
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException();
		}
		return PageResult.success(null);
	}

	@Override
	public PageResult update(ContentCategory contentCategory) throws Exception{
		try {
			contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
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
			for (String string : split) {
				contentCategoryMapper.deleteByPrimaryKey(Long.valueOf(string));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return PageResult.success(null);
	}

}
