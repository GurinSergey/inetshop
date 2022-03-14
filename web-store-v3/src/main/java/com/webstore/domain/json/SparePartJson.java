package com.webstore.domain.json;

/**
 * Created by SGurin on 21.07.2016.
 */
public class SparePartJson {
    private String title;
    private Long parentId;
    private int groupId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
