brandApp.service("specificationService",function($http){
    	this.findAll=function(){
    		return $http.get("/specification/list1")
    	}
    	this.findPage=function(currentPage,pageSize,mohu){
    		return $http.post("../specification/findPage?currentPage="+currentPage+"&pageSize="+pageSize,mohu);
    		
    	}
    	this.save=function(url,entity){
    		return $http.post(url,entity);
    		
    	}
    	this.del=function(selectIds){
    		alert(selectIds);
    		return $http.get("/specification/del?ids="+selectIds);
    	}
    	this.find=function(id){
    		return $http.post("/specification/findgui?id="+id);
    	}
    	
    })