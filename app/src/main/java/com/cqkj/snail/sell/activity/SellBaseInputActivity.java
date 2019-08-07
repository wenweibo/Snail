package com.cqkj.snail.sell.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cqkj.snail.R;
import com.fxkj.publicframework.activity.BaseTitleActivity;

import butterknife.BindView;

/**
 * 买车基本信息输入
 * @author 闻维波
 * @since 2019/08/05 09:35
 */
public class SellBaseInputActivity extends BaseTitleActivity {
    // 看车地点
    @BindView(R.id.lin_place)
    LinearLayout linPlace;
    // 添加图片按钮
    @BindView(R.id.iv_add_pic)
    ImageView ivAddPic;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_sell_base_input;
    }

    @Override
    protected void initListener() {
        super.initListener();
        linPlace.setOnClickListener(this);
        ivAddPic.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        super.initView();
        title_text.setText(R.string.input_msg);
        back.setVisibility(View.VISIBLE);
        title_do.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.lin_place:
                break;
            case R.id.iv_add_pic:
                startActivity(UploadActivity.class);
                break;
        }
    }
}
