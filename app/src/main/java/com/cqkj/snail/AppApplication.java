package com.cqkj.snail;

import android.app.Application;

import com.xuexiang.xui.XUI;

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        XUI.init(this);
//        XUI.debug(true);
    }
}
