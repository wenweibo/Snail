package com.cqkj.snail.system.activity;

import android.support.design.widget.TextInputEditText;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cqkj.snail.R;
import com.fxkj.publicframework.activity.BaseTitleActivity;
import com.xuexiang.xui.utils.StatusBarUtils;

import butterknife.BindView;

/**
 * 登录页
 * @author 闻维波 2019/07/29
 */
public class LoginActivity extends BaseTitleActivity {
    //登录按钮
    @BindView(R.id.tv_login)
    Button tv_login;
    //用户名输入框
    @BindView(R.id.tedit_username)
    TextInputEditText tedit_username;
    //注册按钮
    @BindView(R.id.tv_register)
    TextView tv_register;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setStatusBarDarkMode(this);
        title_text.setText(getString(R.string.login));
        back.setVisibility(View.VISIBLE);
        title_do.setVisibility(View.GONE);
        //给“注册”文字添加下划线
        tv_register.setText(Html.fromHtml("<u>"+getString(R.string.login_register)+"</u>"));
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.tv_login:
                tedit_username.setError("你输入错了哈");
                break;
            case R.id.tv_register:
                startActivity(RegisterActivity.class);
                break;
        }
    }
}
