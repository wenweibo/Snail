package com.cqkj.snail.system.activity;

import android.os.Bundle;
import android.view.View;

import com.cqkj.snail.R;
import com.cqkj.publicframework.activity.BaseTitleActivity;
import com.cqkj.publicframework.beans.CallBackObject;

import java.text.ParseException;


/**
 * 买车页
 * @author 闻维波 2019/07/26
 */
public class BuyCarActivity extends BaseTitleActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
//        setBack(false);
        return R.layout.activity_buy_car;
    }

    @Override
    protected void initView() {
        super.initView();
        title_text.setText(getString(R.string.buy_car));
        back.setVisibility(View.GONE);
        title_do.setVisibility(View.GONE);
    }

    @Override
    protected void initListener() {
        super.initListener();

    }

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

    @Override
    protected void loadData() {
        super.loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onSuccess(int flag, CallBackObject obj) throws ParseException {
        super.onSuccess(flag, obj);
    }

    @Override
    public void onFailure(int flag, String message) {
        super.onFailure(flag, message);
    }

    @Override
    public void onBackPressed() {

    }

}
