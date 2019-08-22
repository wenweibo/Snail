package com.cqkj.snail.system.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.cqkj.publicframework.tool.ToastUtil;
import com.cqkj.publicframework.widget.pull.PullToRefreshLayout;
import com.cqkj.snail.AppApplication;
import com.cqkj.snail.R;
import com.cqkj.snail.requestdata.RequestManager;
import com.cqkj.snail.requestdata.RequestUrl;
import com.cqkj.snail.system.callbackevent.LocationEvent;
import com.cqkj.snail.system.entity.CityEntity;
import com.cqkj.snail.tool.CommonRequest;
import com.cqkj.snail.tool.CommonUtil;
import com.cqkj.snail.tool.MyLocationListener;
import com.cqkj.snail.truck.adapter.FirstMenuAdapter;
import com.cqkj.snail.truck.adapter.TruckListAdapter;
import com.cqkj.snail.config.PublishStatus;
import com.cqkj.snail.truck.entity.MenuEntity;
import com.cqkj.snail.truck.entity.TruckEntity;
import com.cqkj.publicframework.activity.BaseTitleActivity;
import com.cqkj.publicframework.beans.CallBackObject;
import com.cqkj.publicframework.widget.NoScrollGridView;
import com.cqkj.publicframework.widget.WListView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 首页
 *
 * @author 闻维波 2019/07/26
 */
