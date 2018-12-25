brandApp.controller("sellerController",function($scope,$controller,sellerService,$http){
		$controller('baseController',{$scope:$scope});
		$scope.findAll=function(){
			sellerService.findAll().success(function(resp){
				$scope.list=resp;
			})
			
		};
		$scope.seller={};
		$scope.url="";
		$scope.save=function(){
			sellerService.save($scope.url,$scope.seller).success(function(rep){
				if(rep){
					alert("成功");
					$scope.reloadList();
				}
				
			})
			
		};
		$scope.findPage=function(currentPage,pageSize,mohu){
			sellerService.findPage(currentPage,pageSize,mohu).success(
				function(resp){
					$scope.list=resp.rows;
					$scope.myPage.totalItems=resp.total;
				});
			
		};
	
		$scope.findOne=function(id){
					$scope.url="/seller/update";
				    $scope.seller.id = $scope.list[id].id;
				    $scope.seller.name = $scope.list[id].name;
				    $scope.seller.firstChar = $scope.list[id].firstChar;
			
		}
		
		$scope.clear=function(){
			$scope.seller={};
			$scope.url="/seller/add";
			
		}
		
		$scope.del=function(){
			sellerService.del($scope.selectIds).success(function(resp){
				if(resp.status){
					alert("删除成功");
					$scope.reloadList();
				}else{
					alert("删除失败");
				}
			});
			
		}
		
	});