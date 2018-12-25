brandApp.controller('contentController',function($scope,contentService){
	
	$scope.contentList=[];//光列表
	$scope.findByCategoryId=function(categoryId){
		contentService.findByCategoryId(categoryId).success(function(resp){
			$scope.contentList[categoryId]=resp;
			
		});
	}
	
	
	$scope.search=function(){
		location.href="http://localhost:8084/search.html#?keywords="+$scope.keywords;
		
	}
	
	
	
});