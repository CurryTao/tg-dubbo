package com.tg.fyc.sellergoods.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tg.fyc.common.DataGrid;
import com.tg.fyc.common.PageResult;
import com.tg.fyc.dao.BrandMapper;
import com.tg.fyc.dao.GoodsDescMapper;
import com.tg.fyc.dao.GoodsMapper;
import com.tg.fyc.dao.ItemCatMapper;
import com.tg.fyc.dao.ItemMapper;
import com.tg.fyc.dao.SellerMapper;
import com.tg.fyc.pojo.Brand;
import com.tg.fyc.pojo.Goods;
import com.tg.fyc.pojo.GoodsDeitVO;
import com.tg.fyc.pojo.GoodsDesc;
import com.tg.fyc.pojo.Item;
import com.tg.fyc.pojo.ItemCat;
import com.tg.fyc.pojo.Seller;
import com.tg.fyc.sellergoods.api.BrandApi;
import com.tg.fyc.sellergoods.api.GoodsEditApi;

@Service
public class GoodsEditServiceImpl implements GoodsEditApi{

	private Logger log=LoggerFactory.getLogger(BrandApi.class);

	@Autowired
	GoodsMapper  goodsMapper;

	@Autowired
	GoodsDescMapper  goodsDescMapper;

	@Autowired
	private BrandMapper brandMapper;

	@Autowired
	private ItemMapper itemMapper;

	@Autowired
	private ItemCatMapper itemCatMapper;

	@Autowired
	private SellerMapper sellerMapper;

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public PageResult add(GoodsDeitVO goodsDeitVO) throws Exception{
		try {
			//默认添加状态未审核
			goodsDeitVO.getGoods().setAuditStatus("0");

			//插入基本信息
			goodsMapper.insertSelective(goodsDeitVO.getGoods());

			goodsDeitVO.getGoodsDesc().setGoodsId(goodsDeitVO.getGoods().getId());
			goodsDescMapper.insertSelective(goodsDeitVO.getGoodsDesc());

			insertuppblic(goodsDeitVO);
		} catch (Exception e) {
			throw new  RuntimeException();
		}
		return PageResult.success(null);
	}

	private void setItemValues(Item item,GoodsDeitVO goodsDeitVO) {
		item.setCategoryid(goodsDeitVO.getGoods().getCategory3Id());
		item.setCreateTime(new Date());
		item.setGoodsId(goodsDeitVO.getGoods().getId());
		item.setSellerId(goodsDeitVO.getGoods().getSellerId());

		//分类名称
		ItemCat itemCat = itemCatMapper.selectByPrimaryKey(goodsDeitVO.getGoods().getCategory3Id());
		item.setCategory(itemCat.getName());
		Brand brand = brandMapper.selectByPrimaryKey(goodsDeitVO.getGoods().getBrandId());
		item.setBrand(brand.getName());
		Seller seller = sellerMapper.selectByPrimaryKey(goodsDeitVO.getGoods().getSellerId());
		item.setSeller(seller.getNickName());

		List<Map> imagelist = JSON.parseArray(goodsDeitVO.getGoodsDesc().getItemImages(),Map.class);
		if (imagelist.size()>0) {
			item.setImage((String)imagelist.get(0).get("url"));
		}
	}

	@Override
	public DataGrid findPage(Integer currentPage, Integer pageSize, Goods goods) {
		PageHelper.startPage(currentPage, pageSize);
		List<Goods> list=goodsMapper.selectFindPage(goods);
		PageInfo<Goods> pageInfo=new PageInfo<>(list);
		return new DataGrid(pageInfo.getTotal(),pageInfo.getList());
	}

	@Override
	public GoodsDeitVO findOne(Long id) {
		GoodsDeitVO goodsDeitVO=new GoodsDeitVO();
		Goods goods = goodsMapper.selectByPrimaryKey(id);
		goodsDeitVO.setGoods(goods);
		GoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(id);
		goodsDeitVO.setGoodsDesc(goodsDesc);
		List<Item> itemList = itemMapper.selectByExample(id);
		goodsDeitVO.setItemList(itemList);
		return goodsDeitVO;
	}

