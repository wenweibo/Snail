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
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        XUI.init(this);
//        XUI.debug(true);
    }
}
