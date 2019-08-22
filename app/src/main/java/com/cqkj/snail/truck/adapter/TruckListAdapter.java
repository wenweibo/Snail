package com.cqkj.snail.truck.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cqkj.publicframework.tool.CommonUtil;
import com.cqkj.snail.R;
import com.cqkj.snail.buy.entity.TruckPicEntity;
import com.cqkj.snail.truck.activity.TruckDetailActivity;
import com.cqkj.snail.config.PublishStatus;
import com.cqkj.snail.truck.entity.TruckEntity;
import com.cqkj.publicframework.adapter.CommonAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * 货车列表适配器
 *
 * @author 闻维波 2019/08/01
 */
public class TruckListAdapter extends CommonAdapter {
    private RequestOptions mRequestOptions;

    public TruckListAdapter(Context _context, List<TruckEntity> _list) {
        context = _context;
        list = _list;
        layoutid = R.layout.item_truck;
        mRequestOptions = new RequestOptions()
                .error(R.mipmap.defaultimage)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        ViewHolder viewHolder = new ViewHolder(convertView);
        TruckEntity truckEntity = (TruckEntity) getItem(position);

        // 加载车辆图片
        List<TruckPicEntity> attachmentPic = truckEntity.getAttachmentPic();
        String picPath = "";
        if (attachmentPic != null && !attachmentPic.isEmpty()) {
            picPath = attachmentPic.get(0).getSavePath();
        }
        Glide.with(context)
                .asBitmap()
                .apply(mRequestOptions)
                .load(picPath).into(viewHolder.iv_truck);
        // 如果降价标签没有给出降价额度
//        if (TextUtils.isEmpty(truckEntity.getCutPrice())){
//            // 则将降价标签隐藏
//            viewHolder.tv_cut_price.setVisibility(View.GONE);
//        }else{
//            // 否则显示降价标签，并填入降价额度
//            viewHolder.tv_cut_price.setVisibility(View.VISIBLE);
//            viewHolder.tv_cut_price.setText("已降" + truckEntity.getCutPrice() + "元");
//        }

        // 如果发布状态为“1”，则说明已售出
        if (PublishStatus.SOLD.equals(truckEntity.getStatus())) {
            // 显示售出标记
            viewHolder.tv_sell.setVisibility(View.VISIBLE);
        } else {
            // 隐藏售出标记
            viewHolder.tv_sell.setVisibility(View.GONE);
        }

        // 如果新品标记不为空
//        if (!TextUtils.isEmpty(truckEntity.getNewTruck())){
//            // 显示新品标记
//            viewHolder.iv_new.setVisibility(View.VISIBLE);
//        }else{
//            // 隐藏新品标记
//            viewHolder.iv_new.setVisibility(View.GONE);
//        }

        viewHolder.tv_title.setText(truckEntity.getVehicleBrandContent() +
                truckEntity.getVehicleTypeContent() + " " + truckEntity.getHorsePower());
        viewHolder.tv_remark.setText(truckEntity.getCreateTime() + " " +
                truckEntity.getDrivingModeContent() + " " + truckEntity.getEmissionStandardContent() +
                "/" + CommonUtil.changeStringNotNull(truckEntity.getCarWatchingPlaceContent()));
        viewHolder.tv_price.setText(truckEntity.getPrice() + "万");

        // 如果议价标记为“1”
//        if ("1".equals(truckEntity.getBargainingFlag())) {
//            // 则显示可议价标记
//            viewHolder.tv_bargaining.setVisibility(View.VISIBLE);
//        } else {
//            // 否则，隐藏可议价标记
//            viewHolder.tv_bargaining.setVisibility(View.GONE);
//        }
        convertView.setOnClickListener(new ItemOnClick(truckEntity));
        return convertView;
    }

    class ItemOnClick implements View.OnClickListener {
        private TruckEntity truckEntity;

        ItemOnClick(TruckEntity truckEntity) {
            this.truckEntity = truckEntity;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, TruckDetailActivity.class);
            intent.putExtra("truckEntity", truckEntity);
            context.startActivity(intent);
        }
    }

    class ViewHolder extends CommonAdapter.ViewHolder {
        // 货车图片
        @BindView(R.id.iv_truck)
        ImageView iv_truck;
        // 降价标记
        @BindView(R.id.tv_cut_price)
        TextView tv_cut_price;
        // 出售标记
        @BindView(R.id.tv_sell)
        TextView tv_sell;
        // 新品标记
        @BindView(R.id.iv_new)
        ImageView iv_new;
        // 标题
        @BindView(R.id.tv_title)
        TextView tv_title;
        // 说明
        @BindView(R.id.tv_remark)
        TextView tv_remark;
        // 价格
        @BindView(R.id.tv_price)
        TextView tv_price;
        // 议价标记
        @BindView(R.id.tv_bargaining)
        TextView tv_bargaining;

        ViewHolder(View view) {
            super(view);
        }
    }
}
