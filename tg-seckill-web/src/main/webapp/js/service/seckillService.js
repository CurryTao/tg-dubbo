//服务层
app.service('seckillService',function($http){
	    	
	this.findList=function(){
		return $http.get('goods/findList');		
	}	
	
	this.findOne=function(id){
		return $http.get('goods/findOneFromRedis?id='+id);		
	}
	
	this.submitOrder=function(seckillId){
		return $http.get('order/submitOrder?seckillId='+seckillId);
	}
	
});
