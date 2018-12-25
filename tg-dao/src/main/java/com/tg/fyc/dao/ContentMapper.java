package com.tg.fyc.dao;

import java.util.List;

import com.tg.fyc.pojo.Content;

public interface ContentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Content record);

    int insertSelective(Content record);

    Content selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Content record);

    int updateByPrimaryKey(Content record);

	List<Content> findList(Content content);

	List<Content> selectByExample(Content content);
}