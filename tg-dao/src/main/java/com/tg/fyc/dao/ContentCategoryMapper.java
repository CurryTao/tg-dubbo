package com.tg.fyc.dao;

import java.util.List;
import java.util.Map;

import com.tg.fyc.pojo.Content;
import com.tg.fyc.pojo.ContentCategory;

public interface ContentCategoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ContentCategory record);

    int insertSelective(ContentCategory record);

    ContentCategory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ContentCategory record);

    int updateByPrimaryKey(ContentCategory record);

	List<Content> findList(ContentCategory contentCategory);

	List<Map<String, Object>> listall();
}