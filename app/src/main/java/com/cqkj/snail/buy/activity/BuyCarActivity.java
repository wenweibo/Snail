package com.cqkj.snail.buy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqkj.publicframework.activity.BaseTitleActivity;
import com.cqkj.publicframework.beans.CallBackObject;
import com.cqkj.publicframework.tool.ToastUtil;
import com.cqkj.publicframework.widget.pull.PullToRefreshLayout;
import com.cqkj.publicframework.widget.pull.PullableListView;
import com.cqkj.snail.AppApplication;
import com.cqkj.snail.R;
import com.cqkj.snail.buy.adapter.ConditionAdapter;
import com.cqkj.snail.buy.entity.DictInfoEntity;
import com.cqkj.snail.config.DictInfo;
import com.cqkj.snail.config.ResultCode;
import com.cqkj.snail.requestdata.RequestManager;
import com.cqkj.snail.requestdata.RequestUrl;
import com.cqkj.snail.sell.adapter.DictInfoAdapter;
import com.cqkj.snail.system.activity.CitySelectActivity;
import com.cqkj.snail.system.activity.ProvinceCitySelectActivity;
import com.cqkj.snail.system.callbackevent.LocationEvent;
import com.cqkj.snail.system.callbackevent.RefreshEvent;
import com.cqkj.snail.system.entity.CityEntity;
import com.cqkj.snail.tool.CommonRequest;
import com.cqkj.snail.tool.CommonUtil;
import com.cqkj.snail.truck.adapter.TruckListAdapter;
import com.cqkj.snail.truck.entity.TruckEntity;
import com.cqkj.snail.weight.TopPop;
import com.xuexiang.xui.widget.flowlayout.FlowTagLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 买车页
 *
 * @author 闻维波
 * @since 2019/07/26 08:55
 */
