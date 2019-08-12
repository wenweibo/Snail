package com.cqkj.publicframework.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cqkj.publicframework.R;
import com.cqkj.publicframework.adapter.CommonAdapter;
import com.cqkj.publicframework.tool.CommonUtil;

/**
 * 底部菜单弹窗
 * @author 闻维波
 * @since 2019/08/07 11:16
 */
public class BottomPop extends PopupWindow {
    // 标题视图
    private TextView tv_title;
    // 列表视图
    private WListView lv_content;
    // 主内容视图
    private LinearLayout lin_content;
    // 按钮视图
    private Button btn_cancel;

    // 标题
    private String title;
    // 按钮标题
    private String btnStr;
    // 按钮点击监听
    private View.OnClickListener btnClick;
    // 列表
//    private List<T> list;
    // 内容视图
    private View content_View;
    // 列表适配器
    private BaseAdapter commonAdapter;



    private Context context;
    private View pView;
    public BottomPop(Context context, String title) {
        super(context);
        this.context = context;
        this.title = title;
    }

    /**
     * 设置按钮标题
     * @param btnStr
     * @param btnClick
     */
    public BottomPop setBtnMsg(String btnStr,View.OnClickListener btnClick) {
        this.btnStr = btnStr;
        this.btnClick = btnClick;
        return this;
    }

    public BottomPop setContent_View(View content_View) {
        this.content_View = content_View;
        return this;
    }

    public BottomPop setListMsg(CommonAdapter commonAdapter) {
        this.commonAdapter = commonAdapter;
        return this;
    }

    public void init(){
        // 初始化视图
        pView = LayoutInflater.from(context)
                .inflate(R.layout.pop_bottom, null);
        setContentView(pView);
        tv_title = pView.findViewById(R.id.tv_title);
        lv_content = pView.findViewById(R.id.lv_content);
        lv_content.setScroll(false);
        lin_content = pView.findViewById(R.id.lin_content);
        btn_cancel = pView.findViewById(R.id.btn_cancel);
        // 设置标题
        if (!TextUtils.isEmpty(title)){
            tv_title.setText(title);
        }
        // 设置主视图
        if (content_View != null){
            lin_content.addView(content_View);
        }
        // 设置列表视图
        if (commonAdapter != null){
            lv_content.setAdapter(commonAdapter);
        }
        // 设置按钮标题及点击事件
        if (!TextUtils.isEmpty(btnStr) && btnClick != null){
            btn_cancel.setText(btnStr);
            btn_cancel.setOnClickListener(btnClick);
        }else{
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        // 设置底部弹出属性
        setWidth(CommonUtil.getScreen(context)[0]);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.pop_bottom);
        CommonUtil.backgroundAlpha(context, 0.5f);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                CommonUtil.backgroundAlpha(context, 1f);
            }
        });


    }
}
