package win.smartown.android.library.certificateCamera;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

/**
 *  图片浏览适配器
 * @author 闻维波
 * @since 2019/08/08 15:00
 */
public class MyViewpagerAdapter extends PagerAdapter {
    private Context context;
    // 图片列表数据
    private ArrayList<TruckPic> truckPics;
    private RequestOptions mRequestOptions;

    private int mChildCount = 0;
    public MyViewpagerAdapter(Context context, ArrayList<TruckPic> truckPics) {
        this.context = context;
        this.truckPics = truckPics;
        mRequestOptions = new RequestOptions()
                .error(R.mipmap.defaultimage)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

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
        PhotoView view = new PhotoView(context);
        view.enable();
        view.setScaleType(ImageView.ScaleType.FIT_CENTER);
        TruckPic truckPic = truckPics.get(position);
        if (TextUtils.isEmpty(truckPic.getImgPath())) {
            view.setImageResource(truckPic.getSpecimenRes());
        } else {
            Glide.with((Activity) context)
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

    @Override
    public int getItemPosition(@NonNull Object object) {
        if ( mChildCount > 0) {
            mChildCount --;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }
}
