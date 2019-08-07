package com.cqkj.snail.truck.entity;

/**
 * 卡车基本信息实体
 * @author 闻维波
 * @since 2019/08/02 14:22
 */
public class TruckBaseEntity {
    // 标题
    private String title;
    // 内容
    private String content;
    // 标识：false--无需显示叹号，true--显示叹号
    private boolean flag;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean isFlag() {
        return flag;
    }
}
