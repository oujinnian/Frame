package com.jno.cloud.framework.util.base;

import java.util.List;

public class TreeBean extends BaseBean {

    private String id;  //主键
    private String text;//节点显示名称
    private String parentId;    //父节点主键
    private String state;   //选中状态
    private List<TreeBean> children;    //子节点

    public TreeBean() {
    }

    public TreeBean(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public TreeBean(String id, String text, String parentId) {
        this.id = id;
        this.text = text;
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<TreeBean> getChildren() {
        return children;
    }

    public void setChildren(List<TreeBean> children) {
        this.children = children;
    }
}
