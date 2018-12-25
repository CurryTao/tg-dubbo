brandApp.service("goodsEditService",function($http){
	this.findAll=function(){
		return $http.post("/goodsEdit/findAll");
	}
	this.findPage=function(currentPage,pageSize,mohu){
		return $http.post("../goodsEdit/findPage?currentPage="+currentPage+"&pageSize="+pageSize,mohu);
	}
	this.save=function(url,goodsEdit){
		return $http.post(url,goodsEdit);
	}
	this.del=function(selectIds){
		return $http.get("/goodsEdit/del?ids="+selectIds);
	}
	this.findOne=function(id){
		return $http.get("/goodsEdit/findOne?id="+id);
	}
	this.pltijiao=function(id){
		return $http.get("/goodsEdit/pltijiao?ids="+id);
	}

});