package com.tg.fyc.search.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.stereotype.Service;

import com.tg.fyc.pojo.Item;
import com.tg.fyc.search.api.ItemSearchApi;

@Service
public class ItemSearchServiceImpl implements ItemSearchApi{

	@Autowired
	private SolrTemplate solrTemplate;
	
	@Override
	public Map search(Map searchMap) {
		
		//判断输入的字把空格替换成空字符串
		String keywords = (String) searchMap.get("keywords");
		
		//单独加的
		if (keywords!=null && !keywords.equals("")) {
			searchMap.put("keywords", keywords.replace(" ", ""));
		}
		
		//创建map
		Map map=new HashMap();
		
//		Query query=new SimpleQuery("*:*");
//		
//		Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
//		
//		query.addCriteria(criteria);
//		
//		ScoredPage<Item> queryForPage = this.solrTemplate.queryForPage(query, Item.class);
//		
//		System.out.println(queryForPage.getContent());
//		
//		map.put("rows", queryForPage.getContent());
		
		//查询图片列表
		map.putAll(this.searchaList(searchMap));
		
		//查询列表的分类
		List<String> categoryList = this.serarchCategoryList(searchMap);
		map.put("categoryList", categoryList);
		
		//查询品牌和规格列表
		//有分类在进行查找
		String category = (String) searchMap.get("category");
		if (!category.equals("")) {
			map.putAll(this.searchBrandAndSpecList(category));
		}else {
			if (categoryList.size()>0) {
				map.putAll(searchBrandAndSpecList(categoryList.get(0)));
			}
		}
		
		return map;
	}
	//查询列表
	/**
	 * @param searchMap
	 * @return
	 */
	/**
	 * @param searchMap
	 * @return
	 */
	private Map searchaList(Map searchMap) {
		Map map=new HashMap();
		
				//获取高亮
				HighlightQuery query=new SimpleHighlightQuery();
				
				HighlightOptions highlightOptions=new HighlightOptions().addField("item_title");
				//获取前缀
				highlightOptions.setSimplePrefix("<em style='color:red'>");
				highlightOptions.setSimplePostfix("</em>");
				
				//设置查询对象
				query.setHighlightOptions(highlightOptions);
				
				Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
			
				query.addCriteria(criteria);
				
				//--1.2的
				//选择了分类进行赛选
				if (!"".equals(searchMap.get("category"))) {
					FilterQuery filterQuery=new SimpleFilterQuery();
					Criteria filteCriteria=new Criteria("item_category").is(searchMap.get("category"));
					filterQuery.addCriteria(filteCriteria);
					query.addFilterQuery(filterQuery);
				}
				//1.3品牌
				if (!"".equals(searchMap.get("brand"))) {
					FilterQuery filterQuery=new SimpleFilterQuery();
					Criteria filteCriteria=new Criteria("item_brand").is(searchMap.get("brand"));
					filterQuery.addCriteria(filteCriteria);
					query.addFilterQuery(filterQuery);
				}
				//1.4spec规格的判断
				if (searchMap.get("spec")!=null){
					//获取spec
					Map<String, String> specMap = (Map<String, String>) searchMap.get("spec");
					//进行循环
					for(String key:specMap.keySet()) {
						FilterQuery filterQuery=new SimpleFilterQuery();
						Criteria filteCriteria=new Criteria("item_spec_"+key).is(specMap.get(key));
						filterQuery.addCriteria(filteCriteria);
						query.addFilterQuery(filterQuery);
					}
				}
				//1.5价格的判断
				
				if(!"".equals(searchMap.get("price"))) {
					String price = (String) searchMap.get("price");
					String[] split = price.split("-");
					if (!split[0].equals("0")) {
						FilterQuery filterQuery=new SimpleFilterQuery();
						Criteria filteCriteria=new Criteria("item_price").greaterThanEqual(split[0]);
						filterQuery.addCriteria(filteCriteria);
						query.addFilterQuery(filterQuery);
					}
					if (!split[1].equals("*")) {
						FilterQuery filterQuery=new SimpleFilterQuery();
						Criteria filteCriteria=new Criteria("item_price").lessThanEqual(split[1]);
						filterQuery.addCriteria(filteCriteria);
						query.addFilterQuery(filterQuery);
					}
					
				}
				//1.6的分页
				Integer pageNo = (Integer) searchMap.get("pageNo");
				
				if (pageNo==null) {
					pageNo=1;
				}
				Integer pageSize = (Integer) searchMap.get("pageSize");
				if (pageSize==null) {
					pageSize=20;
				}
				//这个是索引
				query.setOffset((pageNo-1)*pageSize);
				//每页的记录数
				query.setRows(pageSize);
				
				//1.7的排序
				String sortValue = (String) searchMap.get("sort");
				String sortField = (String) searchMap.get("sortField");
				//判断条件不为空
				if (sortValue!=null && !sortValue.equals("")) {
					//判断是不是升序
					if (sortValue.equals("ASC")) {
						Sort sort=new Sort(Sort.Direction.ASC,"item_"+sortField);
						query.addSort(sort);
					}
					//判断是不是降序
					if (sortValue.equals("DESC")) {
						Sort sort=new Sort(Sort.Direction.DESC,"item_"+sortField);
						query.addSort(sort);
					}
					
				}
				
				//--------------------分割----
				
				//高亮的集合
				HighlightPage<Item> page = solrTemplate.queryForHighlightPage(query, Item.class);
				
				//高亮入口的集合
				List<HighlightEntry<Item>> entryList = page.getHighlighted();
				
				for (HighlightEntry<Item> entry : entryList) {
					//获取的高亮的列表 高领域的个数
					List<Highlight> highlights = entry.getHighlights();
					/**
					for (Highlight highlight : highlights) {
						List<String> sp = highlight.getSnipplets();
					}
					*/
					
					if (highlights.size()>0 && highlights.get(0).getSnipplets().size()>0) {
						Item item = entry.getEntity();
						item.setTitle(highlights.get(0).getSnipplets().get(0));
					}
				}
				map.put("rows", page.getContent());
				map.put("totalPages", page.getTotalPages());
				map.put("total", page.getTotalElements());
		return map;
	}
	
