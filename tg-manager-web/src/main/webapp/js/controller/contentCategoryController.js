brandApp.controller("contentCategoryController",function($scope,$controller,contentCategoryService,$http){
		$controller('baseController',{$scope:$scope});
		$scope.findAll=function(){
			contentCategoryService.findAll().success(function(resp){
				$scope.list=resp;
			})
			
		};
		$scope.entity={};
		$scope.url="";
		$scope.save=function(){
			contentCategoryService.save($scope.url,$scope.entity).success(function(rep){
				if(rep){
					alert("成功");
					$scope.reloadList();
				}
				
			})
			
		};
		$scope.findPage=function(currentPage,pageSize,mohu){
			contentCategoryService.findPage(currentPage,pageSize,mohu).success(
				function(resp){
					$scope.list=resp.rows;
					$scope.myPage.totalItems=resp.total;
				});
			
		};
		
	
		
		
	
		$scope.findOne=function(id){
					$scope.url="/contentCategory/update";
				    $scope.entity= $scope.list[id];
			
		}
		
		$scope.clear=function(){
			$scope.entity={};
			$scope.url="/contentCategory/add";
			
		}
		
		
		$scope.del=function(){
			contentCategoryService.del($scope.selectIds).success(function(resp){
				if(resp.status){
					alert("删除成功");
					$scope.reloadList();
				}else{
					alert("删除失败");
				}
			});
			
		}
		
		
		
		
		
		
	});