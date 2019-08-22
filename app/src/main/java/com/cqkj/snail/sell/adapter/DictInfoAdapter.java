package com.cqkj.snail.sell.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cqkj.publicframework.adapter.CommonAdapter;
import com.cqkj.snail.R;
import com.cqkj.snail.buy.entity.DictInfoEntity;

import java.util.List;

import butterknife.BindView;

/**
 * 字典列表适配器
 *
 * @author 闻维波
 * @since 2019/08/14
 */
public class DictInfoAdapter extends CommonAdapter {
    private DictInfoInterface dictInfoInterface;
    private PopupWindow popupWindow;

    public DictInfoAdapter(Context _context, List<DictInfoEntity> _list, DictInfoInterface dictInfoInterface, PopupWindow popupWindow) {
        context = _context;
        list = _list;
        this.dictInfoInterface = dictInfoInterface;
        this.popupWindow = popupWindow;
        layoutid = R.layout.item_dictinfo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        ViewHolder viewHolder = new ViewHolder(convertView);
        DictInfoEntity dictInfoEntity = (DictInfoEntity) getItem(position);
        viewHolder.tv_title.setText(dictInfoEntity.getDictName());
        if (dictInfoEntity.getSelectFlag() == 1) {
            viewHolder.tv_title.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            viewHolder.iv_select.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_select.setVisibility(View.GONE);
            viewHolder.tv_title.setTextColor(context.getResources().getColor(R.color.layout_title_color));
        }
        convertView.setOnClickListener(new ItemOnClick(position));
        return convertView;
    }

    class ItemOnClick implements View.OnClickListener {
        DictInfoEntity dictInfoEntity;
        int postion;

        public ItemOnClick(int postion) {
            this.postion = postion;
        }

        @Override
        public void onClick(View v) {
            for (int i = 0; i < list.size(); i++) {
                DictInfoEntity die = (DictInfoEntity) list.get(i);
                if (i == postion) {
                    die.setSelectFlag(1);
                    dictInfoEntity = die;
                } else {
                    die.setSelectFlag(0);
                }
            }
            notifyDataSetChanged();
            dictInfoInterface.onSelect(dictInfoEntity);
            popupWindow.dismiss();
        }
    }

    class ViewHolder extends CommonAdapter.ViewHolder {

        @BindView(R.id.iv_select)
        ImageView iv_select;
        @BindView(R.id.tv_title)
        TextView tv_title;

        ViewHolder(View view) {
            super(view);
        }
    }

    public interface DictInfoInterface {
        void onSelect(DictInfoEntity dictInfoEntity);
    }
}
