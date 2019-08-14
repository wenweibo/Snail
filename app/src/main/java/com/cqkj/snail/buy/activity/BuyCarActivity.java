package com.cqkj.snail.buy.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;

import com.cqkj.publicframework.activity.BaseTitleActivity;
import com.cqkj.publicframework.beans.CallBackObject;
import com.cqkj.publicframework.tool.ToastUtil;
import com.cqkj.publicframework.widget.pull.PullToRefreshLayout;
import com.cqkj.publicframework.widget.pull.PullableListView;
import com.cqkj.snail.R;
import com.cqkj.snail.buy.adapter.ConditionAdapter;
import com.cqkj.snail.buy.entity.DictInfoEntity;
import com.cqkj.snail.config.DictInfo;
import com.cqkj.snail.config.PublishStatus;
import com.cqkj.snail.sell.adapter.DictInfoAdapter;
import com.cqkj.snail.tool.CommonRequest;
import com.cqkj.snail.truck.adapter.TruckListAdapter;
import com.cqkj.snail.truck.entity.TruckEntity;
import com.cqkj.snail.weight.TopPop;
import com.xuexiang.xui.widget.flowlayout.FlowTagLayout;

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
        DictInfoAdapter.DictInfoInterface {
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
    private List<TruckEntity> truckEntities;
    private TruckListAdapter truckListAdapter;
    // 选中标签适配器
    private ConditionAdapter conditionAdapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
//        setBack(false);
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
    }

    @Override
    protected void initData() {
        super.initData();
        conditionAdapter = new ConditionAdapter(this);
        ftlCondition.setAdapter(conditionAdapter);
//        DictInfoEntity dictInfoEntity = new DictInfoEntity();
//        dictInfoEntity.setDictCode("0");
//        dictInfoEntity.setDictName("aaaa");
//        conditionAdapter.addTag(dictInfoEntity);
//
//        dictInfoEntity = new DictInfoEntity();
//        dictInfoEntity.setDictCode("1");
//        dictInfoEntity.setDictName("bbbb");
//        conditionAdapter.addTag(dictInfoEntity);
//
//        dictInfoEntity = new DictInfoEntity();
//        dictInfoEntity.setDictCode("2");
//        dictInfoEntity.setDictName("cccccccccccccccc");
//        conditionAdapter.addTag(dictInfoEntity);
//
//        dictInfoEntity = new DictInfoEntity();
//        dictInfoEntity.setDictCode("3");
//        dictInfoEntity.setDictName("dd");
//        conditionAdapter.addTag(dictInfoEntity);
//
//        dictInfoEntity = new DictInfoEntity();
//        dictInfoEntity.setDictCode("4");
//        dictInfoEntity.setDictName("eeeeeee");
//        conditionAdapter.addTag(dictInfoEntity);
//
//        dictInfoEntity = new DictInfoEntity();
//        dictInfoEntity.setDictCode("5");
//        dictInfoEntity.setDictName("我很好");
//        conditionAdapter.addTag(dictInfoEntity);
//
//        dictInfoEntity = new DictInfoEntity();
//        dictInfoEntity.setDictCode("6");
//        dictInfoEntity.setDictName("我不是很好");
//        conditionAdapter.addTag(dictInfoEntity);

        truckEntities = getTruckEntites(0);
        truckListAdapter = new TruckListAdapter(this, truckEntities);
        plvTruck.setAdapter(truckListAdapter);
    }

    @Override
    public void onClick(View view) {

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
            case R.id.lin_brand:
                // 品牌
                dictInfoEntities = DictInfo.vehicleBrands;
                dictCode = DictInfo.VEHICLE_BRAND;
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

    @Override
    protected void loadData() {
        super.loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onSuccess(int flag, CallBackObject obj) throws ParseException {
        super.onSuccess(flag, obj);
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

    @Override
    public void onFailure(int flag, String message) {
        super.onFailure(flag, message);
        ToastUtil.showLong(this, message);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<TruckEntity> list = getTruckEntites(0);
                handler.obtainMessage(0, list).sendToTarget();
            }
        }).start();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<TruckEntity> list = getTruckEntites(0);
                handler.obtainMessage(1, null).sendToTarget();
            }
        }).start();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<TruckEntity> list = (List<TruckEntity>) msg.obj;
            if (msg.what == 0) {
                refreshView.setCanPullUp(true);
                truckEntities.clear();
                refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
            } else {
                refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
            if (list == null || list.isEmpty()) {
                refreshView.setCanPullUp(false);
            } else {
                truckEntities.addAll(list);
                truckListAdapter.notifyDataSetChanged();
            }
        }
    };

    /**
     * 模拟获取车辆列表
     *
     * @param type
     * @return
     */
    private List<TruckEntity> getTruckEntites(int type) {
        List<TruckEntity> truckEntities = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            TruckEntity truckEntity = new TruckEntity();
            truckEntity.setAttachmentPic("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1564653997279&di=04e1ea12fed27c0f3dcaed8c55164c2b&imgtype=0&src=http%3A%2F%2Fimga.360che.com%2Fimga%2F550x366%2F0%2F112%2F112268.jpg");
            truckEntity.setId(i + "");
            truckEntity.setVehicleBrand("牛逼的卡车" + i + type);
            truckEntity.setVehicleType("哈哈" + i);
            truckEntity.setHorsePower("130 匹");
            truckEntity.setCreateTime("2014年01月");
            truckEntity.setPrice("25万");
            if (i % 2 == 0) {
                truckEntity.setStatus(PublishStatus.ON_SALE);
            } else {
                truckEntity.setStatus(PublishStatus.SOLD);
            }
            truckEntities.add(truckEntity);
            truckEntity = null;
        }
        return truckEntities;
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
    }
}
