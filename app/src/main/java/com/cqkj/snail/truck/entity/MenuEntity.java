package com.cqkj.snail.truck.entity;

/**
 * 菜单实体
 */
public class MenuEntity {
    // 菜单id
    private String id;
    // 图片资源id
    private int imgRes;
    // 图片资源id:选中
    private int imgResSelected;
    // 菜单标题
    private String title;
    // 是否选中
    private boolean Selected;

    public void setSelected(boolean selected) {
        Selected = selected;
    }

    public boolean isSelected() {
        return Selected;
    }

    public void setImgResSelected(int imgResSelected) {
        this.imgResSelected = imgResSelected;
    }

    public int getImgResSelected() {
        return imgResSelected;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getImgRes() {
        return imgRes;
    }

    public String getTitle() {
        return title;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
