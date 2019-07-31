package com.cqkj.snail.system.activity;

import android.view.View;

import com.cqkj.snail.R;
import com.fxkj.publicframework.activity.BaseTitleActivity;


/**
 * 卖车页
 * @author 闻维波 2019/07/26
 */
public class SellCarActivity extends BaseTitleActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_sell_car;
    }

    @Override
    protected void initView() {
        super.initView();
        back.setVisibility(View.GONE);
        title_do.setVisibility(View.GONE);
        title_text.setText(R.string.sys_notice);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    public void onClick(View view) {

    }
    private long exitTime = 0;

    @Override
    public void onBackPressed() {

    }
}
