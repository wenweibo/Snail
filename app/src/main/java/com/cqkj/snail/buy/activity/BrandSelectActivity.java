package com.cqkj.snail.buy.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;

import com.cqkj.publicframework.activity.BaseTitleActivity;
import com.cqkj.publicframework.widget.contactar.SideBar;
import com.cqkj.snail.R;
import com.cqkj.snail.buy.adapter.BrandListAdapter;
import com.cqkj.snail.buy.entity.DictInfoEntity;
import com.cqkj.snail.config.DictInfo;
import com.cqkj.snail.config.ResultCode;
import com.cqkj.snail.sell.adapter.DictInfoAdapter;
import com.cqkj.snail.tool.CommonUtil;

import butterknife.BindView;

/**
 * 品牌选择页面
 *
 * @author 闻维波
 * @since 2019/08/15 09:06
 */
public class BrandSelectActivity extends BaseTitleActivity implements DictInfoAdapter.DictInfoInterface {
    // 品牌列表
    @BindView(R.id.elv_city)
    ExpandableListView elvBrand;
    // 侧边字母提示
    @BindView(R.id.sideBar)
    SideBar sideBar;

    // 品牌列表适配器
    private BrandListAdapter brandListAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_city_select;
    }

    @Override
    protected void initView() {
        super.initView();
        title_text.setText(R.string.brand_select);
        back.setVisibility(View.VISIBLE);
        title_do.setVisibility(View.GONE);

    }

    @Override
    protected void initData() {
        super.initData();
        initBrand();
        boolean openSecond = getIntent().getExtras().getBoolean("openSecond");
        brandListAdapter = new BrandListAdapter(this, DictInfo.vehicleBrandsGroup,
                openSecond, this);
        elvBrand.setAdapter(brandListAdapter);
        // 默认展开全部
        for (int i = 0; i < DictInfo.vehicleBrandsGroup.size(); i++) {
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
                    int position = brandListAdapter.getPositionForSection(s.charAt(0));
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

    /**
     * 初始化品牌列表
     */
    private void initBrand() {
        // 如果有品牌分组列表，则不再做下面的分组操作
        if (DictInfo.vehicleBrandsGroup != null) {
            return;
        }
        // 进行品牌拼音赋值、排序、分组
        CommonUtil.sortAndGroupDict(DictInfo.vehicleBrands);
    }


    @Override
    public void onSelect(DictInfoEntity dictInfoEntity) {
        // 选择完车系，设置回调
        Intent intent = new Intent();
        intent.putExtra("dictInfoEntity", dictInfoEntity);
        setResult(ResultCode.SELECT_BRAND, intent);
        finish();
    }
}
