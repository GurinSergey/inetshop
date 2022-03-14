package com.webstore.core.entities.json;

/**
 * Created by SGurin on 21.07.2016.
 */
public class SparePartJson {
    private String title;
    private int parentId;
    private int groupId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
