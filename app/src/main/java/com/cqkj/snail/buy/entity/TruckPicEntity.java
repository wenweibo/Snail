package com.cqkj.snail.buy.entity;

import java.io.Serializable;

/**
 * 货车图片实体
 */
public class TruckPicEntity implements Serializable {
    // 图片id
    private String id;
    private String createUser;
    private String createTime;
    private String updateUser;
    private String updateTime;
    // 图片真实名称
    private String realName;
    // 图片保存名称
    private String saveName;
    // 图片地址
    private String savePath;
    private String attachSize;

    public void setId(String id) {
        this.id = id;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setSaveName(String saveName) {
        this.saveName = saveName;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public void setAttachSize(String attachSize) {
        this.attachSize = attachSize;
    }

    public String getId() {
        return id;
    }

    public String getCreateUser() {
        return createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getRealName() {
        return realName;
    }

    public String getSaveName() {
        return saveName;
    }

    public String getSavePath() {
        return savePath;
    }

    public String getAttachSize() {
        return attachSize;
    }
}
