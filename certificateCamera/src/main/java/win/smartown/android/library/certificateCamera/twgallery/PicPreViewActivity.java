package win.smartown.android.library.certificateCamera.twgallery;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import win.smartown.android.library.certificateCamera.MyViewpagerAdapter;
import win.smartown.android.library.certificateCamera.R;
import win.smartown.android.library.certificateCamera.TruckPic;

/**
 *  已拍照的货车图片浏览器
 * @author 闻维波
 * @since 2019/08/08 14:36
 */
public class PicPreViewActivity extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    // 返回按钮
    private ImageView ivBack;
    // 删除按钮
    private ImageView ivDelete;
    // 标题文本
    private TextView tvTitle;
    // 图片容器
    private ViewPager viewpager;
    // 图片提示文本
    private TextView tvRemind;
    // 计数文本
    private TextView tvNum;


    // 图片列表数据
    private ArrayList<TruckPic> truckPics;
    // 当前位置
    private int currentPosition;
    // 图片滑动视图适配器
    private MyViewpagerAdapter myViewpagerAdapter;
    // 回调码
    public static final int RESULT_CODE_PREVIEW = 0x66;
    // 是否是必须列表
    private int must;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_pre_view);
        initView();
        initData();
        setListener();
        // 设置图片滑动到当前下标的视图
        viewpager.setCurrentItem(currentPosition);
        if (currentPosition == 0){
            setSelectView(currentPosition);
        }
    }


    /**
     * 初始化视图
     */
    private void initView() {
        ivBack = findViewById(R.id.iv_back);
        ivDelete = findViewById(R.id.iv_delete);
        tvTitle = findViewById(R.id.tv_title);
        viewpager = findViewById(R.id.viewpager);
        tvRemind = findViewById(R.id.tv_remind);
        tvNum = findViewById(R.id.tv_num);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 获取图片列表数据
        truckPics = getIntent().getExtras().getParcelableArrayList("truckPics");
        // 获取当前操作下标
        currentPosition = getIntent().getExtras().getInt("currentPosition");
        // 获取是否是必须列表
        must = getIntent().getExtras().getInt("must");
        // 设置图片滑动视图适配器
        viewpager.setAdapter(myViewpagerAdapter = new MyViewpagerAdapter(this,truckPics));
    }

    /**
     * 设置监听
     */
    private void setListener() {
        ivBack.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        viewpager.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
       if (v.getId() == R.id.iv_back){
           // 返回
           Intent intent = new Intent();
           intent.putParcelableArrayListExtra("truckPics",truckPics);
           intent.putExtra("must",must);
           setResult(RESULT_CODE_PREVIEW, intent);
           finish();
       }else if (v.getId() == R.id.iv_delete){
           // 删除
           truckPics.get(viewpager.getCurrentItem()).setImgPath("");
           myViewpagerAdapter.notifyDataSetChanged();
           setSelectView(viewpager.getCurrentItem());
       }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        setSelectView(position);
    }

    private void setSelectView(int position) {
        TruckPic truckPic = truckPics.get(position);
        tvTitle.setText("(" + (position + 1) + "/" + truckPics.size()+ ")" + truckPic.getTitle());
        // 如果当前操作的数据没有照片
        if (TextUtils.isEmpty(truckPic.getImgPath())){
            // 隐藏删除按钮
            ivDelete.setVisibility(View.GONE);
            tvRemind.setVisibility(View.VISIBLE);
        }else{
            // 显示删除按钮
            ivDelete.setVisibility(View.VISIBLE);
            tvRemind.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//点击的是返回键
            ivBack.performClick();
        }
        return false;
    }
}
