package com.tg.fyc.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.tg.fyc.cart.api.CartApi;
import com.tg.fyc.common.CookieUtil;
import com.tg.fyc.common.PageResult;
import com.tg.fyc.pojo.VO.Cart;

@RestController
@RequestMapping("cart")
public class CartController {

	@Autowired
	private CartApi cartApi;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HttpServletResponse response;
	
	@RequestMapping("/findCartList")
	public List<Cart> findCartList(){
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		String cartListString = CookieUtil.getCookieValue(request, "cartList","UTF-8");
		if(cartListString==null || cartListString.equals("")) cartListString="[]";
		List<Cart> cartList_cookie = JSON.parseArray(cartListString, Cart.class);
		
		if(username.equals("anonymousUser")){//用户以匿名用户（未登录）
			return cartList_cookie;
		}else{//用户登录，从redis中读取购物车数据
			List<Cart> findCartListFromRedis = this.cartApi.findCartListFromRedis(username);
			
			if(cartList_cookie.size()>0){
				findCartListFromRedis=this.cartApi.mergeCartList(findCartListFromRedis, cartList_cookie);
				CookieUtil.deleteCookie(request, response, "cartList");
				this.cartApi.savaCartListToRedis(findCartListFromRedis, username);
			}
			
			return findCartListFromRedis;
		}
		
	}
	
	@RequestMapping("/addGoodsToCartList")
	@CrossOrigin(origins="http://localhost:8087",allowCredentials="true")
	public PageResult addGoodsToCartList(Long itemId,Integer num){
		
		//跨域操作，定义头信息， 等同于origins="http://localhost:8087" (可要求指定 的路径可访问）
		//response.setHeader("Access-Control-Allow-Origin", "http://localhost:8087");
		
		//可以跨域操作cookie信息 等同于 allowCredentials="true" (默认为true)
		//response.setHeader("Access-Control-Allow-Credentials", "true");
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		try {
			
			List<Cart> cartList =findCartList();
			cartList = cartApi.addGoodsToCartList(cartList, itemId, num);
			
			if(username.equals("anonymousUser")){//未登录
				CookieUtil.setCookie(request, response, "cartList", JSON.toJSONString(cartList),3600*24,"UTF-8");
			}else{//已登录
				this.cartApi.savaCartListToRedis(cartList, username);
			}
			
			return PageResult.success(null);
		} catch (Exception e) {
			e.printStackTrace();
			return PageResult.error(500, "添加失败");
		}
	}	
}