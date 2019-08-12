package com.cqkj.snail.system.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.cqkj.snail.R;
import com.cqkj.snail.requestdata.RequestManager;
import com.cqkj.snail.requestdata.RequestUrl;
import com.cqkj.publicframework.activity.BaseTitleActivity;
import com.cqkj.publicframework.beans.CallBackObject;
import com.cqkj.publicframework.tool.ToastUtil;
import com.cqkj.publicframework.tool.ValidTool;

import java.text.ParseException;
import java.util.HashMap;

import butterknife.BindView;

/**
 * 注册页面
 *
 * @author 闻维波 2019/07/30
 */
public class RegisterActivity extends BaseTitleActivity {
    // 用户名输入框
    @BindView(R.id.tedit_username)
    TextInputEditText teditUsername;
    // 手机号输入框
    @BindView(R.id.tedit_mobile)
    TextInputEditText teditMobile;
    // 密码输入框
    @BindView(R.id.tedit_password)
    TextInputEditText teditPassword;
    // 重复密码输入框
    @BindView(R.id.tedit_repeat_password)
    TextInputEditText teditRepeatPassword;
    // 注册按钮
    @BindView(R.id.btn_register)
    Button btn_register;


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
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        super.initView();
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
    public void onSuccess(int flag, CallBackObject obj) throws ParseException {
        super.onSuccess(flag, obj);
        switch (flag) {
            case RequestUrl.request_register:
                finish();
                ToastUtil.showShort(this, R.string.register_success);
                break;
        }
    }

    @Override
    public void onFailure(int flag, String message) {
        super.onFailure(flag, message);
        ToastUtil.showShort(this, message);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_register:
                String check_result = checkData();
                if (TextUtils.isEmpty(check_result)){
                    // 全部校验通过，则提交至服务器去注册
                    showDialog("");
                    register();
                }else{
                    ToastUtil.showShort(this,check_result);
                }
                break;
        }
    }
    @Override
    protected String checkData() {
        if (TextUtils.isEmpty(teditUsername.getText().toString())) {
            teditUsername.setError(getString(R.string.username_hint));
            return getString(R.string.username_hint);
        }
        String mobileNum = teditMobile.getText().toString();
        if (TextUtils.isEmpty(mobileNum)) {
            teditMobile.setError(getString(R.string.mobile_hint));
            return getString(R.string.mobile_hint);
        }
        if (!ValidTool.isMobile(mobileNum)) {
            teditMobile.setError(getString(R.string.mobile_hint2));
            return getString(R.string.mobile_hint2);
        }
        String password = teditPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            teditPassword.setError(getString(R.string.password_hint));
            return getString(R.string.password_hint);
        }
        if (!ValidTool.validPassword(password)) {
            teditPassword.setError(getString(R.string.password_hint2));
            return getString(R.string.password_hint2);
        }
        String repeatPassword = teditRepeatPassword.getText().toString();
        if (TextUtils.isEmpty(repeatPassword)) {
            teditRepeatPassword.setError(getString(R.string.password_repeat_hint));
            return getString(R.string.password_repeat_hint);
        }
        if (!password.equals(repeatPassword)) {
            return getString(R.string.password_unqequal);
        }
        return null;
    }

    /**
     * 注册
     */
    private void register() {
        HashMap<String, String> params = new HashMap<>();
        // 登录名
        params.put("loginName", teditUsername.getText().toString());
        // 	登录密码
        params.put("loginPassword", teditPassword.getText().toString());
        // 	联系方式
        params.put("phone", teditMobile.getText().toString());
        // 获取数据字典
        RequestManager.getRequestManager().post(RequestUrl.request_register, params, false, this);
    }
}
