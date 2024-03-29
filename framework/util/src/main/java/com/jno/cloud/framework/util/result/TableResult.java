package com.jno.cloud.framework.util.result;

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


    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
