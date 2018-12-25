brandApp.service("itemCatService",function($http){
    	this.findAll=function(){
    		return $http.get("/itemCat/list1")
    	}
    	this.findAll1=function(){
    		return $http.get("/itemCat/list2")
    	}
    	this.findPage=function(currentPage,pageSize,mohu){
    		return $http.post("../itemCat/findPage?currentPage="+currentPage+"&pageSize="+pageSize,mohu);
    	}
    	this.save=function(url,itemCat){
    		return $http.post(url,itemCat);
    	}
    	this.del=function(selectIds){
    		return $http.get("/itemCat/del?ids="+selectIds);
    	}
    	this.ByParentId=function(parentId){
    		return $http.get("/itemCat/ByParentId?parentId="+parentId);
    	}
    	this.findOne=function(id){
    		return $http.get("/itemCat/findOne?id="+id);
    	}
    })