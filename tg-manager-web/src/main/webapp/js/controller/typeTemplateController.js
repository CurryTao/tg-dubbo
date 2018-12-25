brandApp.controller("typeTemplateController",function($scope,brandService,specificationService,$controller,typeTemplateService,$http){
		$controller('baseController',{$scope:$scope});
		$scope.findAll=function(){
			typeTemplateService.findAll().success(function(resp){
				$scope.list=resp;
			})
			
		};
		$scope.typeTemplate={};
		$scope.url="";
		$scope.save=function(){
			console.info($scope.entity);
			typeTemplateService.save($scope.url,$scope.entity).success(function(rep){
				if(rep.status){
					alert("成功");
					$scope.reloadList();
				}
				
			})
		};
		$scope.findPage=function(currentPage,pageSize,mohu){
			typeTemplateService.findPage(currentPage,pageSize,mohu).success(
				function(resp){
					$scope.list=resp.rows;
					$scope.myPage.totalItems=resp.total;
				});
			
		};
		$scope.findOne=function(id){
					$scope.url="/typeTemplate/update";
					$scope.entity.id=$scope.list[id].id;
					$scope.entity.name=$scope.list[id].name;
					$scope.entity.specIds= JSON.parse($scope.list[id].specIds);
					$scope.entity.brandIds= JSON.parse($scope.list[id].brandIds);
					$scope.entity.customAttributeItems= JSON.parse($scope.list[id].customAttributeItems);
					
		}
		
		$scope.clear=function(){
			$scope.brand={};
			$scope.url="/typeTemplate/add";
			
		}
		
		$scope.del=function(){
			console.info($scope.selectIds);
			typeTemplateService.del($scope.selectIds).success(function(resp){
				if(resp.status){
					alert("删除成功");
					$scope.reloadList();
				}else{
					alert("删除失败");
				}
			});
			
		};
		
		$scope.add=function(){
			$scope.url="/typeTemplate/add";
			$scope.entity={customAttributeItems:[]};
			
		};
		
//		$scope.up11=function(){
//			$scope.entity.brandIds = {
//		        data: [{id:1,text:'bug'},{id:2,text:'duplicate'},{id:3,text:'invalid'},{id:4,text:'wontfix'}]
//		        // 其他配置略，可以去看看内置配置中的ajax配置
//		    }; 	
//			
//			
//		}
		$scope.JsonMethod=function(text,key){
				var arr=[];
				var text=JSON.parse(text);
				for(i in text){
					arr.push(text[i][key]);
				}
			return arr.join(",");
		}
		
		$scope.initBrandAndSepecifi=function(){
			brandService.findAll().success(function(resp){
				$scope.brandIds={data:resp};
			});
			specificationService.findAll().success(function(resp){
				$scope.specIds={data:resp};
				
			});
		}
		$scope.entity={customAttributeItems:[]};
		$scope.addrows=function(){
			$scope.entity.customAttributeItems.push({});
		}
		
		$scope.delrows=function(index){
			$scope.entity.customAttributeItems.splice(index,1)
		}
		
		
		
	});