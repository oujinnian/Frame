package com.jno.cloud.framework.util.base;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor  //无参数构造器
public class TreeBean extends BaseBean {

    private String id;  //主键
    private String text;//节点显示名称
    private String parentId;    //父节点主键
    private String state;   //选中状态
    private List<TreeBean> children;    //子节点


    public TreeBean(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public TreeBean(String id, String text, String parentId) {
        this.id = id;
        this.text = text;
        this.parentId = parentId;
    }



}
