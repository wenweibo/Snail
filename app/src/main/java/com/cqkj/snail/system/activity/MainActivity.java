package com.cqkj.snail.system.activity;

import android.Manifest;
import android.app.ActivityGroup;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.cqkj.publicframework.activity.BaseActivityManager;
import com.cqkj.publicframework.tool.ToastUtil;
import com.cqkj.snail.AppApplication;
import com.cqkj.snail.R;
import com.cqkj.snail.buy.activity.BuyCarActivity;
import com.cqkj.snail.sell.activity.SellCarActivity;
import com.xuexiang.xui.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 主页
 *
 * @author wwb 2019/07/25
 */
public class MainActivity extends ActivityGroup {
    @BindView(R.id.view_tab_host)
    TabHost tabHost;
    @BindView(R.id.ll_bottom_tabs)
    LinearLayout ll_bottom_tabs;

    //底部标检图标集合
    private ArrayList<Map<Integer, Integer>> botResList;
    private RelativeLayout tab1, tab2, tab3, tab4;
    private List<RelativeLayout> tabRelativeLayouts = new ArrayList<RelativeLayout>();
    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    //消息提示小红点
    private ImageView img_remind_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        StatusBarUtils.setStatusBarDarkMode(this);
        requestPermissions(permissions);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initUI();
    }


    /**
     * 初始化界面元素
     */
    private void initUI() {
        initRes();
        initBottomUI();
    }

    private void initRes() {
        // 初始化底部图片资源文件
        botResList = new ArrayList<Map<Integer, Integer>>();
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(0, R.mipmap.guide_home_nm);
        map.put(1, R.mipmap.guide_home_on);
        botResList.add(map);
        map = new HashMap<Integer, Integer>();
        map.put(0, R.mipmap.guide_tfaccount_nm);
        map.put(1, R.mipmap.guide_tfaccount_on);
        botResList.add(map);
        map = new HashMap<Integer, Integer>();
        map.put(0, R.mipmap.guide_discover_nm);
        map.put(1, R.mipmap.guide_discover_on);
        botResList.add(map);
        map = new HashMap<Integer, Integer>();
        map.put(0, R.mipmap.guide_account_nm);
        map.put(1, R.mipmap.guide_account_on);
        botResList.add(map);

        // 加载底部Tab布局
        LayoutInflater inflater = LayoutInflater.from(this);
        // “首页”标签
        tab1 = (RelativeLayout) inflater.inflate(R.layout.action_item, null);
        ImageView icon1 = (ImageView) tab1.findViewById(R.id.icon);
        icon1.setImageResource(R.mipmap.guide_home_on);
        icon1.setTag(getString(R.string.first_pager));
        TextView tvTilte1 = tab1.findViewById(R.id.tv_title);
        tvTilte1.setText(R.string.first_pager);
        tvTilte1.setTextColor(getResources().getColor(R.color.colorPrimary));
        ImageView img_remind = (ImageView) tab1.findViewById(R.id.img_remind);
        img_remind.setVisibility(View.GONE);
        tabRelativeLayouts.add(tab1);

        // “买车”标签
        tab2 = (RelativeLayout) LayoutInflater.from(this).inflate(
                R.layout.action_item, null);
        ImageView icon2 = (ImageView) tab2.findViewById(R.id.icon);
        icon2.setImageResource(R.mipmap.guide_tfaccount_nm);
        icon2.setTag(getString(R.string.buy_car));
        TextView tvTilte2 = tab2.findViewById(R.id.tv_title);
        tvTilte2.setText(R.string.buy_car);
        img_remind_msg = (ImageView) tab2.findViewById(R.id.img_remind);
        img_remind_msg.setVisibility(View.VISIBLE);
        tabRelativeLayouts.add(tab2);
        // "卖车"标签
        tab3 = (RelativeLayout) LayoutInflater.from(this).inflate(
                R.layout.action_item, null);
        ImageView icon3 = (ImageView) tab3.findViewById(R.id.icon);
        icon3.setImageResource(R.mipmap.guide_discover_nm);
        icon3.setTag(getString(R.string.sell_car));
        ((TextView) tab3.findViewById(R.id.tv_title)).setText(getString(R.string.sell_car));
        img_remind = (ImageView) tab3.findViewById(R.id.img_remind);
        img_remind.setVisibility(View.GONE);
        tabRelativeLayouts.add(tab3);

        // “个人”标签
        tab4 = (RelativeLayout) LayoutInflater.from(this).inflate(
                R.layout.action_item, null);
        ImageView icon4 = (ImageView) tab4.findViewById(R.id.icon);
        icon4.setImageResource(R.mipmap.guide_account_nm);
        icon4.setTag(getString(R.string.mine));
        TextView tvTilte4 = tab4.findViewById(R.id.tv_title);
        tvTilte4.setText(R.string.mine);
        img_remind = (ImageView) tab4.findViewById(R.id.img_remind);
        img_remind.setVisibility(View.GONE);
        tabRelativeLayouts.add(tab4);
        // 加载TabSpec
        tabHost.setup(getLocalActivityManager());

    }

    /**
     * 第一页
     */
    private TabHost.TabSpec ts1;

    /**
     * 初始化底部按钮
     */
    private void initBottomUI() {
        tabHost.clearAllTabs();
        ts1 = tabHost
                .newTabSpec(getString(R.string.first_pager));
        ts1.setIndicator(tab1);// 这句话就是设置每个小tab显示的内容
        ts1.setContent(new Intent(this, FirstPagerActivity.class));
        tabHost.addTab(ts1);

        TabHost.TabSpec ts2 = tabHost.newTabSpec(getString(R.string.buy_car));
        ts2.setIndicator(tab2);
        Intent intent = new Intent(this, BuyCarActivity.class);
        ts2.setContent(intent);
        tabHost.addTab(ts2);

        TabHost.TabSpec ts3 = tabHost.newTabSpec(getString(R.string.sell_car));
        ts3.setIndicator(tab3);
        Intent intent3 = new Intent(this, SellCarActivity.class);
        intent3.putExtra("showReturn", 1);
        ts3.setContent(intent3);
        tabHost.addTab(ts3);

        TabHost.TabSpec ts4 = tabHost.newTabSpec(getString(R.string.mine));
        ts4.setIndicator(tab4);
        Intent intent4 = new Intent(this, MineActivity.class);
        ts4.setContent(intent4);
        tabHost.addTab(ts4);
        tabHost.setOnTabChangedListener(new TabChange());
        tab1.setOnClickListener(new TabOnClick(0));
        tab2.setOnClickListener(new TabOnClick(1));
        tab3.setOnClickListener(new TabOnClick(2));
        tab4.setOnClickListener(new TabOnClick(3));
    }

    class TabOnClick implements View.OnClickListener {
        private int position;

        public TabOnClick(int position) {
            super();
            this.position = position;
        }

        public void onClick(View v) {
            // 点击的是“我的”或“卖车”且是未登录状态，则跳转到登录页面
            if ((position == 3 || position == 2) && AppApplication.userEntity == null) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            } else {
                // 否则打开对应的标签页
                tabHost.setCurrentTab(position);
            }
        }
    }

    /**
     * 页卡切换监听
     */
    class TabChange implements TabHost.OnTabChangeListener {

        @Override
        public void onTabChanged(String tabId) {
            Log.e("TAG", tabId);
            for (int i = 0; i < tabRelativeLayouts.size(); i++) {
                RelativeLayout relativeLayout = tabRelativeLayouts.get(i);
                RelativeLayout linearLayout = (RelativeLayout) relativeLayout
                        .getChildAt(0);
                ImageView imageView = (ImageView) linearLayout.getChildAt(0);
                TextView textView = (TextView) linearLayout.getChildAt(2);
                if (imageView.getTag().toString().equals(tabId)) {
                    imageView.setImageResource(botResList.get(i).get(1));
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    imageView.setImageResource(botResList.get(i).get(0));
                    textView.setTextColor(getResources().getColor(R.color.hint_color));
                }
            }
        }
    }

    private void requestPermissions(String[] _permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> mPermissionList = new ArrayList<>();
            mPermissionList.clear();
            for (String _permission : _permissions) {
                if (ActivityCompat.checkSelfPermission(this, _permission) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(_permission);
                }
            }
            if (!mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
                String[] permissions2 = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
                ActivityCompat.requestPermissions(this, permissions2, 0000);
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private long exitTime = 0;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                ToastUtil.showShort(this, "再按一次退出");
                exitTime = System.currentTimeMillis();
            } else {
                BaseActivityManager.getInstance().AppExit(this);
                finish();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public void jumpTo(int position){
        if (position == 1){
            tab2.performClick();
        }else if (position == 2){
            tab3.performClick();
        }
    }
}
