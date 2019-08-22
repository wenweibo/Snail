package com.cqkj.snail.buy.entity;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

/**
 * 字典实体
 *
 * @author 闻维波
 * @since 2019/08/13
 */
public class DictInfoEntity implements Comparable<DictInfoEntity>, Serializable {
    // 字典id
    private String id;
    // 创建人
    private String createUser;
    // 创建时间
    private String createTime;
    // 修改人
    private String updateUser;
    // 修改时间
    private String updateTime;
    // 父id
    private String parentId;
    // 字典名称
    private String dictName;
    // 名称拼音
    private String letter;
    // 字典编号
    private String dictCode;
    // 排序号
    private int orderNumber;
    // 子集
    private List<DictInfoEntity> children;
    // 是否选中，0--否，1--是
    private int selectFlag;


    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getLetter() {
        return letter;
    }

    public void setSelectFlag(int selectFlag) {
        this.selectFlag = selectFlag;
    }

    public int getSelectFlag() {
        return selectFlag;
    }

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

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setChildren(List<DictInfoEntity> children) {
        this.children = children;
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

    public String getParentId() {
        return parentId;
    }

    public String getDictName() {
        return dictName;
    }

    public String getDictCode() {
        return dictCode;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public List<DictInfoEntity> getChildren() {
        return children;
    }

    @Override
    public int compareTo(@NonNull DictInfoEntity o) {
        return this.getLetter().charAt(0) - o.getLetter().charAt(0);
    }
}