	@Override
	public PageResult update(GoodsDeitVO goodsDeitVO) throws Exception{
		try {
			goodsMapper.updateByPrimaryKeySelective(goodsDeitVO.getGoods());

			goodsDescMapper.updateByPrimaryKeySelective(goodsDeitVO.getGoodsDesc());

			itemMapper.deleteByGoodsId(goodsDeitVO.getGoods().getId());

			insertuppblic(goodsDeitVO);
		} catch (Exception e) {
			throw new  RuntimeException();
		}
		return PageResult.success(null);
	}

	//提取公共的添加方法
	private void  insertuppblic(GoodsDeitVO goodsDeitVO) {
		if ("1".equals(goodsDeitVO.getGoods().getIsEnableSpec())) {
			for(Item item:goodsDeitVO.getItemList()) {
				String title=goodsDeitVO.getGoods().getGoodsName();
				Map<String, Object> map = JSON.parseObject(item.getSpec());
				for(String key:map.keySet()) {
					title+=" "+map.get(key);
				}
				item.setTitle(title); 
				setItemValues(item, goodsDeitVO);
				itemMapper.insertSelective(item);
			}
		}else {
			Item item=new Item();
			item.setTitle(goodsDeitVO.getGoods().getGoodsName());
			item.setPrice(goodsDeitVO.getGoods().getPrice());
			item.setNum(9999);
			item.setStatus("1");
			item.setIsDefault("1");
			item.setSpec("{}");
			setItemValues(item, goodsDeitVO);
			itemMapper.insertSelective(item);
		}
	}

	@Override
	public PageResult pltijiao(String ids) {
		String[] split = ids.split(",");
		for (String string : split) {
			goodsMapper.updateStatus(Long.valueOf(string));
		}
		return PageResult.success(null);
	}

	@Override
	public DataGrid findPage2(Integer currentPage, Integer pageSize, Goods goods) {
		PageHelper.startPage(currentPage, pageSize);
		List<Goods> list=goodsMapper.selectFindPage2(goods);
		PageInfo<Goods> pageInfo=new PageInfo<>(list);

		return new DataGrid(pageInfo.getTotal(),pageInfo.getList());
	}

	@Override
	public PageResult updatestatus(String ids, String status) {
		String[] split = ids.split(",");
		for (String string : split) {

			Goods goods=goodsMapper.selectByPrimaryKey(Long.valueOf(string));
			goods.setAuditStatus(status);
			goodsMapper.updateByPrimaryKeySelective(goods);

			//将商品信息放到redis中
			if(status.equals("2")){
				Item item=new Item();
				item.setGoodsId(goods.getId());
				List<Item> selectByExample3 = this.itemMapper.selectByExample3(item);
				if(selectByExample3!=null && selectByExample3.size()>0){
					for (Item item2 : selectByExample3) {
						redisTemplate.boundHashOps("item").put(item2.getId(), item2);
					}
				}
			}
		}

		return PageResult.success(null);
	}

	@Override
	public PageResult del(String ids) {
		String[] split = ids.split(",");
		//循环删除
		for (String string : split) {
			Goods goods=goodsMapper.selectByPrimaryKey(Long.valueOf(string));
			goods.setIsDelete("1");
			goodsMapper.updateByPrimaryKeySelective(goods);
		}
		return PageResult.success(null);
	}

	public List<Item> findItemListByGoodsIdListAndStatus(String goodsIds,String status){
		String[] split = goodsIds.split(",");
		Item item=new Item();
		List<Item> list=null;
		for (String string : split) {
			item.setGoodsId(Long.valueOf(string));
			item.setStatus(status);
			list=itemMapper.selectByExample2(item);
		}
		return list;
	}

	@Override
	public List<Goods> findAll(Goods goods) {
		return this.goodsMapper.findAll(goods);
	}

}