public class BuyCarActivity extends BaseTitleActivity
        implements PullToRefreshLayout.OnRefreshListener,
        DictInfoAdapter.DictInfoInterface, ConditionAdapter.ConditionInterface {
    // 无数据视图
    @BindView(R.id.lin_no_data)
    LinearLayout linNoData;
    // 无数据刷新按钮
    @BindView(R.id.lin_no_data_refresh)
    LinearLayout linNoDataRefresh;
    // 定位文本
    @BindView(R.id.tv_location)
    TextView tvLocation;
    // 选中标签控件
    @BindView(R.id.ftl_condition)
    FlowTagLayout ftlCondition;
    // 刷新控件
    @BindView(R.id.refresh_view)
    PullToRefreshLayout refreshView;
    // 货车列表
    @BindView(R.id.plv_truck)
    PullableListView plvTruck;
    // 顶部筛选菜单父控件
    @BindView(R.id.lin_top_menu)
    LinearLayout linTopMenu;
    // 排序按钮
    @BindView(R.id.lin_sort)
    LinearLayout linSort;
    // 车型按钮
    @BindView(R.id.lin_truck_type)
    LinearLayout linTruckType;
    // 品牌按钮
    @BindView(R.id.lin_brand)
    LinearLayout linBrand;
    // 价格按钮
    @BindView(R.id.lin_price)
    LinearLayout linPrice;

    // 车辆集合
    private List<TruckEntity> truckEntities = new ArrayList<>();
    private TruckListAdapter truckListAdapter;
    // 选中标签适配器
    private ConditionAdapter conditionAdapter;

    // 当前页码
    private int pageNo = 1;
    // 	车型
    private String vehicleType = "";
    //  车源品牌
    private String vehicleBrand = "";
    //  车系
    private String vehicleSystem = "";
    // 价格
    private String priceCondition = "";
    // 排序条件
    private String sortCondition = "";

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
        return R.layout.activity_buy_car;
    }

    @Override
    protected void initView() {
        super.initView();
        title_main_layout.setVisibility(View.GONE);

    }

    @Override
    protected void initListener() {
        super.initListener();
        refreshView.setOnRefreshListener(this);
        linSort.setOnClickListener(this);
        linTruckType.setOnClickListener(this);
        linBrand.setOnClickListener(this);
        linPrice.setOnClickListener(this);
        linNoDataRefresh.setOnClickListener(this);
        tvLocation.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        // 初始化流标签适配器
        conditionAdapter = new ConditionAdapter(this, this);
        ftlCondition.setAdapter(conditionAdapter);
        // 获取货车列表
//        truckEntities = getTruckEntites(0);
        // 初始化货车列表适配器
        truckListAdapter = new TruckListAdapter(this, truckEntities);
        plvTruck.setAdapter(truckListAdapter);
        // 给城市文本赋值
        CommonUtil.location(this, tvLocation);
    }

    @Override
    protected void loadData() {
        myLoadData();
    }

    protected void myLoadData() {
        showDialog("");
        myRefresh();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.tv_location) {
            // 城市
            // 如果有城市数据，则跳转至城市选择页面
            if (AppApplication.cityEntities != null && !AppApplication.cityEntities.isEmpty()) {
                Intent intent = new Intent(this, ProvinceCitySelectActivity.class);
                startActivityForResult(intent, 0);
            } else {
                // 否则，去重新拉取城市数据
                showDialog("");
                CommonRequest.getCitys(this);
            }
        } else if (view.getId() == R.id.lin_brand) {
            // 点击品牌选择
            // 如果没有品牌信息,则去重新拉取
            if (DictInfo.vehicleBrands == null || DictInfo.vehicleBrands.isEmpty()) {
                showDialog("");
                CommonRequest.getDictByCode(DictInfo.VEHICLE_BRAND, this);
            } else {
                // 跳转至品牌选择页面
                Intent intent = new Intent(this, BrandSelectActivity.class);
                intent.putExtra("openSecond", true);
                startActivityForResult(intent, 0);
            }
        } else if (view.getId() == R.id.lin_no_data_refresh) {
            // 刷新按钮
            showDialog("");
            myRefresh();
        } else {
            List<DictInfoEntity> dictInfoEntities = null;
            String dictCode = null;
            switch (view.getId()) {
                case R.id.lin_sort:
                    // 排序
                    dictInfoEntities = DictInfo.sortKinds;
                    dictCode = DictInfo.SORT_KIND;
                    break;
                case R.id.lin_truck_type:
                    // 车型
                    dictInfoEntities = DictInfo.vehicleTypes;
                    dictCode = DictInfo.VEHICLE_TYPE;
                    break;
                case R.id.lin_price:
                    // 价格
                    dictInfoEntities = DictInfo.prices;
                    dictCode = DictInfo.PRICE;
                    break;
            }
            // 如果有字典列表,则弹窗
            if (dictInfoEntities != null && !dictInfoEntities.isEmpty()) {
                TopPop topPop = new TopPop(getParent(), dictInfoEntities, dictCode, this);
                topPop.showAsDropDown(linTopMenu, 0, 0);
            } else {
                // 没有对应字典，则去重新拉取
                showDialog("");
                CommonRequest.getDictByCode(dictCode, this);
            }
        }
    }


    @Override
    public void onSuccess(int flag, CallBackObject obj) throws ParseException {
        super.onSuccess(flag, obj);
        // 获取货车列表成功
        if (flag == RequestUrl.request_trucks) {
            if (pageNo == 1) {
                truckEntities.clear();
            }
            // 设置上拉和下拉都为成功
            refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
            refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            List<TruckEntity> truckList = (List<TruckEntity>) obj.getObject();
            int total = obj.getRowCount();
            // 当获取到了货车列表
            if (truckList != null && !truckList.isEmpty()) {
                truckEntities.addAll(truckList);
            }
            // 设置视图是否显示
            if (truckEntities.isEmpty()) {
                setMain(View.GONE);
            } else {
                setMain(View.VISIBLE);
                // 如果目前加载的总数小于总数
                if (truckEntities.size() < total) {
                    // 则设置可以上拉加载
                    refreshView.setCanPullUp(true);
                } else {
                    // 否则，设置为不可上拉加载
                    refreshView.setCanPullUp(false);
                }
            }
            truckListAdapter.notifyDataSetChanged();
        } else if (flag == RequestUrl.request_area) {
            // 重新拉取城市数据后的处理
            tvLocation.performLongClick();
        } else {
            // 获取字典列表成功
            HashMap<String, String> params = obj.getParams();
            if (DictInfo.SORT_KIND.equals(params.get("dictCode"))) {
                linSort.performClick();
            } else if (DictInfo.VEHICLE_TYPE.equals(params.get("dictCode"))) {
                linTruckType.performClick();
            } else if (DictInfo.VEHICLE_BRAND.equals(params.get("dictCode"))) {
                linBrand.performClick();
            } else if (DictInfo.PRICE.equals(params.get("dictCode"))) {
                linPrice.performClick();
            }
        }
    }

    /**
     * 设置主视图和无数据视图的显示
     *
     * @param mainVis 主视图显示参数
     */
    private void setMain(int mainVis) {
        // 设置主视图显示参数
        refreshView.setVisibility(mainVis);
        // 如果主视图为显示
        if (mainVis == View.VISIBLE) {
            // 则将无数据视图设为隐藏
            linNoData.setVisibility(View.GONE);
        } else {
            // 否则，将无数据视图设为显示
            linNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailure(int flag, String message) {
        super.onFailure(flag, message);
        if (flag == RequestUrl.request_trucks) {
            if (refreshView != null) {
                refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
                refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
            if (truckEntities.isEmpty()) {
                setMain(View.GONE);
            } else {
                setMain(View.VISIBLE);
            }
            truckListAdapter.notifyDataSetChanged();
        }
        ToastUtil.showLong(this, message);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        myRefresh();
    }

    /**
     * 刷新方法
     */
    private void myRefresh() {
        // 清空数据
//        truckEntities.clear();
        // 初始化页码
        pageNo = 1;
        // 加载货车列表
        getTrucks();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        // 页码增1
        pageNo++;
        // 加载货车列表
        getTrucks();
    }


    @Override
    public void onSelect(DictInfoEntity dictInfoEntity) {
        List<DictInfoEntity> items = conditionAdapter.getItems();
        DictInfoEntity selectDict = null;
        for (DictInfoEntity dictInfoEntity1 : items) {
            if (dictInfoEntity.getParentId().equals(dictInfoEntity1.getParentId())) {
                selectDict = dictInfoEntity1;
            }
        }
        if (selectDict != null) {
            conditionAdapter.removeElement(selectDict);
        }
        conditionAdapter.addTag(dictInfoEntity);
        changeParams();
        myLoadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 选择完品牌
        if (resultCode == ResultCode.SELECT_BRAND && data != null) {
            DictInfoEntity dictInfoEntity = (DictInfoEntity) data.getExtras().getSerializable("dictInfoEntity");
            onSelect(dictInfoEntity);
        } else if (resultCode == CitySelectActivity.RESULT_CODE_SELECT_CITY) {
            if (AppApplication.selectCitys != null && AppApplication.selectCitys.size() > 0) {
                // 取出选中城市列表的第一个
                CityEntity cityEntity = AppApplication.selectCitys.get(0);
                String more = "";
                if (AppApplication.selectCitys.size() > 1) {
                    more = "等";
                }
                tvLocation.setText(com.cqkj.publicframework.tool.CommonUtil.changeStringEllipsis(cityEntity.getName() + more));
            } else {
                tvLocation.setText("全国");
            }
        }
    }

    /**
     * 清空请求参数
     */
    private void clearParams() {
        // 	车型
        vehicleType = "";
        //  车源品牌
        vehicleBrand = "";
        //  车系
        vehicleSystem = "";
        // 地域(看车地点)
//        carWatchingPlace = "";
        // 价格
        priceCondition = "";
        // 排序条件
        sortCondition = "";

    }

    /**
     * 修改请求参数
     */
    private void changeParams() {
        List<DictInfoEntity> items = conditionAdapter.getItems();
        for (DictInfoEntity dictInfoEntity : items) {
            switch (dictInfoEntity.getParentId()) {
                case DictInfo.SORT_KIND:
                    // 排序条件
                    sortCondition = dictInfoEntity.getDictCode();
                    break;
                case DictInfo.VEHICLE_TYPE:
                    // 车型
                    vehicleType = dictInfoEntity.getDictCode();
                    break;
                case DictInfo.VEHICLE_BRAND:
                    // 品牌
                    // 品牌和车系字典码
                    String brandSystemdictCode = dictInfoEntity.getDictCode();
                    if (!TextUtils.isEmpty(brandSystemdictCode)) {
                        if (brandSystemdictCode.contains(",")) {
                            String[] bsCodeArr = brandSystemdictCode.split(",");
                            vehicleBrand = bsCodeArr[0];
                            vehicleSystem = bsCodeArr[1];
                        }else{
                            vehicleBrand = brandSystemdictCode;
                            vehicleSystem = "";
                        }
                    }
                    break;
                case DictInfo.PRICE:
                    // 价格
                    priceCondition = dictInfoEntity.getDictCode();
                    break;
            }
        }
    }

    /**
     * 获取车源列表
     */
    public void getTrucks() {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageSize", CommonRequest.PAGE_SIZE + "");
        params.put("pageNo", pageNo + "");
        params.put("vehicleType", vehicleType);
        params.put("vehicleBrand", vehicleBrand);
        params.put("vehicleSystem", vehicleSystem);
        String carWatchingPlace = "";
        if (AppApplication.selectCitys != null && AppApplication.selectCitys.size() > 0) {
            for (CityEntity cityEntity : AppApplication.selectCitys) {
                carWatchingPlace += cityEntity.getAdcode() + ",";
            }
            carWatchingPlace = carWatchingPlace.substring(0, carWatchingPlace.length() - 1);
        }
        params.put("carWatchingPlace", carWatchingPlace);
        params.put("priceCondition", priceCondition);
        params.put("sortCondition", sortCondition);
        RequestManager.getRequestManager().post(RequestUrl.request_trucks, params, false, this);
    }

    /**
     * 标签删除回调
     *
     * @param dictInfoEntity
     */
    @Override
    public void deleteTag(DictInfoEntity dictInfoEntity) {
        clearParams();
        changeParams();
        myLoadData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshEvent event) {
        //收到此广播，刷新位置信息
        if (event.getFlag() == 0) {
            myLoadData();
        }
    }
}
