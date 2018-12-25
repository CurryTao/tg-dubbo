brandApp.controller("baseController",function($scope){

	//分页助手
	$scope.myPage={
			currentPage:1,
			totalItems:10,//总记录数
			itemsPerPage:5,//分页
			perPageOptions:[5,10,15,20],//可选择的每页展示多少条数据
			onChange:function(){
				$scope.reloadList();
			}
	};

	//刷新页面数据
	$scope.mohu={};
	$scope.reloadList=function(){
		$scope.findPage($scope.myPage.currentPage,$scope.myPage.itemsPerPage,$scope.mohu);
	};

	//复选框事件将选中的对象的id放到sellectId数组中
	$scope.selectIds=[];
	$scope.shanchu=function($event,id){
		if($event.target.checked){
			$scope.selectIds.push(id);
		}else{
			var index=$scope.selectIds.indexOf(id);
			$scope.selectIds.splice(index,1);
		}
		console.info($scope.selectIds);
	}

	//根据key查对象
	$scope.searchObjectByKey=function(list,key,keyValue){
		for(var i=0;i<list.length;i++){
			if(list[i][key]==keyValue){
				return list[i]; 
			}
		}
		return null;
	}

	//将json字符串（json数组【json对象】）根据键返回值的字符串
	$scope.JsonMethod=function(text,key){
		var arr=[];
		var text=JSON.parse(text);
		for(i in text){
			arr.push(text[i][key]);
		}
		return arr.join(",");
	}

})
