	
brandApp.controller("baseController",function($scope){
	
		//助手
	$scope.myPage={
			currentPage:1,
			totalItems:10,//总记录数
			itemsPerPage:5,//分页
			perPageOptions:[5,10,15,20],//可选择的每页展示多少条数据
			onChange:function(){
				$scope.reloadList();
		     }
	  };
	
	
	
	
	$scope.mohu={};
	
	$scope.reloadList=function(){
		$scope.findPage($scope.myPage.currentPage,$scope.myPage.itemsPerPage,$scope.mohu);
	};
	
	$scope.selectIds=[];
	$scope.shanchu=function($event,id){
		 if($event.target.checked){
			 $scope.selectIds.push(id);
			 
		 }else{
			 var index=$scope.selectIds.indexOf(id);
			 $scope.selectIds.splice(index,1);
			 
		 }
		
	}
	
	
})
	