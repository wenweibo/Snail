package com.cqkj.snail.system.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cqkj.publicframework.adapter.CommonAdapter;
import com.cqkj.snail.AppApplication;
import com.cqkj.snail.R;
import com.cqkj.snail.system.entity.CityEntity;

import java.util.List;

import butterknife.BindView;

/**
 * 城市名称列表适配器
 *
 * @author 闻维波
 * @since 2019/08/12 17:21
 */
public class CityChildAdaper extends CommonAdapter {
    private CityExpandAdapter.CitySelect citySelect;
    // 是否是多选
    private boolean isMultiple;
    private List<List<CityEntity>> parentList;

    public CityChildAdaper(Context _context, List<List<CityEntity>> parentList,
                           List<CityEntity> _list, boolean isMultiple,
                           CityExpandAdapter.CitySelect citySelect) {
        context = _context;
        list = _list;
        this.citySelect = citySelect;
        this.isMultiple = isMultiple;
        this.parentList = parentList;
        layoutid = R.layout.item_city;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        ViewHolder viewHolder = new ViewHolder(convertView);
        CityEntity cityEntity = (CityEntity) getItem(position);
        viewHolder.tv_name.setText(cityEntity.getName());
        if (cityEntity.getSelectFlag() == 1) {
            viewHolder.tv_name.setBackgroundResource(R.drawable.shape_red_stroke_shallow_red_solid);
        } else {
            viewHolder.tv_name.setBackgroundResource(R.drawable.shape_gray_stroke);
        }
        convertView.setOnClickListener(new ItemClick(cityEntity));
        return convertView;
    }

    class ItemClick implements View.OnClickListener {
        CityEntity cityEntity;

        public ItemClick(CityEntity cityEntity) {
            this.cityEntity = cityEntity;
        }

        @Override
        public void onClick(View v) {
            // 城市单选
            if (!isMultiple) {
                cityEntity.setSelectFlag(1);
                citySelect.selectCity(cityEntity);
                // 将其他的城市选中都设为0
                for (List<CityEntity> cityEntities : parentList) {
                    for (CityEntity cityEntity1 : cityEntities) {
                        if (!cityEntity1.getAdcode().equals(cityEntity.getAdcode())) {
                            cityEntity1.setSelectFlag(0);
                        }
                    }
                }
            } else {
                // 城市多选
                if (cityEntity.getSelectFlag() == 0) {
                    cityEntity.setSelectFlag(1);
                } else {
                    cityEntity.setSelectFlag(0);
                }
                notifyDataSetChanged();
            }
        }
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
