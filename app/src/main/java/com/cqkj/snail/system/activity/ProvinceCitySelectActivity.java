package com.cqkj.snail.system.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cqkj.publicframework.activity.BaseTitleActivity;
import com.cqkj.publicframework.widget.TabControlView;
import com.cqkj.snail.R;
import com.cqkj.snail.system.fragment.CityFragment;
import com.cqkj.snail.system.fragment.ProvinceFragment;
import com.cqkj.snail.truck.adapter.FragmentViewPagerAdapter;
import com.xuexiang.xui.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 省份和城市选择页面
 */
public class ProvinceCitySelectActivity extends BaseTitleActivity {
    @BindView(R.id.lin_top)
    RelativeLayout lin_top;
    // 返回按钮
    @BindView(R.id.iv_back)
    ImageView iv_back;
    // 重置按钮
    @BindView(R.id.tv_right_menu)
    TextView tv_right_menu;
    // 省份和城市单选
    @BindView(R.id.tcv_province_city)
    TabControlView tcv_province_city;
    // 省份和城市滑动控件
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    // 碎片页面集合
    private List<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tcv_province_city.setSelection(0);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_province_city_select;
    }

    @Override
    protected void initView() {
        super.initView();
        // 设置白色状态栏
        StatusBarUtils.translucent(this, Color.WHITE);
        // 设置状态栏位黑色图标和字
        StatusBarUtils.setStatusBarLightMode(this);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lin_top.getLayoutParams();
        lp.setMargins(0,StatusBarUtils.getStatusBarHeight(this),0,0);
        lin_top.setLayoutParams(lp);
        title_main_layout.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        super.initData();
        initFrag();
    }

    @Override
    protected void initListener() {
        super.initListener();
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        tcv_province_city.setOnTabSelectionChangedListener(new TabControlView.OnTabSelectionChangedListener() {
            @Override
            public void newSelection(String title, String value) {
            }
            @Override
            public void newSelection(int position) {
                viewPager.setCurrentItem(position);
            }
        });
    }

    private void initFrag() {
        fragments.add(CityFragment.newInstance());
        fragments.add(ProvinceFragment.newInstance());
        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(
                this.getSupportFragmentManager(),viewPager, fragments);
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        // private int one = offset *2 +bmpW;//两个相邻页面的偏移量
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageSelected(int arg0) {
            tcv_province_city.setSelection(arg0);
        }
    }

}
