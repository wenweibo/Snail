package com.cqkj.snail.system.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    RelativeLayout linTop;
    // 返回按钮
    @BindView(R.id.iv_back)
    ImageView ivBack;
    // 重置按钮
    @BindView(R.id.tv_reset)
    TextView tvReset;
    // 省份和城市单选
    @BindView(R.id.tcv_province_city)
    TabControlView tcvProvinceCity;
    // 省份和城市滑动控件
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    // 碎片页面集合
    private List<Fragment> fragments = new ArrayList<Fragment>();
    // 城市页面
    private CityFragment cityFragment;
    // 省份页面
    private ProvinceFragment provinceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tcvProvinceCity.setSelection(0);
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
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) linTop.getLayoutParams();
        lp.setMargins(0,StatusBarUtils.getStatusBarHeight(this),0,0);
        linTop.setLayoutParams(lp);
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
        ivBack.setOnClickListener(this);
        tvReset.setOnClickListener(this);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        tcvProvinceCity.setOnTabSelectionChangedListener(new TabControlView.OnTabSelectionChangedListener() {
            @Override
            public void newSelection(String title, String value) {
            }
            @Override
            public void newSelection(int position) {
                viewPager.setCurrentItem(position);
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.iv_back:
                // 返回
                finish();
                break;
            case R.id.tv_reset:
                // 重置
                if (viewPager.getCurrentItem()==0){
                    cityFragment.clearData();
                }else{
                    provinceFragment.clearData();
                }
                break;
        }
    }

    private void initFrag() {
        cityFragment = CityFragment.newInstance();
        fragments.add(cityFragment);
        provinceFragment = ProvinceFragment.newInstance();
        fragments.add(provinceFragment);
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
            tcvProvinceCity.setSelection(arg0);
        }
    }

}
