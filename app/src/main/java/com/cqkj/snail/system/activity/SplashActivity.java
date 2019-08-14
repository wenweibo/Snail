package com.cqkj.snail.system.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.cqkj.publicframework.beans.CallBackObject;
import com.cqkj.publicframework.requestdata.CallBack;
import com.cqkj.publicframework.tool.SpUtils;
import com.cqkj.snail.R;
import com.cqkj.snail.config.DictInfo;
import com.cqkj.snail.requestdata.RequestUrl;
import com.cqkj.snail.tool.CommonRequest;
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
        CommonRequest.getCitys(this);

    }

    /**
     * 默认登录
     */
    private void autoLogin() {
        // 获取用户名和密码
        String loginName = SpUtils.getStringParam(this, "loginName", "");
        String loginPassword = SpUtils.getStringParam(this, "loginPassword", "");
        // 如果存储了用户名密码，则去默认登录
        if (!TextUtils.isEmpty(loginName) && !TextUtils.isEmpty(loginPassword)) {
            CommonUtil.login(loginName, loginPassword, this);
        } else {
            // 否则，直接跳转到主页
            startToMain();
        }

    }


    @Override
    public void onSuccess(int flag, CallBackObject obj) throws ParseException {
        callbackDo(flag, obj);
    }


    @Override
    public void onFailure(int flag, String message) {
        switch (flag) {
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
        switch (flag) {
            case RequestUrl.request_area:
                autoLogin();
                break;
            case RequestUrl.request_do_login:
                startToMain();
                break;
        }
    }
    /**
     * 请求回调后的处理
     * @param flag
     * @param obj
     */
    private void callbackDo(int flag, CallBackObject obj) {
        switch (flag) {
            case RequestUrl.request_area:
                // 获取城市数据成功后，去获取车型字典
                CommonRequest.getDictByCode(DictInfo.VEHICLE_TYPE, this);
                break;
            case RequestUrl.request_do_login:
                // 登录成功，跳转到主页
                startToMain();
                break;
            case RequestUrl.request_dictIfo:
                //获取数据字典后的返回
                HashMap params = obj.getParams();
                String dictCode = (String) params.get("dictCode");
                if (DictInfo.VEHICLE_TYPE.equals(dictCode)) {
                    // 获取车型成功后，去获取车源品牌字典
                    CommonRequest.getDictByCode(DictInfo.VEHICLE_BRAND, this);
                } else if (DictInfo.VEHICLE_BRAND.equals(dictCode)) {
                    // 获取车源品牌字典成功后，去获取价格字典
                    CommonRequest.getDictByCode(DictInfo.PRICE, this);
                } else if (DictInfo.PRICE.equals(dictCode)) {
                    // 获取价格字典成功后，去获取排放标准字典
                    CommonRequest.getDictByCode(DictInfo.EMISSION_STANDARD, this);
                } else if (DictInfo.EMISSION_STANDARD.equals(dictCode)) {
                    // 获取排放标准字典成功后，去获取行驶里程字典
                    CommonRequest.getDictByCode(DictInfo.MILEAGE, this);
                } else if (DictInfo.MILEAGE.equals(dictCode)) {
                    // 获取行驶里程字典成功后，去获取发布状态字典
                    CommonRequest.getDictByCode(DictInfo.PUBLICATION_STATUS, this);
                } else if (DictInfo.PUBLICATION_STATUS.equals(dictCode)) {
                    // 获取发布状态字典成功后，去获取排序条件字典
                    CommonRequest.getDictByCode(DictInfo.SORT_KIND, this);
                } else if (DictInfo.SORT_KIND.equals(dictCode)) {
                    // 获取发布状态字典成功后，去自动登录
                    autoLogin();
                }
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


}
