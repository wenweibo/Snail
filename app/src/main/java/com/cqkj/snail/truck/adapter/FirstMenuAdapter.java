package com.cqkj.snail.truck.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqkj.snail.R;
import com.cqkj.snail.truck.entity.MenuEntity;
import com.fxkj.publicframework.adapter.CommonAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * 首页筛选菜单列表适配器
 * @author 闻维波 2019/07/31
 */
public class FirstMenuAdapter extends CommonAdapter {

    public FirstMenuAdapter(Context _context, List<MenuEntity> _list) {
        context = _context;
        list = _list;
        layoutid = R.layout.item_menu;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        ViewHolder viewHolder = new ViewHolder(convertView);
        MenuEntity menuEntity = (MenuEntity) getItem(position);
        //如果有图片数据，则显示图片控件
        if (menuEntity.getImgRes()>0){
//            viewHolder.lin_item.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,600));
            viewHolder.iv_pic.setVisibility(View.VISIBLE);
            viewHolder.iv_pic.setImageResource(menuEntity.getImgRes());
        }else{
            //否则，就隐藏图片控件
            viewHolder.iv_pic.setVisibility(View.GONE);
        }
        viewHolder.tv_title.setText(menuEntity.getTitle());
        return convertView;
    }

    class ViewHolder extends CommonAdapter.ViewHolder {
        @BindView(R.id.lin_item)
        LinearLayout lin_item;
        @BindView(R.id.iv_pic)
        ImageView iv_pic;
        @BindView(R.id.tv_title)
        TextView tv_title;

        ViewHolder(View view) {
            super(view);
        }
    }
}