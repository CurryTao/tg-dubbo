package com.tg.fyc.common;

import java.io.Serializable;

/**
 * 前后台交互的规范
 * @author luqian
 *
 */
public class PageResult implements Serializable{
   
	private Integer code;//响应的状态码
	private Boolean status;//业务执行是否成功
	private String msg;//提示信息
	private Object data;//传给前台的数据
	
	public PageResult() {
	
	}
	
	public PageResult(Integer code, Boolean status, String msg, Object data) {
		super();
		this.code = code;
		this.status = status;
		this.msg = msg;
		this.data = data;
	}
	
	public static PageResult success(Object data){
		
		return new PageResult(200, true, "OK",data);
	}
	
	
    public static PageResult error(Integer code,String msg){
		
		return new PageResult(code, false, msg ,null);
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}
