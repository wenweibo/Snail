package com.cqkj.snail.system.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.cqkj.publicframework.beans.CallBackObject;
import com.cqkj.publicframework.requestdata.CallBack;
import com.cqkj.publicframework.tool.SpUtils;
import com.cqkj.snail.R;
import com.cqkj.snail.requestdata.RequestManager;
import com.cqkj.snail.requestdata.RequestUrl;
import com.cqkj.snail.tool.CommonUtil;

import java.text.ParseException;
import java.util.HashMap;

/**
 * 闪屏页
 *
 * @author wwb
 * @since 2019/01/08
 */
public class SplashActivity extends AppCompatActivity implements CallBack {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getCitys();

    }

    private void autoLogin() {
        // 获取用户名和密码
        String loginName = SpUtils.getStringParam(this, "loginName", "");
        String loginPassword = SpUtils.getStringParam(this, "loginPassword", "");
        // 如果存储了用户名密码，则去默认登录
        if (!TextUtils.isEmpty(loginName) && !TextUtils.isEmpty(loginPassword)){
            CommonUtil.login(loginName, loginPassword, this);
        }else{
            // 否则，直接跳转到主页
            startToMain();
        }

    }



    @Override
    public void onSuccess(int flag, CallBackObject obj) throws ParseException {
        switch (flag){
            case RequestUrl.request_area:
                // 获取城市数据成功后，去自动登录
                autoLogin();
                break;
            case RequestUrl.request_do_login:
                // 登录成功，跳转到主页
                startToMain();
                break;
        }
    }

    @Override
    public void onFailure(int flag, String message) {
        switch (flag){
            case RequestUrl.request_area:
                // 获取城市数据失败后，也要去自动登录
                autoLogin();
                break;
            case RequestUrl.request_do_login:
                startToMain();
                break;
        }

    }

    @Override
    public void onNoData(int flag) {
        switch (flag){
            case RequestUrl.request_area:
                autoLogin();
                break;
            case RequestUrl.request_do_login:
                startToMain();
                break;
        }
    }

    /**
     * 跳转到主页
     */
    private void startToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /**
     * 获取城市数据
     */
    public void getCitys() {
        HashMap<String, String> params = new HashMap<>();
        RequestManager.getRequestManager().get(RequestUrl.request_area, params, false, this);
    }

}
