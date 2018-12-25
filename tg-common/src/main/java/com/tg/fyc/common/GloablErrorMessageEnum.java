package com.tg.fyc.common;



import java.util.HashMap;
import java.util.Map;
/**
 * @desc 定义全局的错误码表，方便统一管理错误
 * @author luqian
 * @date 2018年3月16日10:57:44
 */
public enum GloablErrorMessageEnum {
    SUCCESS(200, "操作成功"),
    ERROR_PARAM_ILLEGAL(50000,"参数不合法"),
    ERROR_VALIDATECODE_ILLEGAL(50001,"验证码错误"),
    
    /**6开始的都是用户相关的*/
    ERROR_USER_PWD_NAME(60001,"用户名或密码错误"),
    ERROR_USER_LOGOUT(60002,"注销失败"),
    ERROR_PUSH_REDIS(60003,"用户删除失败"),
    ERROR_SYSTEM_ERROR(60004,"用户修改失败"),
    ERROR_SYSADD_ERROR(60005,"用户添加失败"),
   
    
    /**
     * 7开头都是文件相关的代码
     */
    ERROR_FILE_TYPE(70001,"文件类型不允许"),
    ERROR_FILE_DOWN(70002,"文件下载错误"),
    ERROR_FILE_UPLOAD(7003,"文件上传失败"),
    ERROR_FILE_EXCEL(7004,"excel导出错误"),
    /**
     * 8开头是数据库相关
     */
    ERROR_DB_FILE(80000,"数据库连接失败"),
    
    /**
     * 9开头的是规格
     */
    ERROR_GUIGE_SAVA(90000,"添加失败"),
    ERROR_GUIGE_UPDATE(90001,"修改失败"),
    
	/**
	 * 3开头的是模板
	 * 
	 */
    ERROR_MOBAN_SAVA(30000,"添加失败"),
	ERROR_MOBAN_UPDATE(30001,"修改失败"),
	/**
	 * 11开头的是商家入驻的
	 */
	
	 ERROR_SELLER_SAVA(110001,"注册失败"),
	ERROR_SELLER_UPDATE(110001,"修改状态失败");
	
    
    private Integer code;
    private String message;
    GloablErrorMessageEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    final  static Map<Integer,GloablErrorMessageEnum> enumMap = new HashMap<Integer,GloablErrorMessageEnum>();
    public static GloablErrorMessageEnum getDisabledByType(String code){
        if(enumMap.size() > 0){
            return enumMap.get(code);
        }
        GloablErrorMessageEnum[]gloablErrorMessageEnums =  GloablErrorMessageEnum.values();
        for (GloablErrorMessageEnum gloablErrorMessageEnum:gloablErrorMessageEnums){
            enumMap.put(gloablErrorMessageEnum.getCode(),gloablErrorMessageEnum);
        }
        return enumMap.get(code);
    }
}
