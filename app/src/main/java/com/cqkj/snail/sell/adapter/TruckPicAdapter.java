package com.cqkj.snail.sell.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cqkj.snail.R;
import com.cqkj.snail.sell.activity.UploadActivity;
import com.cqkj.snail.truck.entity.MenuEntity;
import com.fxkj.publicframework.adapter.CommonAdapter;
import com.fxkj.publicframework.widget.BottomPop;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import win.smartown.android.library.certificateCamera.CameraActivity;
import win.smartown.android.library.certificateCamera.TruckPic;

/**
 * 货车照片列表适配器
 * @author 闻维波
 * @since 2019/08/06 17:12
 */
public class TruckPicAdapter extends CommonAdapter {
    private int type;
    ArrayList<TruckPic> _list;
    public TruckPicAdapter(Context _context, List<TruckPic> _list, int type) {
        context = _context;
        list = _list;
        this._list = (ArrayList<TruckPic>) _list;
        this.type = type;
        layoutid = R.layout.item_truck_pic;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        ViewHolder viewHolder = new ViewHolder(convertView);
        TruckPic truckPic = (TruckPic) getItem(position);
        // 如果有图片地址，则显示图片
        if (!TextUtils.isEmpty(truckPic.getImgPath())){

        }else{
            //否则，显示示例图片
            viewHolder.iv_truck.setImageResource(truckPic.getSpecimenRes());
        }
        viewHolder.tv_title.setText(truckPic.getTitle());
        viewHolder.iv_xing.setVisibility(truckPic.getMustFlag() == 1 ? View.VISIBLE : View.GONE);
        viewHolder.rl_mask.getBackground().setAlpha(150);
        viewHolder.lin_main.setBackgroundResource(truckPic.getSelected()==1 ? R.color.hint_color : R.color.white);
        convertView.setOnClickListener(new ItemOnClick(position));
        return convertView;
    }

    class ItemOnClick implements View.OnClickListener{
        int position;

        public ItemOnClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            for (int i = 0; i < list.size(); i++) {
                TruckPic truckPic = (TruckPic) getItem(i);
                if (i == position){
                    truckPic.setSelected(1);
                }else{
                    truckPic.setSelected(0);
                }
            }
            notifyDataSetChanged();
            ((UploadActivity)context).setUnSelect(type);

            List<String> menus = new ArrayList<>();
            menus.add("拍照");
            menus.add("从相册选");
            BottomPop bottomPop = new BottomPop(context,context.getString(R.string.upload_pic));
            bottomPop.setListMsg(new MenuAdapter(context,menus)).init();
            bottomPop.showAtLocation(v, Gravity.BOTTOM,0,0);

        }
    }

    class MenuAdapter extends CommonAdapter {
        public MenuAdapter(Context _context, List<String> _list) {
            context = _context;
            list = _list;
            layoutid = android.R.layout.simple_list_item_1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);
            ViewHolder viewHolder = new ViewHolder(convertView);
            String item = (String) getItem(position);
            viewHolder.text1.setGravity(Gravity.CENTER);
            viewHolder.text1.setText(item);
            convertView.setOnClickListener(new mItemClick(position));
            return convertView;
        }
        class ViewHolder extends CommonAdapter.ViewHolder {
            @BindView(android.R.id.text1)
            TextView text1;
            ViewHolder(View view) {
                super(view);
            }
        }

        class mItemClick implements View.OnClickListener{
            int position;

            public mItemClick(int position) {
                this.position = position;
            }

            @Override
            public void onClick(View v) {
                if (position == 0){
                    takePhoto();
                }
            }
        }
    }

    class ViewHolder extends CommonAdapter.ViewHolder {
        @BindView(R.id.lin_main)
        LinearLayout lin_main;
        @BindView(R.id.rl_mask)
        LinearLayout rl_mask;
        @BindView(R.id.iv_truck)
        ImageView iv_truck;
        @BindView(R.id.iv_xing)
        ImageView iv_xing;
        @BindView(R.id.tv_title)
        TextView tv_title;

        ViewHolder(View view) {
            super(view);
        }
    }

    /**
     * 拍摄证件照片
     */
    private void takePhoto() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, 0x12);
            return;
        }
        CameraActivity.openCertificateCamera((Activity) context, CameraActivity.TYPE_IDCARD_FRONT,_list);
    }
}
