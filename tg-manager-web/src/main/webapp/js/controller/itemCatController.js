brandApp.controller("itemCatController",function($scope,$controller,brandService,itemCatService,$http){
		$controller('baseController',{$scope:$scope});
		$scope.findAll=function(){
			itemCatService.findAll().success(function(resp){
				$scope.list=resp;
			})
			
		};
		$scope.entity={};
		$scope.entity={parentId:""};
		$scope.url="";
		
		$scope.save=function(){
			$scope.entity.parentId=$scope.mohu.id;
			if($scope.entity.typeIds!=null) $scope.entity.typeId=$scope.entity.typeIds.id;
			console.info($scope.entity);
			itemCatService.save($scope.url,$scope.entity).success(function(rep){
				if(rep.status){
					alert("成功");
					$scope.reloadList();
				}
			})
		};
		
		$scope.findPage=function(currentPage,pageSize,mohu){
			itemCatService.findPage(currentPage,pageSize,mohu).success(
				function(resp){
					$scope.list=resp.rows;
					$scope.myPage.totalItems=resp.total;
				});
		};
	
		$scope.findOne=function(id){
					$scope.url="/itemCat/update";
				    $scope.entity = $scope.list[id];
				    console.info($scope.entity);
		}
		
		$scope.clear=function(){
			$scope.entity={};
			$scope.url="/itemCat/add";
			
		}
		
		//删除方法
		$scope.del=function(){
			console.info($scope.selectIds);
			itemCatService.del($scope.selectIds).success(function(resp){
				if(resp.status){
					alert("删除成功");
					$scope.reloadList();
				}else{
					alert("删除失败");
				}
			});
		}
		$scope.initBrandAndSepecifi=function(){
			itemCatService.findAll().success(function(resp){
				$scope.typeId={data:resp};
			});
			
		}
		$scope.findByParentId=function(parentId){
			$scope.mohu.id=parentId;
			
			$scope.reloadList();
		}
		
		
		// 面包屑查询下级列表的方法
	    //（直接传对象而不是id，方便导航栏获取对应菜单名称）
	    $scope.selectList = function(p_entity) {
	        // 如果为1级目录,设置2级，3级菜单为空
	        if ($scope.grade == 1) {
	            $scope.entity_1 = null;
	            $scope.entity_2 = null;
	        }
	        ;
	        // 如果为2级菜单,将一级目录保存到上级
	        if ($scope.grade == 2) {
	            $scope.entity_1 = p_entity;
	            $scope.entity_2 = null;
	        };

	        // 如果为3级菜单,将2级目录保存到上级
	        if ($scope.grade == 3) {
	            $scope.entity_2 = p_entity;
	        };
	        //调用查询下级列表
	        $scope.findByParentId(p_entity.id);

	    };
	 // 初始化grade菜单等级为1
	    $scope.grade = 1;
	    // 提供设置grade菜单等级的方法
	    $scope.setGrade = function(value) {

	        $scope.grade = value;
	    };
		
	});