package com.tg.fyc.pojo;

import java.io.Serializable;
import java.util.List;

public class GoodsDeitVO implements Serializable{
	private Goods goods;
	private GoodsDesc goodsDesc;
	private List<Item> itemList;
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	public GoodsDesc getGoodsDesc() {
		return goodsDesc;
	}
	public void setGoodsDesc(GoodsDesc goodsDesc) {
		this.goodsDesc = goodsDesc;
	}
	public List<Item> getItemList() {
		return itemList;
	}
	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}
	
}
