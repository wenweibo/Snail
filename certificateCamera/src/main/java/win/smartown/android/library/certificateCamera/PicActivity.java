package win.smartown.android.library.certificateCamera;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

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
    // 图片容器
    private TextView tv_remind;
    // 计数文本
    private TextView tv_num;
    // 去拍照按钮
    private ImageView iv_go_photo;

    // 图片列表数据
    private ArrayList<TruckPic> truckPics;
    private RequestOptions mRequestOptions;
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

    private void initData() {
        truckPics = getIntent().getExtras().getParcelableArrayList("truckPics");
        currentPosition = getIntent().getExtras().getInt("currentPosition");
        mRequestOptions = new RequestOptions()
                .error(R.mipmap.defaultimage)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        viewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return truckPics.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                PhotoView view = new PhotoView(PicActivity.this);
                view.enable();
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                TruckPic truckPic = truckPics.get(position);
                if (TextUtils.isEmpty(truckPic.getImgPath())) {
                    view.setImageResource(truckPic.getSpecimenRes());
                } else {
                    Glide.with(PicActivity.this)
                            .asBitmap()
                            .apply(mRequestOptions)
                            .load(truckPic.getImgPath()).into(view);
                }
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

    }

    private void setListener(){
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TruckPic truckPic = truckPics.get(position);
                tv_num.setText("(" + (position + 1) + "/" + truckPics.size()+ ")" + truckPic.getTitle());
                if (TextUtils.isEmpty(truckPic.getImgPath())){
                    tv_remind.setVisibility(View.VISIBLE);
                    iv_go_photo.setImageResource(R.mipmap.go_photograph);
                }else{
                    tv_remind.setVisibility(View.GONE);
                    iv_go_photo.setImageResource(R.mipmap.reset_photograph);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        iv_close.setOnClickListener(this);
        iv_go_photo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_close){
            finish();
        }else{

        }
    }
}
