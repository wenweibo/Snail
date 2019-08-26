package com.cqkj.snail.system.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.cqkj.publicframework.activity.BaseTitleActivity;
import com.cqkj.publicframework.tool.ToastUtil;
import com.cqkj.publicframework.widget.contactar.SideBar;
import com.cqkj.snail.AppApplication;
import com.cqkj.snail.R;
import com.cqkj.snail.system.adapter.CityExpandAdapter;
import com.cqkj.snail.system.callbackevent.LocationEvent;
import com.cqkj.snail.system.entity.CityEntity;
import com.cqkj.snail.tool.CommonUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 城市选择页面
 *
 * @author 闻维波
 * @since 2019/08/12 08:55
 */
public class CitySelectActivity extends BaseTitleActivity implements CityExpandAdapter.CitySelect {
    // 城市列表
    @BindView(R.id.elv_city)
    ExpandableListView elvCity;
    // 侧边字母提示
    @BindView(R.id.sideBar)
    SideBar sideBar;
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
    // 回调码
    public static final int RESULT_CODE_SELECT_CITY = 0X100;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonUtil.evenUnregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtil.evenRegister(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_city_select;
    }

    @Override
    protected void initView() {
        super.initView();
        title_text.setText(R.string.select_city);
        back.setVisibility(View.VISIBLE);
        title_do.setVisibility(View.GONE);
        sideBar.setTextList(a);
    }

    @Override
    protected void initData() {
        super.initData();
        // 初始化城市数据
        Map<String, Object> map = CommonUtil.initCitys();
        cityEntities = (List<CityEntity>) map.get("cityEntities");
        List<List<CityEntity>> lists = (List<List<CityEntity>>) map.get("lists");
        parentList.clear();
        parentList.addAll(lists);

        // 设置列表标头，也就是定位及热门城市数据
        final LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.header_city, null);
        // 初始化搜索框
        etSearch = view.findViewById(R.id.et_search);
        tvLocation = view.findViewById(R.id.tv_location);
//        CommonUtil.location(this, tvLocation);
        // 如果有定位城市信息
        if (AppApplication.locationCity != null) {
            // 则显示定位城市名称
            tvLocation.setText(AppApplication.locationCity.getName());
            // 如果已选的城市就是当前定位城市
//            if (AppApplication.selectCity != null
//                    && AppApplication.locationCity.getAdcode().equals(AppApplication.selectCity.getAdcode())) {
//                // 则将定位城市背景设为已选
//                tvLocation.setBackgroundResource(R.drawable.shape_red_stroke_shallow_red_solid);
//            } else {
//                tvLocation.setBackgroundResource(R.drawable.shape_gray_stroke);
//            }
        } else {
            // 否则显示定位中...
            tvLocation.setText(R.string.locationing);
            // 重新进行定位
            LocationClient mLocationClient = CommonUtil.initLocation(this);
            mLocationClient.start();
        }

        elvCity.addHeaderView(view);
        elvCity.setAdapter(cityExpandAdapter = new CityExpandAdapter(this, parentList,
                false, this));
        // 默认展开全部
        for (int i = 0; i < parentList.size(); i++) {
            elvCity.expandGroup(i);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
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
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_location:
                // 如果有定位信息
                if (AppApplication.locationCity != null) {
                    // 则可以选择
                    selectCity(AppApplication.locationCity);
                } else {
                    ToastUtil.showShort(this, getString(R.string.locationing));
                }
                break;
        }
    }

    /**
     * 城市选择回调
     *
     * @param cityEntity
     */
    @Override
    public void selectCity(CityEntity cityEntity) {
        selectCityEntity = cityEntity;
        AppApplication.selectCity = cityEntity;
        // 如果没有选择的城市列表数据，则默认生成，并把当前选中的城市列表数据存入
        if (AppApplication.selectCitys == null){
            AppApplication.selectCitys = new ArrayList<>();
            AppApplication.selectCitys.add(cityEntity);
        }
            Intent intent = new Intent();
        intent.putExtra("cityEntity", cityEntity);
        setResult(RESULT_CODE_SELECT_CITY, intent);
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LocationEvent event) {
        //收到此广播，刷新位置信息
        if (AppApplication.locationCity != null) {
            tvLocation.setText(com.cqkj.publicframework.tool.CommonUtil.
                    changeStringEllipsis(AppApplication.locationCity.getName()));
        }
    }

    private void changeSidebar() {
        // 重置侧边栏字母，只要存在的字母
        a = new String[parentList.size() + 1];
        a[0] = "↑";
        for (int i = 0; i < parentList.size(); i++) {
            a[i + 1] = parentList.get(i).get(0).getLetters().charAt(0) + "";
        }
        sideBar.setTextList(a);
    }


}
