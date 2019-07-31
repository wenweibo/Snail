package com.cqkj.snail.truck.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cqkj.snail.R;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 货车详情
 * @author 闻维波 2019/07/30
 */
public class TruckDetailActivity extends AppCompatActivity  implements OnBannerListener {
    @BindView(R.id.appbar_layout)
    AppBarLayout appbar_layout;
    @BindView(R.id.af_banner)
    Banner banner;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // banner图片地址集合
    private ArrayList<String> list_path;
    // banner标题集合
    private ArrayList<String> list_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_detail);
        ButterKnife.bind(this);
        StatusBarUtils.translucent(this, Color.TRANSPARENT);
        StatusBarUtils.setStatusBarLightMode(this);
        toolbar.setTitle("我们的故事");
        toolbar.setTitleTextColor(getResources().getColor(R.color.focus_color_3));
//        toolbar.setTitleTextAppearance(this,R.style.ToolbarTheme);
        initBanner();
        appbar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    toolbar.setTitleTextColor(getResources().getColor(R.color.focus_color_3));
//                    StatusBarUtils.setStatusBarDarkMode(TruckDetailActivity.this);
                } else {
                    toolbar.setTitleTextColor(getResources().getColor(R.color.focus_color_2));
//                    StatusBarUtils.setStatusBarLightMode(TruckDetailActivity.this);
                }
            }
        });
    }

    /**
     * 初始化banner
     * 目前采用模拟数据
     */
    private void initBanner() {
        // 放图片地址的集合
        list_path = new ArrayList<>();
        // 放标题的集合
        list_title = new ArrayList<>();
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg");
        list_title.add("好好学习");
        list_title.add("天天向上");
        list_title.add("热爱劳动");
        list_title.add("不搞对象");
        // 设置内置样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        // 设置图片加载器
        banner.setImageLoader(new MyLoader());
        // 设置图片网址或地址的集合
        banner.setImages(list_path);
        // 设置轮播的动画效果
        banner.setBannerAnimation(Transformer.Default);
        // 设置轮播图的标题集合
        banner.setBannerTitles(list_title);
        // 设置轮播间隔时间
        banner.setDelayTime(3000);
        // 设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        // 设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                // 以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(this)
                // 必须最后调用的方法，启动轮播图。
                .start();


    }

    /**
     * banner图点击监听
     * @param position
     */
    @Override
    public void OnBannerClick(int position) {
        Log.i("tag", "你点了第" + position + "张轮播图");
    }

    /**
     * 自定义的图片加载器
     */
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }

}
