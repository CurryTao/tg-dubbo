 //控制层 
app.controller('userController' ,function($scope,$controller,userService){	
	
	$scope.entity={phone:'',password:'',username:''};
	
	//注册用户
	$scope.reg=function(){
		//比较两次输入的密码是否一致
		if($scope.password!=$scope.entity.password){
			alert("两次输入密码不一致，请重新输入");
			$scope.entity.password="";
			$scope.password="";
			return ;			
		}
		//新增
		userService.add($scope.entity,$scope.smscode).success(
			function(response){
				if(response.status) alert('注册成功');
				else alert(response.message);
			}		
		);
	}
    
	//发送验证码
	$scope.sendCode=function(){
		if($scope.entity.phone==null || $scope.entity.phone==""){
			alert("请填写手机号码");
			return ;
		}
		
		userService.sendCode($scope.entity.phone).success(
			function(response){
				if(response.status) alert("发送成功");
				else alert(response.message);
			}
		);		
	}
	//发送验证码
	$scope.showName=function(){
		userService.showName().success(
				function(response){
					$scope.loginName=response.loginName;
				}
		);		
	}
	
});	
