brandApp.controller("contentController",function($scope,$controller,contentService,uploadService,contentCategoryService,$http){
		$controller('baseController',{$scope:$scope});
		$scope.findAll=function(){
			contentService.findAll().success(function(resp){
				$scope.list=resp;
			})
			
		};
		$scope.entity={};
		$scope.url="";
		$scope.save=function(){
			
			contentService.save($scope.url,$scope.entity).success(function(rep){
				if(rep){
					alert("成功");
					$scope.reloadList();
				}
				
			})
			
		};
		$scope.findPage=function(currentPage,pageSize,mohu){
			contentService.findPage(currentPage,pageSize,mohu).success(
				function(resp){
					$scope.list=resp.rows;
					$scope.myPage.totalItems=resp.total;
				});
			
		};
		
	
		
		
	
		$scope.findOne=function(id){
					$scope.url="/content/update";
				    $scope.entity= $scope.list[id];
				    console.info($scope.entity);
		}
		
		$scope.clear=function(){
			$scope.entity={};
			$scope.url="/content/add";
			
		}
		
		
		$scope.del=function(){
			contentService.del($scope.selectIds).success(function(resp){
				if(resp.status){
					alert("删除成功");
					$scope.reloadList();
				}else{
					alert("删除失败");
				}
			});
			
		}
		
		//图片上传
		$scope.uploadFile=function(){
			uploadService.uploadFile().success(function(resp){
				if(resp.status){
					$scope.entity.pic=resp.data;
				}else{
					alert(resp.msg);
				}
			});
		}
		$scope.contentCategoryListAll=function(){
			contentCategoryService.findAll().success(function(resp){
				$scope.contentCategoryLis=resp;
				
			});
			
		}
		$scope.status=['无效','有效'];
		
		
		
		
		
		
	});