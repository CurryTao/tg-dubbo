brandApp.controller('searchController',function($scope,$location,searchService){
	
	$scope.contentList=[];//光列表
	//定义默认的存储结构
	$scope.searchMap={'keywords':'','category':'','brand':'','spec':{},'price':'','pageNo':1,'pageSize':40,'sort':'','sortField':''};
	
	$scope.search=function(){
		$scope.searchMap.pageNo=parseInt($scope.searchMap.pageNo);
		searchService.search($scope.searchMap).success(function(resp){
			$scope.resultMap=resp;
			
			buildPageLabel();
		});
	}
	
	buildPageLabel=function(){
		//构建分页栏
		$scope.pageLabel=[];
		var firstPage=1;
		var lastPage=$scope.resultMap.totalPages;
		
		$scope.firstDot=true;
		$scope.lastDot=true;
		
		if($scope.resultMap.totalPages>5){
			//前五页
			if($scope.searchMap.pageNo<=3){
				lastPage=5;
				$scope.firstDot=false;
			}else if($scope.searchMap.pageNo>= $scope.resultMap.totalPages-2){
				//后五
				firstPage=$scope.resultMap.totalPages-4;
				$scope.lastDot=false;
			}else{
				firstPage=$scope.searchMap.pageNo-2;
				lastPage=$scope.searchMap.pageNo+2;
			}
		}else{
			$scope.firstDot=false;
			$scope.lastDot=false;
		}
		
		for(var i=firstPage;i<=lastPage;i++){
			$scope.pageLabel.push(i);
		}
	}
	
	//点击分页数
	$scope.queryByPage=function(pageNo){
		//判断条件
		if(pageNo<1 || pageNo>$scope.resultMap.totalPages){
			return ;
		}
//		if(pageNo<1){
//			pageNo=1;
//		}
		
		$scope.searchMap.pageNo=pageNo;
		$scope.search();
	}
	
	
	
	
	$scope.addSearchItem=function(key,value){
		//判断是不是类型和品牌
		if(key=='category' || key=='brand' || key=='price'){
			$scope.searchMap[key]=value;
			
		}else{
			$scope.searchMap.spec[key]=value;
		}
		$scope.search();
	}
	
	$scope.removeSearchItem=function(key){
		if(key=='category' || key=='brand' || key=='price'){
			$scope.searchMap[key]="";
			
		}else{
			delete $scope.searchMap.spec[key];
		}
		$scope.search();
	}
	
	$scope.isToPage=function(){
		if($scope.searchMap.pageNo==1){
			return true;
		}else{
			return false;
		}
	}
	$scope.isEndPage=function(){
		if($scope.searchMap.pageNo==$scope.resultMap.totalPages){
			return true;
		}else{
			return false;
		}
	}
	
	$scope.sortSearch=function(sortField,sort){
		//赋值给变量
		$scope.searchMap.sortField=sortField;
		$scope.searchMap.sort=sort;
		//调用方法
		$scope.search();
		
	}
	$scope.keywordsIsBrand=function(){
		
		for(var i=0;i<$scope.resultMap.brandList.length;i++){
			if($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text)>=0){
				return true;
			}
		}
		return false;
		
	}
	//跳过来
	$scope.loadkeywords=function(){
		
		$scope.searchMap.keywords= $location.search()['keywords'];
		$scope.search();
	}
	
	
	
	
	
});