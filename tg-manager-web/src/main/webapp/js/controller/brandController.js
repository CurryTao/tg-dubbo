brandApp.controller("brandController",function($scope,$controller,brandService,$http){
		$controller('baseController',{$scope:$scope});
		$scope.findAll=function(){
			brandService.findAll().success(function(resp){
				$scope.list=resp;
			})
			
		};
		$scope.brand={};
		$scope.url="";
		$scope.save=function(){
			brandService.save($scope.url,$scope.brand).success(function(rep){
				if(rep){
					alert("成功");
					$scope.reloadList();
				}
				
			})
			
		};
		$scope.findPage=function(currentPage,pageSize,mohu){
			brandService.findPage(currentPage,pageSize,mohu).success(
				function(resp){
					$scope.list=resp.rows;
					$scope.myPage.totalItems=resp.total;
				});
			
		};
		
	
		
		
	
		$scope.findOne=function(id){
					$scope.url="/brand/update";
				    $scope.brand.id = $scope.list[id].id;
				    $scope.brand.name = $scope.list[id].name;
				    $scope.brand.firstChar = $scope.list[id].firstChar;
			
		}
		
		$scope.clear=function(){
			$scope.brand={};
			$scope.url="/brand/add";
			
		}
		
		
		$scope.del=function(){
			brandService.del($scope.selectIds).success(function(resp){
				if(resp.status){
					alert("删除成功");
					$scope.reloadList();
				}else{
					alert("删除失败");
				}
			});
			
		}
		
		
		
		
		
		
	});