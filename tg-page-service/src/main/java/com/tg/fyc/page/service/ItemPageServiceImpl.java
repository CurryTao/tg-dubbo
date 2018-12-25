package com.tg.fyc.page.service;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.alibaba.fastjson.JSON;
import com.tg.fyc.dao.GoodsDescMapper;
import com.tg.fyc.dao.GoodsMapper;
import com.tg.fyc.dao.ItemCatMapper;
import com.tg.fyc.dao.ItemMapper;
import com.tg.fyc.dao.PromotionMapper;
import com.tg.fyc.page.api.ItemPageApi;
import com.tg.fyc.pojo.Goods;
import com.tg.fyc.pojo.GoodsDesc;
import com.tg.fyc.pojo.Item;
import com.tg.fyc.pojo.Promotion;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class ItemPageServiceImpl implements ItemPageApi {

	@Value("${pagedir}")
	private String pagedir;
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Autowired
	private GoodsMapper goodsMapper;

	@Autowired
	private GoodsDescMapper goodsDescMapper;

	@Autowired
	private ItemCatMapper itemCatMapper;

	@Autowired
	private ItemMapper itemMapper;

	@Autowired
	private PromotionMapper promotionMapper;

	@Override
	public boolean genItemHtml(Long goodsId) {

		//直接得到
		Configuration configuration = freeMarkerConfigurer.getConfiguration();

		try {
			//获取到ftl里面的模板
			Template template = configuration.getTemplate("item.ftl");

			//new一个模板
			Map dataModel=new HashMap<>();
			Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goods", goods);
			GoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goodsDesc", goodsDesc);

			//获取面包屑
			String itemCat1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
			String itemCat2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
			String itemCat3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
			dataModel.put("itemCat1", itemCat1);
			dataModel.put("itemCat2", itemCat2);
			dataModel.put("itemCat3", itemCat3);

			//获取itemsku
			Item item=new Item();
			item.setStatus("1");
			item.setGoodsId(goodsId);
			List<Item> itemList=itemMapper.selectByExample3(item);
			dataModel.put("itemList", itemList);

			//获取促销信息
			List<Promotion> promotionList= promotionMapper.findListByGoodsId(goodsId);
			if(promotionList!=null && promotionList.size()>0){
				dataModel.put("promotionList", promotionList);

				//获取促销赠品
				List<Goods> giftList=new ArrayList<>();
				for (Promotion promotion : promotionList) {
					if(!promotion.getGift().isEmpty()){
						List<Goods> parseArray = JSON.parseArray(promotion.getGift(), Goods.class);
						giftList.addAll(parseArray);
					}
				}
				dataModel.put("giftList", giftList);
			}

			Writer out=new FileWriter(pagedir+goodsId+".html");
			template.process(dataModel, out);
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteItemHtml(String goodsId) {
		try {
			String[] split = goodsId.split(",");
			for (String string : split) {
				new File(pagedir+goodsId+".html").delete();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
