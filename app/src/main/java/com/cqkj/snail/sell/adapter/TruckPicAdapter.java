package com.cqkj.snail.sell.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cqkj.snail.R;
import com.cqkj.snail.sell.activity.UploadActivity;
import com.cqkj.snail.truck.entity.MenuEntity;
import com.fxkj.publicframework.adapter.CommonAdapter;
import com.fxkj.publicframework.widget.BottomPop;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.xuexiang.xui.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import win.smartown.android.library.certificateCamera.CameraActivity;
import win.smartown.android.library.certificateCamera.TruckPic;
import win.smartown.android.library.certificateCamera.twgallery.PicPreViewActivity;

/**
 * 货车照片列表适配器
 * @author 闻维波
 * @since 2019/08/06 17:12
 */
public class TruckPicAdapter extends CommonAdapter {
    // 是否是必须
    private int type;
    ArrayList<TruckPic> _list;
    private RequestOptions mRequestOptions;
    // 底部弹窗
    private BottomPop bottomPop;

    public TruckPicAdapter(Context _context, List<TruckPic> _list, int type) {
        context = _context;
        list = _list;
        this._list = (ArrayList<TruckPic>) _list;
        this.type = type;
        layoutid = R.layout.item_truck_pic;
        mRequestOptions = new RequestOptions()
                .error(R.mipmap.defaultimage)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        ViewHolder viewHolder = new ViewHolder(convertView);
        TruckPic truckPic = (TruckPic) getItem(position);
        // 如果有图片地址，则显示图片
        if (!TextUtils.isEmpty(truckPic.getImgPath())){
            Glide.with(context)
                    .asBitmap()
                    .apply(mRequestOptions)
                    .load(truckPic.getImgPath()).into(viewHolder.iv_truck);
            viewHolder.rl_mask.setVisibility(View.INVISIBLE);
        }else{
            //否则，显示示例图片
            viewHolder.iv_truck.setImageResource(truckPic.getSpecimenRes());
            viewHolder.rl_mask.setVisibility(View.VISIBLE);
            viewHolder.rl_mask.getBackground().setAlpha(150);
        }
        viewHolder.tv_title.setText(truckPic.getTitle());
        viewHolder.iv_xing.setVisibility(truckPic.getMustFlag() == 1 ? View.VISIBLE : View.GONE);
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
            TruckPic selectTruckPic = null;
            for (int i = 0; i < list.size(); i++) {
                TruckPic truckPic = (TruckPic) getItem(i);
                if (i == position){
                    truckPic.setSelected(1);
                    selectTruckPic = truckPic;
                }else{
                    truckPic.setSelected(0);
                }
            }
            notifyDataSetChanged();
            ((UploadActivity)context).setUnSelect(type);
            // 如果当前操作的是没有照片路径的的数据
            if (TextUtils.isEmpty(selectTruckPic.getImgPath())){
                // 则弹出底部弹窗
                List<String> menus = new ArrayList<>();
                menus.add("拍照");
                menus.add("从相册选");
                bottomPop = new BottomPop(context,context.getString(R.string.upload_pic));
                bottomPop.setListMsg(new MenuAdapter(context, menus, position)).init();
                bottomPop.showAtLocation(v, Gravity.BOTTOM,0,0);
            }else{
                // 否则，就是有照片的，则跳转至照片预览界面
                Intent intent = new Intent(context, PicPreViewActivity.class);
                intent.putParcelableArrayListExtra("truckPics",_list);
                intent.putExtra("currentPosition",position);
                intent.putExtra("must",type);
                ((UploadActivity) context).startActivityForResult(intent,0);
            }
        }
    }

    class MenuAdapter extends CommonAdapter {
        // 当前选择项目的下标
        int currentPostion;
        public MenuAdapter(Context _context, List<String> _list, int currentPostion) {
            context = _context;
            list = _list;
            this.currentPostion = currentPostion;
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
                if (bottomPop != null && bottomPop.isShowing()){
                    bottomPop.dismiss();
                }
                if (position == 0){
                    takePhoto(currentPostion);
                }else{
                    UploadActivity uploadActivity = (UploadActivity) context;
                    uploadActivity.setCurrent(_list,TruckPicAdapter.this, currentPostion);
                    toAlbum();
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
     * 拍摄照片
     */
    private void takePhoto(int currentPostion) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, 0x12);
            return;
        }
        CameraActivity.openCertificateCamera((Activity) context, CameraActivity.TYPE_IDCARD_FRONT, _list, currentPostion, type);
    }

    private void toAlbum() {
        PictureSelector.create((Activity) context)
                // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .openGallery(PictureConfig.TYPE_IMAGE)
                // 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .theme(R.style.picture_default_style)
                // 最大图片选择数量
                .maxSelectNum(1)
                // 最小选择数量
                .minSelectNum(1)
                // 每行显示个数
                .imageSpanCount(3)
                // PictureConfig.MULTIPLE 多选 or 单选PictureConfig.SINGLE
                .selectionMode(PictureConfig.SINGLE)
                // 是否可预览图片
                .previewImage(true)
                // 是否可预览视频
//                .previewVideo(true)
                // 是否可播放音频
//                .enablePreviewAudio(true)
                // 是否显示拍照按钮
                .isCamera(false)
                // 图片列表点击 缩放效果 默认true
                .isZoomAnim(true)
                //同步true或异步false 压缩 默认同步
                .synOrAsy(true)
                // glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .glideOverride(160, 160)
                // 小于100kb的图片不压缩
                .minimumCompressSize(100)
                // 最大20秒
                .videoMaxSecond(21)
                // 允许裁剪
                .enableCrop(true)
                // 允许自由设定裁剪框
                .freeStyleCropEnabled(true)
                // 结果回调onActivityResult code
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


}
