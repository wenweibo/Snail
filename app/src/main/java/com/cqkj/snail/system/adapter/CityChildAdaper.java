package com.cqkj.snail.system.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cqkj.publicframework.adapter.CommonAdapter;
import com.cqkj.snail.R;
import com.cqkj.snail.system.entity.CityEntity;

import java.util.List;

import butterknife.BindView;

/**
 * 城市名称列表适配器
 * @author 闻维波
 * @since 2019/08/12 17:21
 */
public class CityChildAdaper extends CommonAdapter {

    public CityChildAdaper(Context _context, List<CityEntity> _list) {
        context = _context;
        list = _list;
        layoutid = R.layout.item_city;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        ViewHolder viewHolder = new ViewHolder(convertView);
        CityEntity cityEntity = (CityEntity) getItem(position);
        viewHolder.tv_name.setText(cityEntity.getName());
        return convertView;
    }

    class ViewHolder extends CommonAdapter.ViewHolder {
        // 城市名称
        @BindView(R.id.tv_name)
        TextView tv_name;

        ViewHolder(View view) {
            super(view);
        }
    }


}
