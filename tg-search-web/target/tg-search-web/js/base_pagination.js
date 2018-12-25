 var brandApp=angular.module('brandApp',['pagination']);
 
 //定义一个过滤器一个过滤器只能解决一个 如果需要可以多定义
 brandApp.filter('trustHtml',['$sce',function($sce){
	 return function(data){
		 
		 return $sce.trustAsHtml(data);
	 }
 }]);