public class FirstPagerActivity extends BaseTitleActivity implements OnBannerListener,
        PullToRefreshLayout.OnRefreshListener {
    // 刷新控件
    @BindView(R.id.refresh_view)
    PullToRefreshLayout refresh_view;
    // 标头定位
    @BindView(R.id.tv_location)
    TextView tvLocation;
    // 轮播图
    @BindView(R.id.af_banner)
    Banner banner;
    // 买车按钮
    @BindView(R.id.lin_buy_car)
    LinearLayout linBuyCar;
    // 卖车按钮
    @BindView(R.id.lin_sell_car)
    LinearLayout linSellCar;
    // 收车按钮
    @BindView(R.id.lin_recycle_car)
    LinearLayout linRecycleCar;
    // 估车按钮
    @BindView(R.id.lin_assess_car)
    LinearLayout linAssessCar;
    // 筛选菜单
    @BindView(R.id.ngv_menu)
    NoScrollGridView ngvMenu;
    // 筛选菜单2
    @BindView(R.id.ngv_menu2)
    NoScrollGridView ngvMenu2;
    // 车辆列表
    @BindView(R.id.wlv_trucks)
    WListView wlvTrucks;
    // 最新上架按钮
    @BindView(R.id.lin_new)
    LinearLayout linNew;
    // 浏览记录按钮
    @BindView(R.id.lin_record)
    LinearLayout linRecord;

    // banner图片地址集合
    private ArrayList<String> listPath;
    // banner标题集合
    private ArrayList<String> listTitle;

    private final String[] menuItemArr1 = new String[]{"牵引车", "载货车", "挂车", "自卸车",
            "1-10万", "10-15万", "15-20万", "20-50万"};
    private final String[] menuItemArr2 = new String[]{
            "东风", "中国重汽", "福田欧曼", "陕汽"};

    private final int[] menuItemImgArr1 = new int[]{-1, -1, -1, -1,
            -1, -1, -1, -1};
    private final int[] menuItemImgArr2 = new int[]{R.mipmap.logo_df, R.mipmap.logo_zq,
            R.mipmap.logo_om, R.mipmap.logo_sq};

    // 车辆集合
    private List<TruckEntity> trucksData = new ArrayList<>();
    // 最新上架车辆集合
    private List<TruckEntity> newTrucks = new ArrayList<>();
    // 浏览记录车辆集合
    private List<TruckEntity> recordTrucks;
    // 车辆列表适配器
    private TruckListAdapter truckListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtil.evenRegister(this);
        // 默认加载最新上架车辆列表
        linNew.performClick();

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_first;
    }

    @Override
    protected void initView() {
        super.initView();
        title_main_layout.setVisibility(View.GONE);
        // 加载banner
        initBanner();
    }

    @Override
    protected void initListener() {
        super.initListener();
        linBuyCar.setOnClickListener(this);
        linSellCar.setOnClickListener(this);
        linRecycleCar.setOnClickListener(this);
        linAssessCar.setOnClickListener(this);
        linNew.setOnClickListener(this);
        linRecord.setOnClickListener(this);
        tvLocation.setOnClickListener(this);

        refresh_view.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        // 设置首页筛选菜单适配器
        ngvMenu.setAdapter(new FirstMenuAdapter(this, getMenuList(menuItemArr1, menuItemImgArr1)));
        ngvMenu2.setAdapter(new FirstMenuAdapter(this, getMenuList(menuItemArr2, menuItemImgArr2)));

        // 初始化车辆列表
        wlvTrucks.setScroll(true);
//        newTrucks = getTruckEntites(0);
//        recordTrucks = getTruckEntites(1);
        recordTrucks = new ArrayList<>();
        truckListAdapter = new TruckListAdapter(this, trucksData);
        wlvTrucks.setAdapter(truckListAdapter);

    }

    @Override
    protected void loadData() {
        super.loadData();
        showDialog("");
        getTrucks();
    }

    @Override
    public void onSuccess(int flag, CallBackObject obj) throws ParseException {
        super.onSuccess(flag, obj);
        switch (flag) {
            case RequestUrl.request_area:
                tvLocation.performLongClick();
                break;
            case RequestUrl.request_trucks:
                refresh_view.refreshFinish(PullToRefreshLayout.SUCCEED);
                List<TruckEntity> truckList = (List<TruckEntity>) obj.getObject();
                newTrucks.clear();
                if (truckList != null && !truckList.isEmpty()) {
                    newTrucks.addAll(truckList);
                }
                tabChange(linNew, linRecord);
                break;
        }
    }

    @Override
    public void onFailure(int flag, String message) {
        super.onFailure(flag, message);
        refresh_view.refreshFinish(PullToRefreshLayout.SUCCEED);
        ToastUtil.showShort(this, message);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            // 买车监听
            case R.id.lin_buy_car:
                break;
            // 卖车监听
            case R.id.lin_sell_car:
                break;
            // 收车监听
            case R.id.lin_recycle_car:
                break;
            // 估车监听
            case R.id.lin_assess_car:
                break;
            // 最新上架
            case R.id.lin_new:
                tabChange(linNew, linRecord);
                break;
            // 浏览记录
            case R.id.lin_record:
                tabChange(linRecord, linNew);
                break;
            case R.id.tv_location:
                // 如果有城市数据，则跳转至城市选择页面
                if (AppApplication.cityEntities != null && !AppApplication.cityEntities.isEmpty()) {
                    Intent intent = new Intent(this, CitySelectActivity.class);
                    startActivityForResult(intent, 0);
                } else {
                    // 否则，去重新拉取城市数据
                    showDialog("");
                    CommonRequest.getCitys(this);
                }
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        CommonUtil.evenUnregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 城市选择完成回调
        if (resultCode == CitySelectActivity.RESULT_CODE_SELECT_CITY) {
            if (data != null) {
                CityEntity cityEntity = (CityEntity) data.getExtras().getSerializable("cityEntity");
                AppApplication.selectCity = cityEntity;
                tvLocation.setText(com.cqkj.publicframework.tool.CommonUtil.changeStringEllipsis(cityEntity.getName()));
            }
        }
    }

    /**
     * 初始化banner
     * 目前采用模拟数据
     */
    private void initBanner() {
        // 放图片地址的集合
        listPath = new ArrayList<>();
        // 放标题的集合
        listTitle = new ArrayList<>();
        listPath.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
        listPath.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
        listPath.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");
        listPath.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg");
        listTitle.add("好好学习");
        listTitle.add("天天向上");
        listTitle.add("热爱劳动");
        listTitle.add("不搞对象");
        // 设置内置样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        // 设置图片加载器
        banner.setImageLoader(new MyLoader());
        // 设置图片网址或地址的集合
        banner.setImages(listPath);
        // 设置轮播的动画效果
        banner.setBannerAnimation(Transformer.CubeIn);
        // 设置轮播图的标题集合
        banner.setBannerTitles(listTitle);
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
        Log.i("tag", "你点了第" + position + "张轮播图");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LocationEvent event) {
        //收到此广播，刷新位置信息
        if (AppApplication.locationCity != null) {
            tvLocation.setText(com.cqkj.publicframework.tool.CommonUtil.
                    changeStringEllipsis(AppApplication.locationCity.getName()));
        }
    }

    /**
     * 下拉刷新监听
     * @param pullToRefreshLayout
     */
    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        getTrucks();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
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
     * 生成菜单数据
     *
     * @return List<MenuEntity>
     */
    private List<MenuEntity> getMenuList(String[] menuItemArr, int[] menuItemImgArr) {
        List<MenuEntity> menuEntities = new ArrayList<>();
        MenuEntity menuEntity = null;
        for (int i = 0; i < menuItemArr.length; i++) {
            String title = menuItemArr[i];
            menuEntity = new MenuEntity();
            menuEntity.setId(i + "");
            menuEntity.setTitle(title);

            menuEntity.setImgRes(menuItemImgArr[i]);
            menuEntities.add(menuEntity);
            menuEntity = null;
        }
        return menuEntities;
    }
    /**
     * 获取车源列表
     */
    public void getTrucks() {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageSize","6");
        params.put("pageNo", "1");
//        params.put("carWatchingPlace",
//                AppApplication.selectCity != null ? AppApplication.selectCity.getAdcode() :
//                        (AppApplication.locationCity != null ?
//                                AppApplication.locationCity.getAdcode() : ""));
        // 最新上架
        params.put("sortCondition", "SHELF_TIME");
        RequestManager.getRequestManager().post(RequestUrl.request_trucks, params, false, this);
    }

    /**
     * 列表标签切换
     *
     * @param taglin
     * @param hintLin
     */
    private void tabChange(LinearLayout taglin, LinearLayout hintLin) {

        TextView tagTv = (TextView) taglin.getChildAt(0);
        // 设置目标标签字体为粗体
        tagTv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tagTv.setTextColor(getResources().getColor(R.color.black));
        View lineTag = taglin.getChildAt(1);
        // 设置目标标签下划线为显示
        lineTag.setVisibility(View.VISIBLE);

        TextView hintTv = (TextView) hintLin.getChildAt(0);
        // 设置另一标签字体为正常
        hintTv.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
        hintTv.setTextColor(getResources().getColor(R.color.hint_color));
        View hintView = hintLin.getChildAt(1);
        // 设置另一标签下划线为隐藏
        hintView.setVisibility(View.GONE);

        trucksData.clear();
        if (taglin.getId() == R.id.lin_new) {
            trucksData.addAll(newTrucks);
        } else {
            trucksData.addAll(recordTrucks);
        }
        truckListAdapter.notifyDataSetChanged();
    }

}
