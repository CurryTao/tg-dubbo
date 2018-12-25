//服务层
app.service('userService',function($http){
	    	
	//增加 
	this.add=function(entity,smscode){
		return  $http.post('../user/register?smscode='+smscode,entity );
	}
	//发送验证码
	this.sendCode=function(phone){
		return $http.get('../user/sendCode?phone='+phone);
	}
	//显示登录用户名
	this.showName=function(){
		return $http.get('../user/showName');
	}
	
});
