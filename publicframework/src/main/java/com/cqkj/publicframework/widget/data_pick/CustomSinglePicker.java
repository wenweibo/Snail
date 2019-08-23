package com.cqkj.publicframework.widget.data_pick;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cqkj.publicframework.R;

import java.util.List;

/**
 * 自定义单一滚轮选择控件
 *
 * @author 闻维波
 * @since 2019/08/21 09:20
 */
public class CustomSinglePicker {
    // 标题
    private TextView tv_title;
    // 取消按钮
    private TextView tv_cancle;
    // 确定按钮
    private TextView tv_select;
    // 数据滚轮选择器
    private DatePickerView data_pv;

    /**
     * 定义结果回调接口
     */
    public interface ResultHandler {
        void handle(int position, String text);
    }

    private ResultHandler handler;
    private Context context;
    // 弹窗
    private Dialog pickerDialog;

    private List<String> datas;

    public CustomSinglePicker(Context context, List<String> datas, ResultHandler handler) {
        this.context = context;
        this.datas = datas;
        this.handler = handler;
        initDialog();
        initView();
        data_pv.setData(datas);
    }

    /**
     * 初始化弹窗
     */
    private void initDialog() {
        if (pickerDialog == null) {
            pickerDialog = new Dialog(context, R.style.time_dialog);
            pickerDialog.setCancelable(true);
            pickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pickerDialog.setContentView(R.layout.custom_single_picker);
            Window window = pickerDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = dm.widthPixels;
            window.setAttributes(lp);
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        tv_title = (TextView) pickerDialog.findViewById(R.id.tv_title);
        tv_cancle = (TextView) pickerDialog.findViewById(R.id.tv_cancle);
        tv_select = (TextView) pickerDialog.findViewById(R.id.tv_select);
        data_pv = (DatePickerView) pickerDialog.findViewById(R.id.data_pv);

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickerDialog.dismiss();
            }
        });

        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.handle(data_pv.getmCurrentSelected(), data_pv.getCurrentStr());
                pickerDialog.dismiss();
            }
        });
    }
    public CustomSinglePicker setTitle(String title){
        tv_title.setText(title);
        return this;
    }


    public void show() {
        pickerDialog.show();
    }

}
