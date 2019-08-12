package com.cqkj.snail.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.cqkj.publicframework.widget.NoScrollGridView;
import com.cqkj.snail.R;
import com.cqkj.snail.system.entity.CityEntity;
import com.cqkj.snail.tool.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市列表适配器
 *
 * @author 闻维波
 * @since 2019/08/12 11:43
 */
public class CityExpandAdapter extends BaseExpandableListAdapter implements SectionIndexer, Filterable {
    // 上下文
    private Context context;
    // 源数据
    private List<List<CityEntity>> parentList;
    // 存储原始数据
    private List<CityEntity> cityEntities;
    // 布局加载器
    private LayoutInflater inflater;
    // 城市筛选器
    private CityFilter cityFilter;
    // 选中回调接口
    private CitySelect citySelect;

    public CityExpandAdapter(Context context, List<List<CityEntity>> parentList,
                             List<CityEntity> cityEntities, CitySelect citySelect) {
        this.context = context;
        this.parentList = parentList;
        this.cityEntities = cityEntities;
        this.citySelect = citySelect;
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
        if (parentList != null && groupPosition < parentList.size()) {
            return parentList.get(groupPosition);
        } else {
            return null;
        }
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
        childHolder.ngv_city.setAdapter(new CityChildAdaper(context, parentList.get(groupPosition)));
        // 设置网格视图（城市网格）点击事件
        childHolder.ngv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                citySelect.selectCity(parentList.get(groupPosition).get(position));
            }
        });
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

    @Override
    public Filter getFilter() {
        if (cityFilter == null) {
            cityFilter = new CityFilter();
        }
        return cityFilter;
    }

    class GroupHolder {
        TextView tvLetter;
    }

    class ChildHolder {
        NoScrollGridView ngv_city;
    }


    private class CityFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // 初始化筛选结果对象
            FilterResults filterResults = new FilterResults();
            // 筛选后的城市列表
            List<CityEntity> filterCitys = new ArrayList<>();
            // 如果传入的筛选条件为空，则默认返回源数据
            if (constraint.toString().isEmpty()) {
                filterCitys = cityEntities;
            } else {
                // 遍历源数据，找出符合筛选条件的城市对象
                for (CityEntity cityEntity : cityEntities) {
                    String s = constraint.toString().toUpperCase();
                    // 如果城市名称的拼音首字母是筛选字符或城市名称中包含筛选字符，则说明该城市是需要的城市
                    if (cityEntity.getLetters().startsWith(s) || cityEntity.getName().contains(s)) {
                        filterCitys.add(cityEntity);
                    }
                }
            }
            // 将筛选后的城市对象列表进行排序并且生成组集合
            List<List<CityEntity>> lists = CommonUtil.sortAndGroupCitys(filterCitys);
            if (lists == null) {
                lists = new ArrayList<>();
            }
            filterResults.values = lists;
            filterResults.count = lists.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            parentList = (List<List<CityEntity>>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
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
