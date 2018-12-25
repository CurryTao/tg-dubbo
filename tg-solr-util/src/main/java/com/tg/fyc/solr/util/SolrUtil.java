package com.tg.fyc.solr.util;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.tg.fyc.dao.ItemMapper;
import com.tg.fyc.pojo.Item;

@Component
public class SolrUtil {
	
	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private SolrTemplate solrTemplate;
	
	public void importItemData() {
		Item item=new Item();
		item.setStatus("1");
		List<Item> itemList=itemMapper.selectByExample1(item);
		System.out.println("列表");
		for (Item item2 : itemList) {
			System.out.println(item2.getId()+" "+item2.getTitle()+" "+item2.getPrice());
			//提取的规格的json转换成map
			Map map = JSON.parseObject(item2.getSpec(), Map.class);
			item2.setSpecMap(map);
		}
		solrTemplate.saveBeans(itemList);
		solrTemplate.commit();
		System.out.println("结束");
	}
	
	
	public static void main(String[] args) {
		ApplicationContext context=new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
		SolrUtil solrUtil = (SolrUtil) context.getBean("solrUtil");
		solrUtil.importItemData();
		
	}
	
}
