package com.cqkj.snail.truck.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqkj.snail.R;
import com.cqkj.snail.truck.entity.MenuEntity;
import com.cqkj.snail.truck.entity.TruckBaseEntity;
import com.fxkj.publicframework.adapter.CommonAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * 车辆基本信息适配器
 * @author 闻维波
 * @since 2019/08/02
 */
public class TruckBaseAdapter extends CommonAdapter {

    public TruckBaseAdapter(Context _context, List<TruckBaseEntity> _list) {
        context = _context;
        list = _list;
        layoutid = R.layout.item_base;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        ViewHolder viewHolder = new ViewHolder(convertView);
        TruckBaseEntity truckBaseEntity = (TruckBaseEntity) getItem(position);
        viewHolder.tv_title.setText(truckBaseEntity.getTitle());
        viewHolder.tv_content.setText(truckBaseEntity.getContent());
        if (truckBaseEntity.isFlag()){
            viewHolder.iv_warning.setVisibility(View.VISIBLE);
        }else{
            viewHolder.iv_warning.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder extends CommonAdapter.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_content)
        TextView tv_content;
        @BindView(R.id.iv_warning)
        ImageView iv_warning;

        ViewHolder(View view) {
            super(view);
        }
    }
}
