brandApp.service("brandService",function($http){
    	this.findAll=function(){
    		return $http.get("/brand/list1")
    	}
    	this.findPage=function(currentPage,pageSize,mohu){
    		return $http.post("../brand/findPage?currentPage="+currentPage+"&pageSize="+pageSize,mohu);
    		
    	}
    	this.save=function(url,brand){
    		return $http.post(url,brand);
    		
    	}
    	this.del=function(selectIds){
    		return $http.get("/brand/del?ids="+selectIds);
    	}
    })