package com.cqkj.snail.system.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.cqkj.publicframework.activity.BaseTitleActivity;
import com.cqkj.publicframework.tool.PinYinUtil;
import com.cqkj.publicframework.widget.contactar.SideBar;
import com.cqkj.snail.R;
import com.cqkj.snail.system.adapter.CityExpandAdapter;
import com.cqkj.snail.system.entity.CityEntity;
import com.cqkj.snail.tool.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 城市选择页面
 *
 * @author 闻维波
 * @since 2019/08/12 08:55
 */
public class CitySelectActivity extends BaseTitleActivity {
    // 城市列表
    @BindView(R.id.elv_city)
    ExpandableListView elvCity;
    // 字母提示
    @BindView(R.id.tv_dialog)
    TextView tvDialog;
    // 侧边字母提示
    @BindView(R.id.sideBar)
    SideBar sideBar;
    // 搜索框
    EditText etSearch;
    private List<CityEntity> cityEntities;
    private List<List<CityEntity>> parentList;
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
    protected int getLayoutId() {
        return R.layout.activity_city_select;
    }

    @Override
    protected void initView() {
        super.initView();
        title_text.setText(R.string.select_city);
        back.setVisibility(View.VISIBLE);
        title_do.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        super.initData();
        initCitys();
        // 设置列表标头，也就是定位及热门城市数据
        final LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.header_city, null);
        etSearch = view.findViewById(R.id.et_search);
        elvCity.addHeaderView(view);
        elvCity.setAdapter(cityExpandAdapter = new CityExpandAdapter(this, parentList,
                cityEntities, new CityExpandAdapter.CitySelect() {
            @Override
            public void selectCity(CityEntity cityEntity) {
                selectCityEntity = cityEntity;
                Intent intent = new Intent();
                intent.putExtra("cityEntity", cityEntity);
                setResult(RESULT_CODE_SELECT_CITY, intent);
                finish();
            }
        }));
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
                // 调用城市适配器筛选器
                cityExpandAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 默认展开全部
                for (int i = 0; i < parentList.size(); i++) {
                    elvCity.expandGroup(i);
                }
            }
        });
    }

    /**
     * 模拟城市数据
     */
    private void initCitys() {
        cityEntities = new ArrayList<>();
        CityEntity cityEntity = new CityEntity();
        cityEntity.setName("南昌");
        cityEntity.setLetters(PinYinUtil.getFirstSpell(cityEntity.getName()));
        cityEntities.add(cityEntity);

        cityEntity = new CityEntity();
        cityEntity.setName("南宁");
        cityEntity.setLetters(PinYinUtil.getFirstSpell(cityEntity.getName()));
        cityEntities.add(cityEntity);

        cityEntity = new CityEntity();
        cityEntity.setName("苏宁");
        cityEntity.setLetters(PinYinUtil.getFirstSpell(cityEntity.getName()));
        cityEntities.add(cityEntity);

        cityEntity = new CityEntity();
        cityEntity.setName("南京");
        cityEntity.setLetters(PinYinUtil.getFirstSpell(cityEntity.getName()));
        cityEntities.add(cityEntity);

        cityEntity = new CityEntity();
        cityEntity.setName("那边");
        cityEntity.setLetters(PinYinUtil.getFirstSpell(cityEntity.getName()));
        cityEntities.add(cityEntity);

        cityEntity = new CityEntity();
        cityEntity.setName("哪边");
        cityEntity.setLetters(PinYinUtil.getFirstSpell(cityEntity.getName()));
        cityEntities.add(cityEntity);


        cityEntity = new CityEntity();
        cityEntity.setName("九江");
        cityEntity.setLetters(PinYinUtil.getFirstSpell(cityEntity.getName()));
        cityEntities.add(cityEntity);

        cityEntity = new CityEntity();
        cityEntity.setName("赣州");
        cityEntity.setLetters(PinYinUtil.getFirstSpell(cityEntity.getName()));
        cityEntities.add(cityEntity);

        cityEntity = new CityEntity();
        cityEntity.setName("长沙");
        cityEntity.setLetters(PinYinUtil.getFirstSpell(cityEntity.getName()));
        cityEntities.add(cityEntity);

        cityEntity = new CityEntity();
        cityEntity.setName("长春");
        cityEntity.setLetters(PinYinUtil.getFirstSpell(cityEntity.getName()));
        cityEntities.add(cityEntity);

        cityEntity = new CityEntity();
        cityEntity.setName("乌鲁木齐");
        cityEntity.setLetters(PinYinUtil.getFirstSpell(cityEntity.getName()));
        cityEntities.add(cityEntity);

        cityEntity = new CityEntity();
        cityEntity.setName("阿勒泰");
        cityEntity.setLetters(PinYinUtil.getFirstSpell(cityEntity.getName()));
        cityEntities.add(cityEntity);

        cityEntity = new CityEntity();
        cityEntity.setName("北京");
        cityEntity.setLetters(PinYinUtil.getFirstSpell(cityEntity.getName()));
        cityEntities.add(cityEntity);

        cityEntity = new CityEntity();
        cityEntity.setName("张家界");
        cityEntity.setLetters(PinYinUtil.getFirstSpell(cityEntity.getName()));
        cityEntities.add(cityEntity);

        cityEntity = new CityEntity();
        cityEntity.setName("宜春");
        cityEntity.setLetters(PinYinUtil.getFirstSpell(cityEntity.getName()));
        cityEntities.add(cityEntity);
        parentList = CommonUtil.sortAndGroupCitys(cityEntities);
        changeSidebar();
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
