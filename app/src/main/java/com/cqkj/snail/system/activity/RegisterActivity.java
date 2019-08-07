package com.cqkj.snail.system.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.cqkj.snail.R;
import com.cqkj.snail.truck.activity.TruckDetailActivity;
import com.fxkj.publicframework.activity.BaseTitleActivity;
import com.xuexiang.xui.utils.StatusBarUtils;

import butterknife.BindView;

/**
 * 注册页面
 * @author 闻维波 2019/07/30
 */
public class RegisterActivity extends BaseTitleActivity {
    @BindView(R.id.btn_register)
    Button btn_register;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setStatusBarLightMode(this);
        title_text.setText(R.string.register);
        back.setVisibility(View.VISIBLE);
        title_do.setVisibility(View.GONE);
    }

    @Override
    protected void initListener() {
        super.initListener();
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.btn_register:

                break;
        }
    }
}
