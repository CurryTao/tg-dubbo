package com.tg.fyc.content.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tg.fyc.common.DataGrid;
import com.tg.fyc.common.PageResult;
import com.tg.fyc.content.api.ContentApi;
import com.tg.fyc.dao.ContentMapper;
import com.tg.fyc.pojo.Content;

@Service
public class ContentServiceImpl implements ContentApi{
	
	@Autowired
	ContentMapper contentMapper;
	
	@Override
	public DataGrid findPage(Integer currentPage, Integer pageSize,Content content) {
		// TODO Auto-generated method stub
		PageHelper.startPage(currentPage, pageSize);
		List<Content> list=contentMapper.findList(content);
		PageInfo<Content> pageInfo=new PageInfo<>(list);
		
		return new DataGrid(pageInfo.getTotal(),pageInfo.getList());
	}

	@Override
	public PageResult insertSelective(Content content) throws Exception{
		try {
			//先添加单表的
			contentMapper.insertSelective(content);
			redisTemplate.boundHashOps("content").delete(content.getCategoryId());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException();
		}
		return PageResult.success(null);
	}

	@Override
	public PageResult update(Content content) throws Exception{
		try {
			Long categoryId = contentMapper.selectByPrimaryKey(content.getId()).getCategoryId();
			//删除redi的缓存
			redisTemplate.boundHashOps("content").delete(categoryId);
			//这是进行修改
			contentMapper.updateByPrimaryKeySelective(content);
			if (categoryId.longValue()!=content.getCategoryId().longValue()) {
				//删除分类的ID
				redisTemplate.boundHashOps("content").delete(content.getCategoryId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return PageResult.success(null);
	}

	@Override
	public PageResult del(String ids) throws Exception{
		// TODO Auto-generated method stub
		try {
			String[] split = ids.split(",");
			for (String string : split) {
				Long categoryId = contentMapper.selectByPrimaryKey(Long.valueOf(string)).getCategoryId();
				redisTemplate.boundHashOps("content").delete(categoryId);
				contentMapper.deleteByPrimaryKey(Long.valueOf(string));
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
	public List<Content> findByCategoryId(Long categoryId) {
		// TODO Auto-generated method stub
		List<Content>  list=(List<Content>) redisTemplate.boundHashOps("content").get(categoryId);
		if (list==null) {
			Content content=new Content();
			content.setStatus("1");
			content.setCategoryId(categoryId);
			list=contentMapper.selectByExample(content);
			redisTemplate.boundHashOps("content").put(categoryId, list);
			System.out.println("新加的");
		}else {
			System.out.println("缓存的");
		}
		return list;
	}

}
