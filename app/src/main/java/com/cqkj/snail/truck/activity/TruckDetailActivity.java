package com.cqkj.snail.truck.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cqkj.snail.R;
import com.cqkj.snail.tool.CommonUtil;
import com.cqkj.snail.truck.adapter.ImgAdapter;
import com.cqkj.snail.truck.adapter.TruckBaseAdapter;
import com.cqkj.snail.truck.entity.ImageViewInfo;
import com.cqkj.snail.truck.entity.TruckBaseEntity;
import com.cqkj.publicframework.widget.NoScrollGridView;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.imageview.nine.NineGridImageViewAdapter;
import com.xuexiang.xui.widget.imageview.preview.PreviewBuilder;
import com.xuexiang.xui.widget.imageview.preview.loader.GlideMediaLoader;
import com.xuexiang.xui.widget.toast.XToast;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 货车详情
 *
 * @author 闻维波 2019/07/30
 */
public class TruckDetailActivity extends AppCompatActivity implements OnBannerListener {
    @BindView(R.id.appbar_layout)
    AppBarLayout appbar_layout;
    // 轮播图集
    @BindView(R.id.af_banner)
    Banner banner;
    // 标题头
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    // 标题
    @BindView(R.id.tv_title)
    TextView tv_title;
    // 基本信息网格视图
    @BindView(R.id.ngv_base)
    NoScrollGridView ngv_base;
    // 图片列表视图
    @BindView(R.id.ngl_images)
    NoScrollGridView ngl_images;

