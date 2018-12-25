	brandApp.service("indexService",function($http){
		this.showName=function(){
			return $http.get("/indexlogin/showname");
			
		}
		
		
	})