brandApp.service("contentService",function($http){
    	this.findAll=function(){
    		return $http.get("/content/list1")
    	}
    	this.findPage=function(currentPage,pageSize,mohu){
    		return $http.post("../content/findPage?currentPage="+currentPage+"&pageSize="+pageSize,mohu);
    		
    	}
    	this.save=function(url,content){
    		return $http.post(url,content);
    		
    	}
    	this.del=function(selectIds){
    		return $http.get("/content/del?ids="+selectIds);
    	}
    })