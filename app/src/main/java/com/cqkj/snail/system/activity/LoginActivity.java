package com.cqkj.snail.system.activity;

import android.support.design.widget.TextInputEditText;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cqkj.snail.R;
import com.cqkj.snail.requestdata.RequestUrl;
import com.cqkj.snail.tool.CommonUtil;
import com.cqkj.publicframework.activity.BaseTitleActivity;
import com.cqkj.publicframework.beans.CallBackObject;
import com.cqkj.publicframework.tool.ToastUtil;
import com.cqkj.publicframework.tool.ValidTool;
import com.xuexiang.xui.utils.StatusBarUtils;

import java.text.ParseException;

import butterknife.BindView;

/**
 * 登录页
 * @author 闻维波 2019/07/29
 */
public class LoginActivity extends BaseTitleActivity {
    //登录按钮
    @BindView(R.id.tv_login)
    Button tvLogin;
    //用户名输入框
    @BindView(R.id.tedit_username)
    TextInputEditText teditUsername;
    //密码输入框
    @BindView(R.id.tedit_password)
    TextInputEditText teditPassword;
    //注册按钮
    @BindView(R.id.tv_register)
    TextView tvRegister;
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
        tvRegister.setText(Html.fromHtml("<u>"+getString(R.string.login_register)+"</u>"));
    }

    @Override
    protected void initListener() {
        super.initListener();
        tvLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.tv_login:
                // 检验表单数据
                String checkResult = checkData();
                // 如果通过，则提交至服务器进行登录
                if (TextUtils.isEmpty(checkResult)){
                    showDialog("");
                    CommonUtil.login(teditUsername.getText().toString(),
                            teditPassword.getText().toString(), this);
                }else{
                    // 否则弹出提示
                    ToastUtil.showShort(this,checkResult);
                }
                break;
            case R.id.tv_register:
                startActivity(RegisterActivity.class);
                break;
        }
    }

    @Override
    protected String checkData() {
        // 校验用户名
        if (TextUtils.isEmpty(teditUsername.getText().toString())){
            teditUsername.setError(getString(R.string.username_hint));
            return getString(R.string.username_hint);
        }
        if (!ValidTool.validPassword(teditPassword.getText().toString())){
            teditPassword.setError(getString(R.string.password_hint2));
            return getString(R.string.password_hint2);
        }
        return null;
    }

    @Override
    public void onSuccess(int flag, CallBackObject obj) throws ParseException {
        super.onSuccess(flag, obj);
        switch (flag){
            case RequestUrl.request_do_login:
                ToastUtil.showShort(this, R.string.login_success);
                finish();
                break;
        }
    }

    @Override
    public void onFailure(int flag, String message) {
        super.onFailure(flag, message);
        ToastUtil.showShort(this, message);
    }
}
