package com.cqkj.snail.sell.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;

import com.cqkj.publicframework.activity.BaseTitleActivity;
import com.cqkj.publicframework.widget.contactar.SideBar;
import com.cqkj.snail.AppApplication;
import com.cqkj.snail.R;
import com.cqkj.snail.buy.entity.DictInfoEntity;
import com.cqkj.snail.config.DictInfo;
import com.cqkj.snail.config.ResultCode;
import com.cqkj.snail.sell.adapter.DictInfoAdapter;
import com.cqkj.snail.sell.adapter.PlaceListAdapter;
import com.cqkj.snail.system.entity.CityEntity;
import com.cqkj.snail.tool.CommonUtil;

import java.util.List;

import butterknife.BindView;

/**
 * 看车地点选择页面
 *
 * @author 闻维波
 * @since 2019/08/20 15:13
 */
public class CarWatchPlaceSelectActivity extends BaseTitleActivity implements PlaceListAdapter.PlaceSelect {
    // 省份列表
    @BindView(R.id.elv_city)
    ExpandableListView elvBrand;
    // 侧边字母提示
    @BindView(R.id.sideBar)
    SideBar sideBar;

    // 品牌列表适配器
    private PlaceListAdapter placeListAdapter;
    private List<List<CityEntity>> groupList;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_city_select;
    }

    @Override
    protected void initView() {
        super.initView();
        title_text.setText(R.string.city_select);
        back.setVisibility(View.VISIBLE);
        title_do.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        super.initData();
        groupList = CommonUtil.sortAndGroupCitys(AppApplication.cityEntities);
        placeListAdapter = new PlaceListAdapter(this, groupList, this);
        elvBrand.setAdapter(placeListAdapter);
        // 默认展开全部
        for (int i = 0; i <groupList.size(); i++) {
            elvBrand.expandGroup(i);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                if (s.equals("↑")) {
                    elvBrand.setSelection(0);
                } else {
                    //该字母首次出现的位置
                    int position = placeListAdapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        elvBrand.setSelection(position);
                    }
                }
            }
        });
        // 设置抽屉控件不能点击收缩
        elvBrand.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
    }
    @Override
    public void onSelect(CityEntity cityEntity) {
        // 选择完车系，设置回调
        Intent intent = new Intent();
        intent.putExtra("cityEntity", cityEntity);
        setResult(ResultCode.SELECT_PLACE, intent);
        finish();
    }
}
