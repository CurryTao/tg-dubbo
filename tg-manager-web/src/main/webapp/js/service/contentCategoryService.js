brandApp.service("contentCategoryService",function($http){
    	this.findAll=function(){
    		return $http.get("/contentCategory/list1")
    	}
    	this.findPage=function(currentPage,pageSize,mohu){
    		return $http.post("../contentCategory/findPage?currentPage="+currentPage+"&pageSize="+pageSize,mohu);
    		
    	}
    	this.save=function(url,contentCategory){
    		return $http.post(url,contentCategory);
    		
    	}
    	this.del=function(selectIds){
    		return $http.get("/contentCategory/del?ids="+selectIds);
    	}
    })