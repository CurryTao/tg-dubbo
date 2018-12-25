brandApp.service("typeTemplateService",function($http){
    	this.findAll=function(){
    		return $http.get("/typeTemplate/list1")
    	}
    	this.findPage=function(currentPage,pageSize,mohu){
    		return $http.post("../typeTemplate/findPage?currentPage="+currentPage+"&pageSize="+pageSize,mohu);
    		
    	}
    	this.save=function(url,entity){
    		return $http.post(url,entity);
    		
    	}
    	this.del=function(selectIds){
    		alert(selectIds);
    		return $http.get("/typeTemplate/del?ids="+selectIds);
    	}
    	this.find=function(id){
    		return $http.post("/typeTemplate/findgui?id="+id);
    	}
    	
    })