package win.smartown.android.library.certificateCamera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 图片浏览器
 *
 * @author 闻维波
 * @since 2019/08/07 16:39
 */
public class PicActivity extends Activity implements View.OnClickListener {
    // 主视图
    private LinearLayout lin_main;
    // 关闭按钮
    private ImageView iv_close;
    // 图片容器
    private ViewPager viewpager;
    // 图片提示文本
    private TextView tv_remind;
    // 计数文本
    private TextView tv_num;
    // 去拍照按钮
    private ImageView iv_go_photo;

    // 图片列表数据
    private ArrayList<TruckPic> truckPics;
//    private RequestOptions mRequestOptions;
    // 当前位置
    private int currentPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
        initView();
        initData();
        setListener();
        viewpager.setCurrentItem(currentPosition);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        lin_main = findViewById(R.id.lin_main);
        iv_close = findViewById(R.id.iv_close);
        viewpager = findViewById(R.id.viewpager);
        tv_remind = findViewById(R.id.tv_remind);
        tv_num = findViewById(R.id.tv_num);
        iv_go_photo = findViewById(R.id.iv_go_photo);
    }

    /**
     * 初始化页面数据
     */
    private void initData() {
        // 获取图片列表数据
        truckPics = getIntent().getExtras().getParcelableArrayList("truckPics");
        // 获取当前操作下标
        currentPosition = getIntent().getExtras().getInt("currentPosition");
        // 设置滑动视图适配器
        viewpager.setAdapter(new MyViewpagerAdapter(this, truckPics));
        // 如果当前操作下标为0，则设置对应的视图
        if (currentPosition == 0){
           setSelectView(currentPosition);
        }
    }

    /**
     * 设置监听
     */
    private void setListener(){
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setSelectView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        iv_close.setOnClickListener(this);
        iv_go_photo.setOnClickListener(this);
    }

    /**
     * 设置图片显示
     * @param position
     */
    private void setSelectView(int position) {
        TruckPic truckPic = truckPics.get(position);
        // 设置数字提示
        tv_num.setText("(" + (position + 1) + "/" + truckPics.size()+ ")" + truckPic.getTitle());
        // 如果没有照片，则设置为样图，且显示提示文本
        if (TextUtils.isEmpty(truckPic.getImgPath())){
            tv_remind.setVisibility(View.VISIBLE);
            iv_go_photo.setImageResource(R.mipmap.go_photograph);
        }else{
            // 如果有照片，则显示照片，并隐藏提示文本
            tv_remind.setVisibility(View.GONE);
            iv_go_photo.setImageResource(R.mipmap.reset_photograph);
        }
    }

    @Override
    public void onClick(View v) {
        // 关闭按钮监听
        if (v.getId() == R.id.iv_close){
            finish();
        }else{
            // 完成按钮监听
            Intent intent = new Intent(this,CameraActivity.class);
            intent.putExtra("currentPosition",viewpager.getCurrentItem());
            setResult(0,intent);
            finish();
        }
    }
}
