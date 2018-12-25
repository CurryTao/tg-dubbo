brandApp.controller("indexController",function(indexService,$scope){
	
	 $scope.showName=function(){
		 indexService.showName().success(function(resp){
			 if(resp.data){
				 $scope.loginName=resp.data; 
			 }else{
				 alert(resp.msg);
			 }
		 });
	 }
	 
});