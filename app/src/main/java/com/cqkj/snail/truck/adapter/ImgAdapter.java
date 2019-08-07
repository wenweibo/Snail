package com.cqkj.snail.truck.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cqkj.snail.R;
import com.cqkj.snail.truck.entity.ImageViewInfo;
import com.cqkj.snail.truck.entity.TruckBaseEntity;
import com.fxkj.publicframework.adapter.CommonAdapter;
import com.fxkj.publicframework.widget.NoScrollGridView;
import com.xuexiang.xui.widget.imageview.preview.PreviewBuilder;
import com.xuexiang.xui.widget.imageview.preview.loader.GlideMediaLoader;

import java.util.List;

import butterknife.BindView;

/**
 * 图片列表适配器
 * @author 闻维波
 * @since 2019/08/02
 */
public class ImgAdapter extends CommonAdapter {
    List<ImageViewInfo> _list;
    NoScrollGridView ngl_images;
    public ImgAdapter(Context _context, List<ImageViewInfo> _list, NoScrollGridView ngl_images) {
        context = _context;
        list = _list;
        this._list = _list;
        this.ngl_images = ngl_images;
        layoutid = R.layout.item_img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        ViewHolder viewHolder = new ViewHolder(convertView);
        ImageViewInfo imageViewInfo = (ImageViewInfo) getItem(position);
        Glide.with(context)
                .load(imageViewInfo.getUrl())
                .apply(GlideMediaLoader.getRequestOptions())
                .into(viewHolder.iv_img);
        Rect bounds = new Rect();
        viewHolder.iv_img.getGlobalVisibleRect(bounds);
        imageViewInfo.setBounds(bounds);
        convertView.setOnClickListener(new ItemOnClick(position));
        return convertView;
    }

    class ItemOnClick implements View.OnClickListener{
        private int position;

        public ItemOnClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            computeBoundsBackward();
            PreviewBuilder.from((Activity) context)
                    .setImgs(_list)
                    .setCurrentIndex(position)
                    .setType(PreviewBuilder.IndicatorType.Dot)
                    .start();//启动
        }
    }

    class ViewHolder extends CommonAdapter.ViewHolder {
        @BindView(R.id.iv_img)
        ImageView iv_img;
        ViewHolder(View view) {
            super(view);
        }
    }

    /**
     * 查找信息
     */
    private void computeBoundsBackward() {
        for (int i = 0;i < ngl_images.getChildCount(); i++) {
            LinearLayout itemView = (LinearLayout) ngl_images.getChildAt(i);
            Rect bounds = new Rect();
            if (itemView != null) {
                ImageView thumbView = (ImageView) itemView.getChildAt(0);
                thumbView.getGlobalVisibleRect(bounds);
            }
            _list.get(i).setBounds(bounds);
        }

    }
}
