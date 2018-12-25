 //控制层 
app.controller('addressController' ,function($scope,addressService,cartService){	
	
	$scope.order={paymentType:'1'};	
	
	$scope.findCartList=function(){
		cartService.findCartList().success(
			function(response){
				$scope.cartList=response;
				$scope.totalValue=cartService.sum($scope.cartList);//求合计数
			}
		);
	}
	
	$scope.init=function(){
		addressService.findCartList().success(
			function(response){
				$scope.addressList=response;
				for(var i=0;i<$scope.addressList.length;i++){
					if($scope.addressList[i].isDefault=='1'){
						$scope.address=$scope.addressList[i];
						break;
					}
				}
			}
		);		
	}
	
	$scope.selectAddress=function(address){
		$scope.address=address;
	}
	
	$scope.isSelectAddress=function(address){
		if(address==$scope.address) return true;
		else return false;
	}
	
	$scope.toAddOrder=function(){
		$scope.order.receiverAreaName=$scope.address.address;//地址
		$scope.order.receiverMobile=$scope.address.mobile;//手机
		$scope.order.receiver=$scope.address.contact;//联系人
		cartService.toAddOrder($scope.order).success(
				function(response){
					if(response.status) {
						if($scope.order.paymentType=="1"){
							location.href="pay.html";
							return ;
						}
						else location.href="paysuccess.html";
					}
					else alert(response.msg);
				}
		);
	}
	
});	
