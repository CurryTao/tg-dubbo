brandApp.controller("itemController",function($scope,$http){
	
	$scope.addNum=function(x){
		$scope.num+=x;
		if($scope.num<1){
			$scope.num=1;
		}
	}
	
	$scope.specificationItems={};

	$scope.selectSpecification=function(key,value){
		$scope.specificationItems[key]=value;
		searchSku();
	}

	$scope.isSelected=function(key,value){
		if($scope.specificationItems[key]==value){
			return true;
		}else{
			return false;
		}		
	}

	$scope.sku={};

	$scope.loadSku=function(){
		$scope.sku=skuList[0];
		$scope.specificationItems=JSON.parse(JSON.stringify($scope.sku.spec));
	}

	matchObject=function(map1,map2){
		for(var k in map1){
			if(map1[k]!=map2[k]){
				return false;
			}
		}
		for(var k in map2){
			if(map2[k]!=map1[k]){
				return false;
			}
		}
		return true;
	}
	
	searchSku=function(){
		for(var i=0;i<skuList.length;i++){
			if(matchObject(skuList[i].spec,$scope.specificationItems)){
				$scope.sku=skuList[i];
				return ;
			}
		}
	}
	
	$scope.addToCart=function(){
		console.info($scope.num);
		$http.get("http://localhost:8086/cart/addGoodsToCartList?itemId="+$scope.sku.id+"&num="+$scope.num,{'withCredentials':true})
		.success(function(response){
			if(response.status) location.href="http://localhost:8086/cart.html";
			else alert(response.msg);
		});
	}
	
})