package com.jno.cloud.framework.util.result;

import com.jno.cloud.framework.util.base.BaseBean;
import lombok.Data;

@Data
public class Result extends BaseBean {

    public final static int SUCCESS = 0;   //操作成功
    public final static int ERROR = 99; //操作失败
    public final static int OS_ERROR = 3;//系统错误
//    final static int BUSINESS_ERROR = 1;//业务错误
//    final static int AUTHENTICATION_ERROR = 2;//认证错误

    private int code;   //状态码
    private String msg; //说明
    private Object data;//返回数据
//    private int status; //状态

    public Result (){
        this.code = ERROR; //未知错误
        this.msg = "操作失败";
    }

    //快捷返回错误
    public Result (String msg){
        this.code = ERROR; //未知错误
        this.msg = msg;
    }

    public Result (int code, Object data, String msg){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


}
