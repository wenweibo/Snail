package com.cqkj.snail.buytruck.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqkj.snail.R;
import com.cqkj.snail.truck.adapter.FirstMenuAdapter;
import com.cqkj.snail.truck.entity.MenuEntity;
import com.fxkj.publicframework.adapter.CommonAdapter;
import com.fxkj.publicframework.tool.CommonUtil;

import java.util.List;

import butterknife.BindView;

/**
 * 车型列表适配器
 *
 * @author 闻维波 2019/07/31
 */
public class TruckTypeAdapter extends CommonAdapter {

    public TruckTypeAdapter(Context _context, List<MenuEntity> _list) {
        context = _context;
        list = _list;
        layoutid = R.layout.item_truck_type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        ViewHolder viewHolder = new ViewHolder(convertView);
        MenuEntity menuEntity = (MenuEntity) getItem(position);
        //如果是选中的，则赋值为选中的图片
        if (menuEntity.isSelected()) {
            viewHolder.iv_icon.setImageResource(menuEntity.getImgResSelected());
        } else {
            //否则赋值为未选中图片
            viewHolder.iv_icon.setImageResource(menuEntity.getImgRes());
        }
        convertView.setOnClickListener(new ItemOnClick(position));
        return convertView;
    }

    class ItemOnClick implements View.OnClickListener {
        private int position;

        public ItemOnClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            for (int i = 0; i < list.size(); i++) {
                MenuEntity menuEntity = (MenuEntity) list.get(i);
                if (i == position) {
                    menuEntity.setSelected(!menuEntity.isSelected());
                } else {
                    menuEntity.setSelected(false);
                }
            }
            notifyDataSetChanged();
        }
    }

    class ViewHolder extends CommonAdapter.ViewHolder {

        @BindView(R.id.iv_icon)
        ImageView iv_icon;

        ViewHolder(View view) {
            super(view);
        }
    }
}
