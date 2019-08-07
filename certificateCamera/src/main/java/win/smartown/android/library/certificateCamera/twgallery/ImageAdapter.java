package win.smartown.android.library.certificateCamera.twgallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import win.smartown.android.library.certificateCamera.R;
import win.smartown.android.library.certificateCamera.TruckPic;

/**
 * Created by HelloCsl(cslgogogo@gmail.com) on 2015/10/4 0004.
 */
public class ImageAdapter extends BaseAdapter {
    private static final String TAG = "ImageAdapter";


    private final LayoutInflater mInflater;
    private  ArrayList<TruckPic> truckPics;
    public ImageAdapter(Context context, ArrayList<TruckPic> truckPics) {
        this.truckPics = truckPics;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return truckPics.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_image, parent, false);
            vh.imageView = (ImageView) convertView.findViewById(R.id.iv_image);
            vh.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        TruckPic truckPic = truckPics.get(position);
        vh.tvTitle.setText(truckPic.getTitle());
        vh.imageView.setImageResource(truckPic.getSpecimenRes());
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView tvTitle;
    }
}
