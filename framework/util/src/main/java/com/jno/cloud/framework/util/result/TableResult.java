package com.jno.cloud.framework.util.result;

import lombok.Data;

@Data
public class TableResult extends Result {

    private long count; //总行数

    //未知错误
    public TableResult (){
        super();
    }

    public TableResult (String msg){
        super(msg);
    }

    public TableResult (int code, Object data, String msg, long count){
        super(code,data,msg);
        this.count = count;
    }

}
