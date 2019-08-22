package com.cqkj.snail.buy.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.cqkj.publicframework.adapter.CommonAdapter;
import com.cqkj.publicframework.tool.CommonUtil;
import com.cqkj.snail.R;
import com.cqkj.snail.buy.entity.DictInfoEntity;
import com.cqkj.snail.sell.adapter.DictInfoAdapter;
import com.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 品牌列表适配器
 *
 * @author 闻维波
 * @since 2019/08/15 09:23
 */
public class BrandListAdapter extends BaseExpandableListAdapter implements SectionIndexer {
    private Context context;
    private List<List<DictInfoEntity>> groupList;
    private LayoutInflater inflater;
    // 车系选中回调
    private DictInfoAdapter.DictInfoInterface dictInfoInterface;
    // 侧滑菜单
    private SlidingMenu slidingMenu;
    // 车系列表
    private List<DictInfoEntity> vehiecleSystems = new ArrayList<>();
    // 车系列表适配器
    private VehicleBrandAdapter vehicleSystemAdapter;
    // 侧滑菜单标题
    private TextView tv_title;
    // 是否打开二级菜单
    private boolean openSecond;

    public BrandListAdapter(Context context, List<List<DictInfoEntity>> groupList,
                            boolean openSecond,
                            DictInfoAdapter.DictInfoInterface dictInfoInterface) {
        this.context = context;
        this.groupList = groupList;
        this.dictInfoInterface = dictInfoInterface;
        this.openSecond = openSecond;
        inflater = LayoutInflater.from(context);
        setSlide();
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupList.get(groupPosition).size();
    }

    @Override
    public List<DictInfoEntity> getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public DictInfoEntity getChild(int groupPosition, int childPosition) {
        return groupList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getGroupCount(); i++) {
            String sortStr = getGroup(i).get(0).getLetter();
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

        List<DictInfoEntity> dictInfoEntities = getGroup(groupPosition);
        // 每一个组视图，都获取本组第一个城市对象，获取其首字母，并显示
        if (dictInfoEntities != null) {
            DictInfoEntity dictInfoEntity = dictInfoEntities.get(0);
            groupHolder.tvLetter.setText(dictInfoEntity.getLetter().charAt(0) + "");
        }
        return convertView;
    }

    class ChildHodler {
        TextView tv_name;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHodler childHodler = null;
        if (convertView == null) {
            childHodler = new ChildHodler();
            convertView = inflater.inflate(R.layout.child_brand, null);
            childHodler.tv_name = convertView.findViewById(R.id.tv_name);
            convertView.setTag(childHodler);
        } else {
            childHodler = (ChildHodler) convertView.getTag();
        }
        DictInfoEntity dictInfoEntity = getChild(groupPosition, childPosition);
        childHodler.tv_name.setText(dictInfoEntity.getDictName());
        convertView.setOnClickListener(new ChildItemClick(dictInfoEntity));
        return convertView;
    }

    class ChildItemClick implements View.OnClickListener {
        DictInfoEntity dictInfoEntity;

        public ChildItemClick(DictInfoEntity dictInfoEntity) {
            this.dictInfoEntity = dictInfoEntity;
        }

        @Override
        public void onClick(View v) {
            // 如果运行打开二级菜单
            if (openSecond) {
                // 如果品牌下有车系列表，则打开侧滑列表
                if (dictInfoEntity.getChildren() != null && !dictInfoEntity.getChildren().isEmpty()) {
                    vehiecleSystems.clear();
                    vehiecleSystems.addAll(dictInfoEntity.getChildren());
                    vehicleSystemAdapter.notifyDataSetChanged();
                    // 给侧滑菜单设置品牌实体
                    vehicleSystemAdapter.setBrand(dictInfoEntity);
                    // 设置侧滑标题
                    tv_title.setText(dictInfoEntity.getDictName());
                    //打开右侧菜单
                    slidingMenu.showSecondaryMenu();
                }
            }else{
                dictInfoInterface.onSelect(dictInfoEntity);
            }
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    private void setSlide() {
        // 初始化SlidingMenu
        slidingMenu = new SlidingMenu(context);
        // 设置侧拉模式--left(左侧菜单)
        slidingMenu.setMode(SlidingMenu.RIGHT);
        // 设定触摸侧拉区域
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        // 设定菜单的宽度--反向设定
        slidingMenu.setBehindOffset(CommonUtil.getScreen(context)[0] / 2);
        // 设定阴影
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        slidingMenu.setShadowWidth(5);
        // 设置左侧菜单的样式
        LinearLayout slidRight = (LinearLayout) inflater.inflate(R.layout.sliding_left_vehicle_system, null);
        // 车系列表
        ListView lv_vehicle_system = slidRight.findViewById(R.id.lv_vehicle_system);
        lv_vehicle_system.setAdapter(vehicleSystemAdapter = new VehicleBrandAdapter(context, vehiecleSystems));
        // 侧滑菜单标题
        tv_title = slidRight.findViewById(R.id.tv_title);
        slidingMenu.setSecondaryMenu(slidRight);
        // 设置关联Activity
        slidingMenu.attachToActivity((Activity) context, SlidingMenu.SLIDING_CONTENT);
    }

    class VehicleBrandAdapter extends CommonAdapter {
        // 品牌实体
        private DictInfoEntity brand;

        public VehicleBrandAdapter(Context context, List<DictInfoEntity> list) {
            this.context = context;
            this.list = list;
            layoutid = R.layout.child_brand;
        }

        public void setBrand(DictInfoEntity brand) {
            this.brand = brand;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);
            ViewHolder viewHolder = new ViewHolder(convertView);
            DictInfoEntity dictInfoEntity = (DictInfoEntity) getItem(position);
            viewHolder.tvName.setText(dictInfoEntity.getDictName());
            convertView.setOnClickListener(new ItemClick(dictInfoEntity));
            return convertView;
        }

        class ItemClick implements View.OnClickListener {
            DictInfoEntity dictInfoEntity;

            public ItemClick(DictInfoEntity dictInfoEntity) {
                this.dictInfoEntity = dictInfoEntity;
            }

            @Override
            public void onClick(View v) {
                DictInfoEntity newDie = new DictInfoEntity();
                newDie.setParentId(brand.getParentId());
                // 将品牌code和车系code组合传递
                newDie.setDictCode(brand.getDictCode() + "," + dictInfoEntity.getDictCode());
                newDie.setDictName(brand.getDictName() + dictInfoEntity.getDictName());
                dictInfoInterface.onSelect(newDie);
                slidingMenu.showContent();
            }
        }

        class ViewHolder extends CommonAdapter.ViewHolder {
            @BindView(R.id.tv_name)
            TextView tvName;

            ViewHolder(View view) {
                super(view);
            }
        }

    }
}
