//服务层
app.service('addressService',function($http){
	    	
	this.findCartList=function(){
		return $http.get('../address/findListByLoginUser');
	}
	
});
