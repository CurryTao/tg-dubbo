brandApp.service("promotionEditService",function($http){
	
	this.getPromotionTypeList=function(){
		return $http.get("../promotion/getPromotionTypeList");
	}
	this.save=function(entity){
		return $http.post("../promotion/add",entity);
	}
	this.update=function(entity){
		return $http.post("../promotion/update",entity);
	}
	this.del=function(selectIds){
		return $http.get("../promotion/del?ids="+selectIds);
	}
	this.updateStatus=function(selectIds,status){
		return $http.post("../promotion/updateStatus?status="+status,selectIds);
	}
	this.findPage=function(currentPage,pageSize,mohu){
		return $http.post("../promotion/findPage?currentPage="+currentPage+"&pageSize="+pageSize,mohu);
	}
	
});