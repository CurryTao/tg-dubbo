package com.tg.fyc.cart.api;

import java.util.List;

import com.tg.fyc.pojo.VO.Cart;

public interface CartApi {

	public List<Cart> addGoodsToCartList(List<Cart> cartList,Long itemId,Integer num );
	
	public void savaCartListToRedis(List<Cart> cartList,String username);
	
	public List<Cart> findCartListFromRedis(String username);
	
	public List<Cart> mergeCartList(List<Cart> cartList1,List<Cart> cartList2);
}
