package com.cqkj.snail.system.activity;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.cqkj.snail.R;
import com.cqkj.snail.truck.adapter.FirstMenuAdapter;
import com.cqkj.snail.truck.entity.MenuEntity;
import com.fxkj.publicframework.activity.BaseTitleActivity;
import com.fxkj.publicframework.beans.CallBackObject;
import com.fxkj.publicframework.widget.NoScrollGridView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 首页
 * @author 闻维波 2019/07/26
 */
public class FirstPagerActivity extends BaseTitleActivity implements OnBannerListener {
    // 轮播图
    @BindView(R.id.af_banner)
    Banner banner;
    // 买车按钮
    @BindView(R.id.lin_buy_car)
    LinearLayout lin_buy_car;
    // 卖车按钮
    @BindView(R.id.lin_sell_car)
    LinearLayout lin_sell_car;
    // 收车按钮
    @BindView(R.id.lin_recycle_car)
    LinearLayout lin_recycle_car;
    // 估车按钮
    @BindView(R.id.lin_assess_car)
    LinearLayout lin_assess_car;
    // 筛选菜单
    @BindView(R.id.ngv_menu)
    NoScrollGridView ngv_menu;
    // 筛选菜单2
    @BindView(R.id.ngv_menu2)
    NoScrollGridView ngv_menu2;

    // banner图片地址集合
    private ArrayList<String> list_path;
    // banner标题集合
    private ArrayList<String> list_title;

    private final String[] menuItemArr1 = new String[]{"牵引车", "载货车", "挂车", "自卸车",
            "1-10万", "10-15万", "15-20万", "20-50万"};
    private final String[] menuItemArr2 = new String[]{
            "东风", "中国重汽", "福田欧曼", "陕汽"};

    private final int[] menuItemImgArr1 = new int[]{-1, -1, -1, -1,
            -1, -1, -1, -1};
   private final int[] menuItemImgArr2 = new int[]{R.mipmap.logo_df, R.mipmap.logo_zq,
           R.mipmap.logo_om, R.mipmap.logo_sq};
    @Override
    protected int getLayoutId() {
//        setBack(false);
        return R.layout.activity_first;
    }

    @Override
    protected void initView() {
        super.initView();
        title_text.setText(getString(R.string.first_pager));
        back.setVisibility(View.GONE);
        title_do.setVisibility(View.GONE);
        // 加载banner
        initBanner();
    }

    @Override
    protected void initListener() {
        super.initListener();
        lin_buy_car.setOnClickListener(this);
        lin_sell_car.setOnClickListener(this);
        lin_recycle_car.setOnClickListener(this);
        lin_assess_car.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        // 设置首页筛选菜单适配器
        ngv_menu.setAdapter(new FirstMenuAdapter(this,getMenuList(menuItemArr1, menuItemImgArr1)));
        ngv_menu2.setAdapter(new FirstMenuAdapter(this,getMenuList(menuItemArr2, menuItemImgArr2)));
    }

    @Override
    public void onSuccess(int flag, CallBackObject obj) throws ParseException {}

    @Override
    public void onFailure(int flag, String message) {}

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            //买车监听
            case R.id.lin_buy_car:
                break;
            //卖车监听
            case R.id.lin_sell_car:
                break;
            //收车监听
            case R.id.lin_recycle_car:
                break;
            //估车监听
            case R.id.lin_assess_car:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        banner.setBannerAnimation(Transformer.CubeIn);
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

    /**
     * 生成菜单数据
     * @return List<MenuEntity>
     */
    private List<MenuEntity> getMenuList(String[] menuItemArr, int[] menuItemImgArr){
        List<MenuEntity> menuEntities = new ArrayList<>();
        MenuEntity menuEntity = null;
        for (int i = 0; i < menuItemArr.length; i++) {
            String title = menuItemArr[i];
            menuEntity = new MenuEntity();
            menuEntity.setId(i+"");
            menuEntity.setTitle(title);

            menuEntity.setImgRes(menuItemImgArr[i]);
            menuEntities.add(menuEntity);
            menuEntity = null;
        }
        return menuEntities;
    }

}
