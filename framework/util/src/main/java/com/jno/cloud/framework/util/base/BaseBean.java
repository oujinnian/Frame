package com.jno.cloud.framework.util.base;

import com.alibaba.fastjson.JSON;

public class BaseBean {

    public String toString(){
        return JSON.toJSONString(this);
    }

}