    // banner图片地址集合
    private ArrayList<String> list_path;
    // banner标题集合
    private ArrayList<String> list_title;
    // 车辆基本信息
    List<TruckBaseEntity> truckBaseEntities;
    // 图片列表
    private List<ImageViewInfo> imageViewInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_detail);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    /**
     * 初始化页面控件
     */
    private void initView() {
        // 设置状态栏透明
        StatusBarUtils.translucent(this, Color.TRANSPARENT);
        // 设置状态栏位黑色图标和字
        StatusBarUtils.setStatusBarLightMode(this);
        //设置标头菜单按钮
        toolbar.inflateMenu(R.menu.menu_custom);

        initBanner();
        appbar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                // 向上滚动
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    tv_title.setText("我们的故事");
                    StatusBarUtils.setStatusBarDarkMode(TruckDetailActivity.this);
                } else {
                    StatusBarUtils.setStatusBarLightMode(TruckDetailActivity.this);
                    tv_title.setText("");
                }
            }
        });
    }

    private void initData() {
        getBaseData();
        // 组成数据
        imageViewInfos = CommonUtil.computeBoundsBackward(list_path);
        ngv_base.setAdapter(new TruckBaseAdapter(this, truckBaseEntities));

        ImgAdapter adapter = new ImgAdapter(this, imageViewInfos, ngl_images);
        ngl_images.setAdapter(adapter);
    }


    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_share) {
                    XToast.normal(TruckDetailActivity.this, "分享").show();
                } else {
                    XToast.normal(TruckDetailActivity.this, "收藏").show();
                }
                return false;
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
        list_path.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1564721623561&di=50a2e2ba770ad99e1279b6fbb1bd1e71&imgtype=0&src=http%3A%2F%2Fimg1001.pocoimg.cn%2Fimage%2Fpoco%2Fworks%2F04%2F2010%2F0826%2F02%2F54102567201008260201332544174819597_007_54102567.jpg");
        list_path.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1564721623561&di=1d1acd893b4b73486d3b191769dfa55f&imgtype=0&src=http%3A%2F%2Fimg1.gtimg.com%2Fauto%2Fpics%2Fhv1%2F250%2F208%2F2286%2F148700440.jpg");
        list_path.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1564721623560&di=e50c4c6163a95fcd3690c9fb6fd0ff51&imgtype=0&src=http%3A%2F%2Fimg11.kcimg.cn%2Fofficialpic%2F0%2F20%2Finitc.jpg%25211200");
        list_path.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1564721623559&di=7c7d994b209cfc5e6937deb880d2e8fb&imgtype=0&src=http%3A%2F%2Fpic30.nipic.com%2F20130615%2F10448193_103148405000_2.jpg");
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
     *
     * @param position
     */
    @Override
    public void OnBannerClick(int position) {

        // 启动
        PreviewBuilder.from(this)
                .setImgs(imageViewInfos)
                .setCurrentIndex(position)
                .setType(PreviewBuilder.IndicatorType.Dot)
                .start();
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

    private void getBaseData() {
        truckBaseEntities = new ArrayList<>();
        TruckBaseEntity truckBaseEntity = new TruckBaseEntity();
        truckBaseEntity.setTitle("车辆类型");
        truckBaseEntity.setContent("自卸车");
        truckBaseEntities.add(truckBaseEntity);

        truckBaseEntity = new TruckBaseEntity();
        truckBaseEntity.setTitle("表显里程");
        truckBaseEntity.setContent("12.2万公里");
        truckBaseEntities.add(truckBaseEntity);

        truckBaseEntity = new TruckBaseEntity();
        truckBaseEntity.setTitle("发动机品牌");
        truckBaseEntity.setContent("玉柴");
        truckBaseEntities.add(truckBaseEntity);

        truckBaseEntity = new TruckBaseEntity();
        truckBaseEntity.setTitle("燃料类型");
        truckBaseEntity.setContent("柴油");
        truckBaseEntities.add(truckBaseEntity);

        truckBaseEntity = new TruckBaseEntity();
        truckBaseEntity.setTitle("排放标准");
        truckBaseEntity.setContent("国三");
        truckBaseEntity.setFlag(true);
        truckBaseEntities.add(truckBaseEntity);

        truckBaseEntity = new TruckBaseEntity();
        truckBaseEntity.setTitle("车辆品牌");
        truckBaseEntity.setContent("东风商用");
        truckBaseEntities.add(truckBaseEntity);

        truckBaseEntity = new TruckBaseEntity();
        truckBaseEntity.setTitle("型号");
        truckBaseEntity.setContent("东风大力神");
        truckBaseEntities.add(truckBaseEntity);

        truckBaseEntity = new TruckBaseEntity();
        truckBaseEntity.setTitle("颜色");
        truckBaseEntity.setContent("红色");
        truckBaseEntities.add(truckBaseEntity);

        truckBaseEntity = new TruckBaseEntity();
        truckBaseEntity.setTitle("马力");
        truckBaseEntity.setContent("340匹");
        truckBaseEntities.add(truckBaseEntity);

        truckBaseEntity = new TruckBaseEntity();
        truckBaseEntity.setTitle("驱动方式");
        truckBaseEntity.setContent("6x4");
        truckBaseEntities.add(truckBaseEntity);

        truckBaseEntity = new TruckBaseEntity();
        truckBaseEntity.setTitle("箱体长度");
        truckBaseEntity.setContent("5.8m");
        truckBaseEntities.add(truckBaseEntity);

        truckBaseEntity = new TruckBaseEntity();
        truckBaseEntity.setTitle("栏板高度");
        truckBaseEntity.setContent("1.2m");
        truckBaseEntities.add(truckBaseEntity);
    }

    private NineGridImageViewAdapter<ImageViewInfo> mAdapter = new NineGridImageViewAdapter<ImageViewInfo>() {
        /**
         * 图片加载
         *
         * @param context
         * @param imageView
         * @param imageViewInfo 图片信息
         */
        @Override
        protected void onDisplayImage(Context context, ImageView imageView, ImageViewInfo imageViewInfo) {
            Glide.with(imageView.getContext())
                    .load(imageViewInfo.getUrl())
                    .apply(GlideMediaLoader.getRequestOptions())
                    .into(imageView);
        }

        @Override
        protected ImageView generateImageView(Context context) {
            return super.generateImageView(context);
        }
    };



}
