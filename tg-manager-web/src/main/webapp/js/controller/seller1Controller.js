brandApp.controller("seller1Controller",function($scope,$controller,seller1Service,$http){
		$controller('baseController',{$scope:$scope});
		$scope.findAll=function(){
			seller1Service.findAll().success(function(resp){
				$scope.list=resp;
			})
		};
		$scope.seller={};
		$scope.entity={status:{}};
		$scope.url="";
		$scope.save=function(status){
			$scope.entity.status=status;
			console.info($scope.entity);
			seller1Service.save($scope.url,$scope.entity).success(function(rep){
				if(rep){
					alert("成功");
					$scope.reloadList();
				}
			})
		};
		$scope.findPage=function(currentPage,pageSize,mohu){
			seller1Service.findPage(currentPage,pageSize,mohu).success(
				function(resp){
					$scope.list=resp.rows;
					$scope.myPage.totalItems=resp.total;
				});
			
		};
	
		$scope.findOne=function(id){
					$scope.url="/seller1/update";
				    $scope.entity= $scope.list[id];
		}
		$scope.clear=function(){
			$scope.seller1={};
			$scope.url="/seller1/add";
			
		}
		
		
		$scope.del=function(){
			seller1Service.del($scope.selectIds).success(function(resp){
				if(resp.status){
					alert("删除成功");
					$scope.reloadList();
				}else{
					alert("删除失败");
				}
			});
			
		}
		
		
		
		
		
		
	});