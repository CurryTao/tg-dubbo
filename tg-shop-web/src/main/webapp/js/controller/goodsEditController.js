brandApp.controller("goodsEditController",function($scope,$controller,$location,goodsEditService,uploadService,itemCatService,typeTemplateService,$http){

	$controller('baseController',{$scope:$scope});

	$scope.entity={};
	$scope.goodsEdit={};
	$scope.url="";

	$scope.findAll=function(){
		goodsEditService.findAll().success(function(resp){
			$scope.list=resp;
		});
	};

	$scope.save=function(){
		$scope.entity.goodsDesc.introduction=editor.html();
		if($scope.entity.goods.id!=null){
			$scope.url="/goodsEdit/update";
		}else{
			$scope.url="/goodsEdit/add";
		}
		goodsEditService.save($scope.url,$scope.entity).success(function(rep){
			if(rep.status){
				alert("成功");
				$scope.entity={};
				editor.html("");
			}
		})
	};

	$scope.findPage=function(currentPage,pageSize,mohu){
		goodsEditService.findPage(currentPage,pageSize,mohu).success(
				function(resp){
					$scope.list=resp.rows;
					$scope.myPage.totalItems=resp.total;
				});
	};

	$scope.findOne=function(){
		var id=$location.search()['id'];
		if(id==null){
			return ;
		}
		goodsEditService.findOne(id).success(function(resp){
			$scope.entity=resp;
			//把值赋给editor.html();
			editor.html($scope.entity.goodsDesc.introduction);
			//转换成JSON对象
			//商品图片
			$scope.entity.goodsDesc.itemImages=JSON.parse($scope.entity.goodsDesc.itemImages);
			console.info($scope.entity.goodsDesc.itemImages);

			//扩展属性
			$scope.entity.goodsDesc.customAttributeItems=JSON.parse($scope.entity.goodsDesc.customAttributeItems);
			//规格选择
			$scope.entity.goodsDesc.specificationItems=JSON.parse($scope.entity.goodsDesc.specificationItems);

			for(var i=0;i< $scope.entity.itemList.length;i++){

				$scope.entity.itemList[i].spec=JSON.parse( $scope.entity.itemList[i].spec);
			}
		});
	}

	$scope.clear=function(){
		$scope.goodsEdit={};
		$scope.url="/goodsEdit/add";
	}

	$scope.del=function(){
		goodsEditService.del($scope.selectIds).success(function(resp){
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
				$scope.image_entity.url=resp.data;
			}else{
				alert(resp.msg);
			}
		});
	}

	$scope.entity={goodsDesc:{itemImages:[],specificationItems:[]}};

	//将当前上传图片的实体存入图片列表
	$scope.add_image_entity=function(){
		$scope.entity.goodsDesc.itemImages.push($scope.image_entity);
	}

	//删除图片
	$scope.del_image_entity=function(index){
		$scope.entity.goodsDesc.itemImages.splice(index,1);
	}

	$scope.selectItemCat1List=function(){
		itemCatService.ByParentId(0).success(function(resp){
			$scope.itemCat1List=resp.data;
		});
	}

	$scope.$watch('entity.goods.category1Id',function(newValue,oldValue){
		itemCatService.ByParentId(newValue).success(function(resp){
			$scope.itemCat2List=resp.data;
		});
	});
	$scope.$watch('entity.goods.category2Id',function(newValue,oldValue){
		itemCatService.ByParentId(newValue).success(function(resp){
			$scope.itemCat3List=resp.data;
		});
	});
	//读取模板ID
	$scope.$watch('entity.goods.category3Id',function(newValue,oldValue){
		itemCatService.findOne(newValue).success(function(resp){
			$scope.entity.goods.typeTemplateId=resp.data.typeId;
		});
	});
	
	//读取模板ID后，读取品牌列表,扩展属性，读取规格
	$scope.$watch('entity.goods.typeTemplateId',function(newValue,oldValue){
		typeTemplateService.findOne(newValue).success(function(resp){
			$scope.typeTemplate=resp.data;//模板对象
			//品牌类型转换
			$scope.typeTemplate.brandIds=JSON.parse($scope.typeTemplate.brandIds);
			//扩展属性把转换的给提交的值
			if($location.search()['id']==null){
				$scope.entity.goodsDesc.customAttributeItems= JSON.parse($scope.typeTemplate.customAttributeItems);
			}
		});
		//获取规格选项
		typeTemplateService.findSpecList(newValue).success(function(resp){
			$scope.speclist=resp;
		});
	});

	$scope.upadateSpecAttribute=function($event,name,value){
		var object=$scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems,'attributeName',name);
		if(object!=null){
			if($event.target.checked){
				object.attributeValue.push(value);
			}else{
				object.attributeValue.splice(object.attributeValue.indexOf(value),1);
				if(object.attributeValue.length==0){
					$scope.entity.goodsDesc.specificationItems.splice($scope.entity.goodsDesc.specificationItems.indexOf(object),1);
				}
				if(object.attributeName==null || object.attributeName=='') {
					entity.itemList=[];
				}
			}
		}else{
			$scope.entity.goodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[value]});
		}
	}

	$scope.createItemList=function(){
		//列表初始化
		$scope.entity.itemList=[{spec:{},price:0,num:9999,status:'0',isDefault:'0'}];
		var items=$scope.entity.goodsDesc.specificationItems;
		for(var i=0;i<items.length;i++){
			$scope.entity.itemList= addColumn($scope.entity.itemList,items[i].attributeName,items[i].attributeValue);
		}
	}

	addColumn=function(list,columnName,columnValues){
		var newList=[];
		for(var i=0;i<list.length;i++){
			var oldRow= list[i];
			for(var j=0;j<columnValues.length;j++){
				//进行深克隆
				var newRow=JSON.parse(JSON.stringify(oldRow));
				
				newRow.spec[columnName]=columnValues[j];
				newList.push(newRow);
			}
		}
		return newList;
	}

	$scope.itemCatList=[];

	$scope.findItemsCatList=function(){
		itemCatService.findAll1().success(function(resp){
			for(var i=0;i<resp.length;i++){
				$scope.itemCatList[resp[i].id]=resp[i].name;
			}
		})
	};

	$scope.checkAttributeValue=function(specName,optionName){
		var items=$scope.entity.goodsDesc.specificationItems;
		var object=$scope.searchObjectByKey(items,'attributeName',specName);
		if(object!=null){
			if(object.attributeValue.indexOf(optionName)>=0){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	$scope.tijiao=function(){
		goodsEditService.pltijiao($scope.selectIds).success(function(resp){
			if(resp.status){
				alert("申请成功");
				$scope.reloadList();
			}
		});
	}

});