	private List<String> serarchCategoryList(Map searchMap) {
		List<String> list=new ArrayList();
		
		Query query=new SimpleQuery("*:*");
		
		//相当于where条件
		Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		//设置的分组的选项
		GroupOptions groupOptions=new GroupOptions().addGroupByField("item_category");
		query.setGroupOptions(groupOptions);
		//分组页
		GroupPage<Item> page = solrTemplate.queryForGroupPage(query, Item.class);
		//分组页结果的对象
		GroupResult<Item> groupResult = page.getGroupResult("item_category");
		
		//得到是入口页
		Page<GroupEntry<Item>> groupEntries = groupResult.getGroupEntries();
		//得到是入口集合
		List<GroupEntry<Item>> entityList = groupEntries.getContent();
		
		for (GroupEntry<Item> entity : entityList) {
			list.add(entity.getGroupValue());
		}
		
		return list;
		
	}
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * 根据商品分类名称查询品牌
	 * @param category 通过内容来查找
	 */
	private Map searchBrandAndSpecList(String category) {
		
		Map map=new HashMap<>();
		
		Long templateId= (Long) redisTemplate.boundHashOps("itemcat").get(category);
		
		if (templateId!=null) {
			List brandList= (List) redisTemplate.boundHashOps("brandList").get(templateId);
			map.put("brandList", brandList);
			System.out.println("brand="+brandList.size());
			List specList= (List) redisTemplate.boundHashOps("specList").get(templateId);
			map.put("specList", specList);
			System.out.println("specList="+specList.size());
		}
		return map;
	}
	
	@Override
	public void importList(List list) {
		// TODO Auto-generated method stub
		solrTemplate.saveBeans(list);
		solrTemplate.commit();
	}
	
	@Override
	public void deleteList(String string) {
		
		Query query=new SimpleQuery("*:*");
		
		Criteria criteria =new Criteria("item_goodsid").is(string);
		query.addCriteria(criteria);
		
		solrTemplate.delete(query);
		
		solrTemplate.commit();
		
	}
	
	
}
