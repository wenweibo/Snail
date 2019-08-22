package com.cqkj.snail.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.cqkj.publicframework.widget.NoScrollGridView;
import com.cqkj.snail.R;
import com.cqkj.snail.system.entity.CityEntity;

import java.util.List;

/**
 * 城市列表适配器
 *
 * @author 闻维波
 * @since 2019/08/12 11:43
 */
public class CityExpandAdapter extends BaseExpandableListAdapter implements SectionIndexer {
    // 上下文
    private Context context;
    // 源数据
    private List<List<CityEntity>> parentList;
    // 布局加载器
    private LayoutInflater inflater;
    // 选中回调接口
    private CitySelect citySelect;
    // 是否是多选
    private boolean isMultiple;

    public CityExpandAdapter(Context context, List<List<CityEntity>> parentList,
                             boolean isMultiple, CitySelect citySelect) {
        this.context = context;
        this.parentList = parentList;
        this.citySelect = citySelect;
        this.isMultiple = isMultiple;
        // 初始化布局加载器
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        if (parentList != null) {
            return parentList.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // 默认子项数量为1，是因为需要在布局中做成网格样式
        return 1;
    }

    @Override
    public List<CityEntity> getGroup(int groupPosition) {
        return parentList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder = null;
        if (convertView == null) {
            groupHolder = new GroupHolder();
            convertView = inflater.inflate(R.layout.group_city, null);
            groupHolder.tvLetter = convertView.findViewById(R.id.tv_letter);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }

        List<CityEntity> cityEntities = getGroup(groupPosition);
        // 每一个组视图，都获取本组第一个城市对象，获取其首字母，并显示
        if (cityEntities != null) {
            CityEntity cityEntity = cityEntities.get(0);
            groupHolder.tvLetter.setText(cityEntity.getLetters().charAt(0) + "");
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder = null;
        if (convertView == null) {
            childHolder = new ChildHolder();
            convertView = inflater.inflate(R.layout.item_child_city, null);
            childHolder.ngv_city = convertView.findViewById(R.id.ngv_city);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        // 设置网格视图适配器
        childHolder.ngv_city.setAdapter(new CityChildAdaper(context, parentList,
                getGroup(groupPosition), isMultiple, citySelect));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    /**
     * 获取字母首显位置
     *
     * @param sectionIndex
     * @return
     */
    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getGroupCount(); i++) {
            String sortStr = getGroup(i).get(0).getLetters();
            char firstChar = sortStr.charAt(0);
            if (Character.toUpperCase(firstChar) == Character.toUpperCase(sectionIndex)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }


    class GroupHolder {
        TextView tvLetter;
    }

    class ChildHolder {
        NoScrollGridView ngv_city;
    }

    public interface CitySelect {
        /**
         * 单选用
         *
         * @param cityEntity
         */
        void selectCity(CityEntity cityEntity);
    }
}
