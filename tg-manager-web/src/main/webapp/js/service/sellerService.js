brandApp.service("sellerService",function($http){
    	this.findAll=function(){
    		return $http.get("/seller/list1")
    	}
    	this.findPage=function(currentPage,pageSize,mohu){
    		return $http.post("../seller/findPage?currentPage="+currentPage+"&pageSize="+pageSize,mohu);
    		
    	}
    	this.save=function(url,seller){
    		return $http.post(url,seller);
    		
    	}
    	this.del=function(selectIds){
    		return $http.get("/seller/del?ids="+selectIds);
    	}
    })