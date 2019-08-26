package com.cqkj.snail;

import android.app.Application;
import android.content.Context;

import com.cqkj.snail.system.entity.CityEntity;
import com.cqkj.snail.system.entity.UserEntity;
import com.xuexiang.xui.XUI;

import java.util.List;

public class AppApplication extends Application {
    public static Context context;
    // 用户信息
    public static UserEntity userEntity;
    // 城市列表
    public static List<CityEntity> cityEntities;
    // 选中的城市
    public static CityEntity selectCity;
    // 选中的城市(多选存储列表)
    public static List<CityEntity> selectCitys;
    // 当前定位的城市
    public static CityEntity locationCity;
    // 搜索关键字
    public static String keyword;
    // 买车页面加载标记，-1：从未加载，其他：已加载过
    public static int buyCarloadFlag = -1;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        XUI.init(this);
//        XUI.debug(true);
    }
}
