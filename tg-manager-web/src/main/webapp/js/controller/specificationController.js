brandApp.controller("specificationController",function($scope,$controller,specificationService,$http){
		$controller('baseController',{$scope:$scope});
		$scope.findAll=function(){
			specificationService.findAll().success(function(resp){
				$scope.list=resp;
			})
			
		};
		$scope.specification={};
		$scope.url="";
		$scope.save=function(){
			console.info($scope.entity);
			specificationService.save($scope.url,$scope.entity).success(function(rep){
				if(rep.status){
					alert("成功");
					$scope.reloadList();
				}
				
			})
			
		};
		$scope.findPage=function(currentPage,pageSize,mohu){
			specificationService.findPage(currentPage,pageSize,mohu).success(
				function(resp){
					$scope.list=resp.rows;
					$scope.myPage.totalItems=resp.total;
				});
			
		};
		$scope.findOne=function(id){
					$scope.url="/specification/update";
					$scope.entity.specification=$scope.list[id];
					
					specificationService.find($scope.entity.specification.id).success(function(data){
						console.info(data);
						if(data!=null){
							$scope.entity.specificationOptionList=data.rows;
							console.info($scope.entity.specificationOptionList=data.rows);
							
						}else{
							$scope.entity.specificationOptionList=[];
						}
						
					});
					
				    
		}
		
		$scope.clear=function(){
			$scope.brand={};
			$scope.url="/specification/add";
			
		}
		
		$scope.del=function(){
			alert($scope.selectIds);
			console.info($scope.selectIds);
			specificationService.del($scope.selectIds).success(function(resp){
				if(resp.status){
					alert("删除成功");
					$scope.reloadList();
				}else{
					alert("删除失败");
				}
			});
			
		};
		
		$scope.add=function(){
			$scope.url="/specification/add";
			$scope.entity.specification={};
			$scope.entity={specificationOptionList:[]};
			
		};
		//定义optionlist数组
		$scope.entity={specificationOptionList:[]};
		$scope.addrows=function(){
			$scope.entity.specificationOptionList.push({});
		}
		
		$scope.delrows=function(index){
			$scope.entity.specificationOptionList.splice(index,1)
		}
		
		
		
	});