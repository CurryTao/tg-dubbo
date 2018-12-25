brandApp.controller("promotionEditController",function($scope,$controller,$location,promotionEditService,uploadService,itemCatService,typeTemplateService,goodsEditService){

	$controller('baseController',{$scope:$scope});
	
	$scope.entity={id:'',gift:[],goodsIds:'',starttime:'',endtime:''};
	$scope.giftList=[];
	$scope.goodsList=[];
	$scope.temp={id:'',goodsName:''};
	
	$scope.initData=function(){
		promotionEditService.getPromotionTypeList().success(function(resp){
			$scope.promotionTypeList=resp;
		});
		goodsEditService.findAll().success(function(resp){
			$scope.itemlist=resp;
		});
	};
	$scope.changeGift=function($event,index){
		if($event.target.checked){
			$scope.giftList.push($scope.itemlist[index]);
		}else{
			var index1=$scope.giftList.indexOf($scope.itemlist[index]);
			$scope.giftList.splice(index1,1);
		}
	};
	$scope.changeGoods=function($event,index){
		if($event.target.checked){
			$scope.goodsList.push($scope.itemlist[index]);
		}else{
			var index1=$scope.goodsList.indexOf($scope.itemlist[index]);
			$scope.goodsList.splice(index1,1);
		}
	};
	$scope.save=function(){
		for(var i=0;i<$scope.promotionTypeList.length;i++){
			if($scope.entity.typeid==$scope.promotionTypeList[i].id){
				$scope.entity.typename=$scope.promotionTypeList[i].name;
				break;
			}
		}
		for(var i=0;i<$scope.giftList.length;i++){
			$scope.temp.id=$scope.giftList[i].id;
			$scope.temp.goodsName=$scope.giftList[i].goodsName;
			$scope.entity.gift.push($scope.temp);
		}
		for(var i=0;i<$scope.goodsList.length;i++){
			$scope.entity.goodsIds += $scope.goodsList[i].id+',';
		}
		copyTime();
		if($scope.entity.id==null || $scope.entity.id==''){
			console.info($scope.entity);
			promotionEditService.save($scope.entity).success(function(resp){
				if(resp=="true") {
					alert("添加促销商品成功");
					location.href="promotion.html";
				}else{
					alert("添加促销商品失败，请重试！");
				}
			});
		}else{
			promotionEditService.update($scope.entity).success(function(resp){
				if(resp=="true") {
					alert("修改促销商品成功");
					location.href="promotion.html";
				}else{
					alert("修改促销商品失败，请重试！");
				}
			});
		}
	};

	$scope.findPage=function(currentPage,pageSize,mohu){
		promotionEditService.findPage(currentPage,pageSize,mohu).success(
				function(resp){
					$scope.list=resp.rows;
					$scope.myPage.totalItems=resp.total;
				});
	};

	$scope.del=function(){
		promotionEditService.del($scope.selectIds).success(function(resp){
			if(resp.status){
				alert("删除成功");
				$scope.reloadList();
			}else{
				alert("删除失败");
			}
		});
	};

	$scope.updateStatus=function(status){
		promotionEditService.updateStatus($scope.selectIds,status).success(function(resp){
			if(resp.status){
				alert("申请成功");
				$scope.reloadList();
			}
		});
	}

	copyTime=function(){
		$scope.entity.starttime=$("#starttime").val();
		$scope.entity.endtime=$("#endtime").val();
	}
});