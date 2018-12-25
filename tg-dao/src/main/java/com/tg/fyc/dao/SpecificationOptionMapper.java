package com.tg.fyc.dao;

import java.util.List;

import com.tg.fyc.common.DataGrid;
import com.tg.fyc.pojo.SpecificationOption;

public interface SpecificationOptionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SpecificationOption record);

    int insertSelective(SpecificationOption record);

    SpecificationOption selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SpecificationOption record);

    int updateByPrimaryKey(SpecificationOption record);

	List<SpecificationOption> findgui(Long id);

	void delshanid(Long id);

	void deleteByPrimaryKeyfor(Long id);

	List<SpecificationOption> selectByExample(Long long1);
}