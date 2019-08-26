package com.cqkj.snail.system.entity;

import android.support.annotation.NonNull;

import com.cqkj.publicframework.tool.PinYinUtil;

import java.io.Serializable;
import java.util.List;

/**
 * 城市实体
 *
 * @author 闻维波
 * @since 2019/08/09 16:58
 */
public class CityEntity implements Serializable, Comparable<CityEntity>, Cloneable {
    // 城市id
    private String id;
    // 上一级id
    private String parentId;
    // 上一级名称
    private String parentName;
    // 城市编码
    private String adcode;
    // 城市名称
    private String name;
    // 城市名称拼音
    private String letters;
    // 地区级别
    private String areaLevel;
    // 下一级城市列表
    private List<CityEntity> children;
    // 选中标识 0:未选中，1：选中
    private int selectFlag;

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setSelectFlag(int selectFlag) {
        this.selectFlag = selectFlag;
    }

    public int getSelectFlag() {
        return selectFlag;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    public String getLetters() {
        return letters;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public void setName(String name) {
        this.name = name;
        setLetters(PinYinUtil.getFirstSpell(name));
    }

    public void setAreaLevel(String areaLevel) {
        this.areaLevel = areaLevel;
    }

    public void setChildren(List<CityEntity> children) {
        this.children = children;
    }

    public String getId() {
        return id;
    }

    public String getParentId() {
        return parentId;
    }

    public String getAdcode() {
        return adcode;
    }

    public String getName() {
        return name;
    }

    public String getAreaLevel() {
        return areaLevel;
    }

    public List<CityEntity> getChildren() {
        return children;
    }

    @Override
    public int compareTo(@NonNull CityEntity o) {
        return this.getLetters().charAt(0) - o.getLetters().charAt(0);
    }

}
