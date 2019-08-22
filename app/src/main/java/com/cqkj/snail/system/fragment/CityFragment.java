package com.cqkj.snail.system.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 城市选择 碎片页面
 *
 * @author 闻维波
 * @since 2019/08/16 16:41
 */
public class CityFragment extends Fragment implements CityExpandAdapter.CitySelect, View.OnClickListener {
    // 城市列表
    @BindView(R.id.elv_city)
    ExpandableListView elvCity;
    // 侧边字母提示
    @BindView(R.id.sideBar)
    SideBar sideBar;
    // 确认选择按钮
    @BindView(R.id.btn_sure)
    Button btnSure;
    // 当前定位城市文本
    TextView tvLocation;
    // 搜索框
    EditText etSearch;
    private List<CityEntity> cityEntities;
    private List<List<CityEntity>> parentList = new ArrayList<>();
    private CityExpandAdapter cityExpandAdapter;
    // 新的侧边字母
    private String[] a = {"↑", "a", "b", "c", "d", "e", "f", "g", "h", "i",
            "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
            "w", "x", "y", "z"};
    // 选中的城市
    private CityEntity selectCityEntity;

    public static CityFragment newInstance() {
        CityFragment fragment = new CityFragment();
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
        // 初始化城市数据
        Map<String, Object> map = CommonUtil.initCitys();
        cityEntities = (List<CityEntity>) map.get("cityEntities");
        List<List<CityEntity>> lists = (List<List<CityEntity>>) map.get("lists");
        parentList.clear();
        parentList.addAll(lists);
        // 如果有定位城市列表信息
        if (AppApplication.locationCity != null) {
            // 则显示定位城市名称
            tvLocation.setText(AppApplication.locationCity.getName());
            if (AppApplication.selectCitys != null) {
                // 如果已选的城市列表中，包含当前定位的城市
                boolean isSelectContansLocation = false;
                for (CityEntity cityEntity : AppApplication.selectCitys) {
                    if (cityEntity.getAdcode().equals(AppApplication.locationCity.getAdcode())) {
                        isSelectContansLocation = true;
                    }
                }
                if (isSelectContansLocation) {
                    // 则将定位城市背景设为已选
                    tvLocation.setBackgroundResource(R.drawable.shape_red_stroke_shallow_red_solid);
                } else {
                    // 否则，设为未选
                    tvLocation.setBackgroundResource(R.drawable.shape_gray_stroke);
                }
            } else {
                // 否则，如果有单选城市信息，且单选的城市编码与定位的传给你说编码一致
                if (AppApplication.selectCity != null
                        && AppApplication.selectCity.getAdcode()
                        .equals(AppApplication.locationCity.getAdcode())) {
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


        elvCity.setAdapter(cityExpandAdapter = new CityExpandAdapter(getContext(), parentList,
                true, this));
        // 默认展开全部
        for (int i = 0; i < parentList.size(); i++) {
            elvCity.expandGroup(i);
        }
    }

    private void initListener() {
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                if (s.equals("↑")) {
                    elvCity.setSelection(0);
                } else {
                    //该字母首次出现的位置
                    int position = cityExpandAdapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        elvCity.setSelection(position);
                    }
                }
            }
        });
        // 设置抽屉控件不能点击收缩
        elvCity.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        // 设置搜索字符改变监听
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 遍历源数据，找出符合筛选条件的城市对象
                // 筛选后的城市列表
                List<CityEntity> filterCitys = new ArrayList<>();
                if (s.toString().isEmpty()) {
                    filterCitys = cityEntities;
                } else {
                    for (CityEntity cityEntity : cityEntities) {
                        String str = s.toString().toUpperCase();
                        // 如果城市名称的拼音首字母是筛选字符或城市名称中包含筛选字符，则说明该城市是需要的城市
                        if (cityEntity.getLetters().startsWith(str) || cityEntity.getName().contains(s)) {
                            filterCitys.add(cityEntity);
                        }
                    }
                }
                // 将筛选后的城市对象列表进行排序并且生成组集合
                List<List<CityEntity>> lists = CommonUtil.sortAndGroupCitys(filterCitys);
                if (lists == null) {
                    lists = new ArrayList<>();
                }
                parentList.clear();
                parentList.addAll(lists);
                cityExpandAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 默认展开全部
                for (int i = 0; i < parentList.size(); i++) {
                    elvCity.expandGroup(i);
                }
            }
        });
        tvLocation.setOnClickListener(this);
        btnSure.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_location:

                break;
            case R.id.btn_sure:
                // 确认查询按钮
                selectCity(null);
                break;
        }
    }

    private void loadData() {
    }


    @Override
    public void selectCity(CityEntity cityEntity) {
        AppApplication.selectCitys = new ArrayList<>();
        // 遍历取出选中的城市
        for (CityEntity cityEntity1 : cityEntities) {
            if (cityEntity1.getSelectFlag() == 1) {
                AppApplication.selectCitys.add(cityEntity1);
            }
        }
        // 关闭当前页面
        Intent intent = new Intent();
        getActivity().setResult(CitySelectActivity.RESULT_CODE_SELECT_CITY, intent);
        getActivity().finish();
        EventBus.getDefault().post(new RefreshEvent(0));
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
