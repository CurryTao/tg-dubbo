brandApp.service("seller1Service",function($http){
    	this.findAll=function(){
    		return $http.get("/seller1/list1")
    	}
    	this.findPage=function(currentPage,pageSize,mohu){
    		return $http.post("../seller1/findPage?currentPage="+currentPage+"&pageSize="+pageSize,mohu);
    		
    	}
    	this.save=function(url,seller){
    		return $http.post(url,seller);
    		
    	}
    	this.del=function(selectIds){
    		return $http.get("/seller1/del?ids="+selectIds);
    	}
    })