app.controller('cartController' ,function($scope,$controller,cartService){	

	$scope.cartList=[];

	$scope.findCartList=function(){
		cartService.findCartList().success(
				function(response){
					$scope.cartList=response;
					$scope.totalValue=cartService.sum($scope.cartList);//求合计数
				}
		);
	}

	$scope.addGoodsToCartList=function(itemId,num){
		cartService.addGoodsToCartList(itemId,num).success(
				function(response){
					if(response.status) $scope.findCartList();
					else alert(response.msg);
				}
		);		
	}

});	
