package com.tg.fyc.pojo.VO;

import java.io.Serializable;
import java.util.List;

import com.tg.fyc.pojo.OrderItem;
import com.tg.fyc.pojo.Promotion;

public class Cart implements Serializable{

	private static final long serialVersionUID = 5711608805889074000L;

	private String sellerId;
	private String sellerName;
	private List<OrderItem> orderItemList;
	private List<Promotion> promotionList;
	private Double giftCount;
	
	public Double getGiftCount() {
		return giftCount;
	}
	public void setGiftCount(Double giftCount) {
		this.giftCount = giftCount;
	}
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	public List<Promotion> getPromotionList() {
		return promotionList;
	}
	public void setPromotionList(List<Promotion> promotionList) {
		this.promotionList = promotionList;
	}
	
}
