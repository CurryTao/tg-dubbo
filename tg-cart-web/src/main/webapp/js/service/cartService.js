app.service('cartService',function($http){
	    	
	this.findCartList=function(){
		return $http.get('../cart/findCartList');
	}
	this.addGoodsToCartList=function(itemId,num){
		return $http.post('../cart/addGoodsToCartList?itemId='+itemId+"&num="+num);		
	}
	this.toAddOrder=function(order){
		return $http.post('../order/toAddOrder',order);		
	}
	this.sum=function(cartList){
		var totalValue={totalNum:0, totalMoney:0.00,giftNum:0,lessprice:0.00};//合计实体
		for(var i=0;i<cartList.length;i++){
			var cart=cartList[i];
			for(var j=0;j<cart.orderItemList.length;j++){
				var orderItem=cart.orderItemList[j];//购物车明细
				totalValue.totalNum+=orderItem.num;
				totalValue.totalMoney+= orderItem.totalFee;
			}
			for(var j=0;j<cart.promotionList.length;j++){
				var promotion=cart.promotionList[j];//促销商品减价和赠品
				totalValue.lessprice+=promotion.lessprice;
				//totalValue.giftNum+=promotion.giftCount;
			}
			console.info(cart);
		}
		totalValue.totalMoney -= totalValue.lessprice;
		return totalValue;
	}
	
});
