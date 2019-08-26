package com.cqkj.snail.system.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.cqkj.publicframework.tool.ToastUtil;
import com.cqkj.publicframework.widget.contactar.SideBar;
import com.cqkj.snail.AppApplication;
import com.cqkj.snail.R;
import com.cqkj.snail.system.activity.CitySelectActivity;
import com.cqkj.snail.system.adapter.CityExpandAdapter;
import com.cqkj.snail.system.callbackevent.LocationEvent;
import com.cqkj.snail.system.callbackevent.RefreshEvent;
import com.cqkj.snail.system.entity.CityEntity;
import com.cqkj.snail.tool.CommonUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 省份选择 碎片页面
 *
 * @author 闻维波
 * @since 2019/08/16 16:41
 */
public class ProvinceFragment extends Fragment implements CityExpandAdapter.CitySelect,
        View.OnClickListener {
    // 城市列表
    @BindView(R.id.elv_city)
    ExpandableListView elvCity;
    // 侧边字母提示
    @BindView(R.id.sideBar)
    SideBar sideBar;
    // 确定查询按钮
    @BindView(R.id.btn_sure)
    Button btnSure;
    // 当前定位城市文本
    TextView tvLocation;
    // 搜索框
    EditText etSearch;
    // 新的侧边字母
    private String[] a = {"↑", "a", "b", "c", "d", "e", "f", "g", "h", "i",
            "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
            "w", "x", "y", "z"};
    private List<List<CityEntity>> parentList = new ArrayList<>();
    private CityExpandAdapter cityExpandAdapter;

    public static ProvinceFragment newInstance() {
        ProvinceFragment fragment = new ProvinceFragment();
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CommonUtil.evenUnregister(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtil.evenRegister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        initListener();
        loadData();
        return view;
    }

    private void initView() {
        sideBar.setTextList(a);
        // 设置列表标头，也就是定位及热门城市数据
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.header_city, null);
        // 初始化搜索框
        etSearch = view.findViewById(R.id.et_search);
        tvLocation = view.findViewById(R.id.tv_location);
        elvCity.addHeaderView(view);
    }

    private void initData() {
        // 如果有定位城市信息
        if (AppApplication.locationCity != null) {
            // 则显示定位城市名称
            tvLocation.setText(AppApplication.locationCity.getParentName());
            // 如果有定位省份列表信息
            if (AppApplication.selectCitys != null) {
                // 如果已选的城市列表中，包含当前定位的省份
                boolean isSelectContansLocation = false;
                for (CityEntity cityEntity : AppApplication.selectCitys) {
                    if (cityEntity.getAdcode().equals(AppApplication.locationCity.getParentId())) {
                        isSelectContansLocation = true;

                    }
                }
                if (isSelectContansLocation) {
                    // 则将定位城市背景设为已选
                    AppApplication.locationCity.setSelectFlag(1);
                    tvLocation.setBackgroundResource(R.drawable.shape_red_stroke_shallow_red_solid);
                } else {
                    // 否则，设为未选
                    AppApplication.locationCity.setSelectFlag(0);
                    tvLocation.setBackgroundResource(R.drawable.shape_gray_stroke);
                }
            } else {
                // 否则,如果有单选定位城市信息
                if (AppApplication.selectCity != null
                        && AppApplication.selectCity.getParentId()
                        .equals(AppApplication.locationCity.getParentId())) {
                    // 则将定位城市背景设为已选
                    tvLocation.setBackgroundResource(R.drawable.shape_red_stroke_shallow_red_solid);
                } else {
                    // 否则，设为未选
                    tvLocation.setBackgroundResource(R.drawable.shape_gray_stroke);
                }
            }
        } else {
            // 否则显示定位中...
            tvLocation.setText(R.string.locationing);
            // 重新进行定位
            LocationClient mLocationClient = CommonUtil.initLocation(getContext());
            mLocationClient.start();
        }


        List<List<CityEntity>> lists = CommonUtil.sortAndGroupCitys(AppApplication.cityEntities);
        parentList.clear();
        parentList.addAll(lists);
        elvCity.setAdapter(cityExpandAdapter = new CityExpandAdapter(getContext(), parentList,
                true, this));
        // 默认展开全部
        for (int i = 0; i < parentList.size(); i++) {
            elvCity.expandGroup(i);
        }
    }

    private void initListener() {
        btnSure.setOnClickListener(this);
        tvLocation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_sure) {
            // 确定按钮
            sureSelect();
        }else if (v.getId() == R.id.tv_location){
            // 定位按钮
            if (AppApplication.locationCity.getSelectFlag() == 0){
                // 则将定位城市背景设为已选
                tvLocation.setBackgroundResource(R.drawable.shape_red_stroke_shallow_red_solid);
                AppApplication.locationCity.setSelectFlag(1);

            }else{
                // 否则，设为未选
                AppApplication.locationCity.setSelectFlag(0);
                tvLocation.setBackgroundResource(R.drawable.shape_gray_stroke);
            }
            for (CityEntity cityEntity:AppApplication.cityEntities) {
                if (AppApplication.locationCity.getParentId().equals(cityEntity.getAdcode())){
                    cityEntity.setSelectFlag(AppApplication.locationCity.getSelectFlag());
                }
            }
            cityExpandAdapter.notifyDataSetChanged();
        }

    }

    private void loadData() {
    }

    @Override
    public void selectCity(CityEntity cityEntity) {
        // 如果当前操作的省份就是定位城市
        if (cityEntity.getAdcode().equals(AppApplication.locationCity.getParentId())){
            // 则根据选中情况设置定位城市选中状态
            if (cityEntity.getSelectFlag() == 1) {
                // 则将定位城市背景设为已选
                AppApplication.locationCity.setSelectFlag(1);
                tvLocation.setBackgroundResource(R.drawable.shape_red_stroke_shallow_red_solid);
            } else {
                // 否则，设为未选
                AppApplication.locationCity.setSelectFlag(0);
                tvLocation.setBackgroundResource(R.drawable.shape_gray_stroke);
            }
        }
    }

    private void sureSelect() {
        AppApplication.selectCitys = new ArrayList<>();
        // 遍历取出选中的省份
        for (CityEntity cityEntity1 : AppApplication.cityEntities) {
            if (cityEntity1.getSelectFlag() == 1) {
                AppApplication.selectCitys.add(cityEntity1);
            }
        }
        // 如果有选择省份
        if (!AppApplication.selectCitys.isEmpty()) {
            // 关闭当前页面
            Intent intent = new Intent();
            getActivity().setResult(CitySelectActivity.RESULT_CODE_SELECT_CITY, intent);
            getActivity().finish();
            EventBus.getDefault().post(new RefreshEvent(0));
        } else {
            // 没有选择城市，则提示
            ToastUtil.showShort(getContext(), R.string.please_choose_at_least_one);
        }
    }

    /**
     * 清空已选
     */
    public void clearData() {
        if (AppApplication.cityEntities != null) {
            for (CityEntity cityEntity : AppApplication.cityEntities) {
                cityEntity.setSelectFlag(0);
            }
            cityExpandAdapter.notifyDataSetChanged();
        }
        if (AppApplication.locationCity != null) {
            AppApplication.locationCity.setSelectFlag(0);
            tvLocation.setBackgroundResource(R.drawable.shape_gray_stroke);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LocationEvent event) {
        //收到此广播，刷新位置信息
        if (AppApplication.locationCity != null) {
            tvLocation.setText(com.cqkj.publicframework.tool.CommonUtil.
                    changeStringEllipsis(AppApplication.locationCity.getName()));
        }
    }

}
