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
import com.cqkj.publicframework.tool.CommonUtil;
import com.cqkj.snail.R;
import com.cqkj.snail.buy.entity.TruckPicEntity;
import com.cqkj.snail.requestdata.RequestManager;
import com.cqkj.snail.truck.adapter.ImgAdapter;
import com.cqkj.snail.truck.adapter.TruckBaseAdapter;
import com.cqkj.snail.truck.entity.ImageViewInfo;
import com.cqkj.snail.truck.entity.TruckBaseEntity;
import com.cqkj.publicframework.widget.NoScrollGridView;
import com.cqkj.snail.truck.entity.TruckEntity;
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
    // 货车标题
    @BindView(R.id.tv_trcuk_title)
    TextView tvTrcukTitle;
    // 价格
    @BindView(R.id.tv_price)
    TextView tvPrice;

    // banner图片地址集合
    private ArrayList<String> list_path;
    // banner标题集合
    private ArrayList<String> list_title;
    // 车辆基本信息
    List<TruckBaseEntity> truckBaseEntities;
    // 图片列表
    private List<ImageViewInfo> imageViewInfos;
    private TruckEntity truckEntity;

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


        appbar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                // 向上滚动
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    tv_title.setText("货车详情");
                    StatusBarUtils.setStatusBarDarkMode(TruckDetailActivity.this);
                } else {
                    StatusBarUtils.setStatusBarLightMode(TruckDetailActivity.this);
                    tv_title.setText("");
                }
            }
        });
    }

    private void initData() {
        truckEntity = (TruckEntity) getIntent().getExtras().getSerializable("truckEntity");
        getBaseData();
        initBanner();
        // 组成数据
        imageViewInfos = com.cqkj.snail.tool.CommonUtil.computeBoundsBackward(list_path);
        ngv_base.setAdapter(new TruckBaseAdapter(this, truckBaseEntities));

        ImgAdapter adapter = new ImgAdapter(this, imageViewInfos, ngl_images);
        ngl_images.setAdapter(adapter);

        tvTrcukTitle.setText(truckEntity.getVehicleBrandContent() +
                truckEntity.getVehicleTypeContent() + " " + truckEntity.getHorsePower());
        tvPrice.setText(truckEntity.getPrice() + "万");
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
        // 将必传图片列表取出
        List<TruckPicEntity> attachmentPics = truckEntity.getAttachmentPic();
        // 如果必传列表不为空,则将之添加至图片列表中
        if (attachmentPics != null) {
            for (TruckPicEntity truckPicEntity : attachmentPics) {
                list_path.add(RequestManager.fileipurl + truckPicEntity.getSaveName());
            }
        }
        // 将车辆牌照图片列表取出
        List<TruckPicEntity> attachmentPicLicensePlates = truckEntity.getAttachmentPicLicensePlates();
        // 如果车辆牌照图片列表不为空，则将之添加至图片列表中
        if (attachmentPicLicensePlates != null) {
            for (TruckPicEntity truckPicEntity : attachmentPicLicensePlates) {
                list_path.add(RequestManager.fileipurl + truckPicEntity.getSaveName());
            }
        }
        // 将驾驶室图片列表取出
        List<TruckPicEntity> attachmentPicCab = truckEntity.getAttachmentPicCab();
        // 如果驾驶室图片列表不为空，则将之添加至图片列表中
        if (attachmentPicCab!=null){
            for (TruckPicEntity truckPicEntity : attachmentPicCab) {
                list_path.add(RequestManager.fileipurl + truckPicEntity.getSaveName());
            }
        }
        if (list_path.size() > 0) {
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
            banner.setVisibility(View.VISIBLE);
        } else {
            banner.setVisibility(View.GONE);
        }


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

    /**
     * 组装基础数据
     */
    private void getBaseData() {
        truckBaseEntities = new ArrayList<>();
        TruckBaseEntity truckBaseEntity = new TruckBaseEntity();
        truckBaseEntity.setTitle("车辆类型");
        truckBaseEntity.setContent(CommonUtil.changeStringNotNull(truckEntity.getVehicleTypeContent()));
        truckBaseEntities.add(truckBaseEntity);

        truckBaseEntity = new TruckBaseEntity();
        truckBaseEntity.setTitle("表显里程");
        truckBaseEntity.setContent(CommonUtil.changeStringNotNull(truckEntity.getMileage()));
        truckBaseEntities.add(truckBaseEntity);

        truckBaseEntity = new TruckBaseEntity();
        truckBaseEntity.setTitle("发动机品牌");
        truckBaseEntity.setContent(CommonUtil.changeStringNotNull(truckEntity.getEngineBrandContent()));
        truckBaseEntities.add(truckBaseEntity);

        truckBaseEntity = new TruckBaseEntity();
        truckBaseEntity.setTitle("燃料类型");
        truckBaseEntity.setContent(CommonUtil.changeStringNotNull(truckEntity.getFuelTypeContent()));
        truckBaseEntities.add(truckBaseEntity);

        truckBaseEntity = new TruckBaseEntity();
        truckBaseEntity.setTitle("排放标准");
        truckBaseEntity.setContent(CommonUtil.changeStringNotNull(truckEntity.getEmissionStandardContent()));
        truckBaseEntity.setFlag(true);
        truckBaseEntities.add(truckBaseEntity);

        truckBaseEntity = new TruckBaseEntity();
        truckBaseEntity.setTitle("车辆品牌");
        truckBaseEntity.setContent(CommonUtil.changeStringNotNull(truckEntity.getVehicleBrandContent()));
        truckBaseEntities.add(truckBaseEntity);

        truckBaseEntity = new TruckBaseEntity();
        truckBaseEntity.setTitle("型号");
        truckBaseEntity.setContent(CommonUtil.changeStringNotNull(truckEntity.getVehicleSystemContent()));
        truckBaseEntities.add(truckBaseEntity);

        truckBaseEntity = new TruckBaseEntity();
        truckBaseEntity.setTitle("颜色");
        truckBaseEntity.setContent(CommonUtil.changeStringNotNull(truckEntity.getColourContent()));
        truckBaseEntities.add(truckBaseEntity);

        truckBaseEntity = new TruckBaseEntity();
        truckBaseEntity.setTitle("马力");
        truckBaseEntity.setContent(CommonUtil.changeStringNotNull(truckEntity.getHorsePower()));
        truckBaseEntities.add(truckBaseEntity);

        truckBaseEntity = new TruckBaseEntity();
        truckBaseEntity.setTitle("驱动方式");
        truckBaseEntity.setContent(CommonUtil.changeStringNotNull(truckEntity.getDrivingModeContent()));
        truckBaseEntities.add(truckBaseEntity);

//        truckBaseEntity = new TruckBaseEntity();
//        truckBaseEntity.setTitle("箱体长度");
//        truckBaseEntity.setContent("5.8m");
//        truckBaseEntities.add(truckBaseEntity);

//        truckBaseEntity = new TruckBaseEntity();
//        truckBaseEntity.setTitle("栏板高度");
//        truckBaseEntity.setContent("1.2m");
//        truckBaseEntities.add(truckBaseEntity);
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
