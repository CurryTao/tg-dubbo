 //控制层 
app.controller('payController' ,function($scope,payService,$location){	
	
	$scope.createNative=function(){
		payService.createNative().success(
			function(response){
				$scope.money=  (response.total_fee/100).toFixed(2) ;//金额
				$scope.out_trade_no= response.out_trade_no;//订单号
				//二维码
		    	var qr = new QRious({
		 		   element:document.getElementById('qrious'),
		 		   size:250,
		 		   level:'H',
		 		   value:response.code_url
		 		});	
		    	$scope.queryPayStatus();
			}
		);
	}
	
	$scope.queryPayStatus=function(){
		payService.queryPayStatus($scope.out_trade_no).success(
				function(response){
					console.info(response);
					if(response.status){
						location.href="paysuccess.html#?money="+$scope.money;
					}else{		
						if(response.msg=='二维码超时'){
							alert(response.msg);
							location.href="payfail.html";
						}else{
							location.href="payfail.html";
						}								
					}		
				}
		);
	}
	
	$scope.initMoney=function(){
		return $location.search()['money'];
	}
});	
