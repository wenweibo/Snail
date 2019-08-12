package com.cqkj.snail.system.activity;

import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.cqkj.snail.R;
import com.cqkj.snail.buytruck.adapter.TruckTypeAdapter;
import com.cqkj.snail.sell.activity.SellBaseInputActivity;
import com.cqkj.snail.truck.entity.MenuEntity;
import com.cqkj.publicframework.activity.BaseTitleActivity;
import com.cqkj.publicframework.tool.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 卖车页
 * @author 闻维波 2019/07/26
 */
public class SellCarActivity extends BaseTitleActivity {
    // 车型列表
    @BindView(R.id.gv_truck_type)
    GridView gv_truck_type;
    // 下一步按钮
    @BindView(R.id.btn_next_step)
    Button btn_next_step;
    // 类型列表
    private List<MenuEntity> datas;
    // 卡车类型图片
    private final int[] truckImgArr = new int[]{R.mipmap.truck_tractor, R.mipmap.truck_cargo,
            R.mipmap.truck_trailer, R.mipmap.truck_dump, R.mipmap.truck_mixer_concrete,
            R.mipmap.truck_bulk, R.mipmap.truck_tanker, R.mipmap.truck_refrigerated,
            R.mipmap.truck_sprinkler, R.mipmap.truck_concrete_pump, R.mipmap.truck_crane,
            R.mipmap.truck_other
    };
    // 卡车类型图片:选中
    private final int[] truckImgArrSelected = new int[]{R.mipmap.truck_tractor_s, R.mipmap.truck_cargo_s,
            R.mipmap.truck_trailer_s, R.mipmap.truck_dump_s, R.mipmap.truck_mixer_concrete_s,
            R.mipmap.truck_bulk_s, R.mipmap.truck_tanker_s, R.mipmap.truck_refrigerated_s,
            R.mipmap.truck_sprinkler_s, R.mipmap.truck_concrete_pump_s, R.mipmap.truck_crane_s,
            R.mipmap.truck_other_s
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_sell_car;
    }

    @Override
    protected void initView() {
        super.initView();
        back.setVisibility(View.GONE);
        title_do.setVisibility(View.GONE);
        title_text.setText(R.string.select_truck_type);
    }

    @Override
    protected void initData() {
        super.initData();
        gv_truck_type.setAdapter(new TruckTypeAdapter(this, datas = getTypeData()));
    }

    @Override
    protected void initListener() {
        super.initListener();
        btn_next_step.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_next_step:
                // 当类型列表不为空
                if (datas!=null && !datas.isEmpty()){
                    // 选择的车型
                    MenuEntity selectedME = null;
                    // 遍历数据集
                    for (MenuEntity menuEntity : datas) {
                        //如果碰到了选中的，则将选中的车型赋值为该车型,并且结束遍历
                        if (menuEntity.isSelected()){
                            selectedME = menuEntity;
                            break;
                        }
                    }
                    // 如果选择了车型，则进行下一步
                    if (selectedME != null){
                       startActivity(SellBaseInputActivity.class);
                    }else{
                        // 如果没有选择车型，则弹出提示
                        ToastUtil.showShort(this, R.string.no_selected_truck_type);
                    }
                }else{
                    // 当类型列表为空
                    ToastUtil.showShort(this, R.string.no_truch_type);
                }
                break;
        }
    }
    private long exitTime = 0;

    @Override
    public void onBackPressed() {

    }

    /**
     * 获取车型列表数据
     * @return
     */
    private List<MenuEntity> getTypeData(){
        List<MenuEntity> menuEntities = new ArrayList<>();
        for (int i = 0; i < truckImgArr.length; i++) {
            MenuEntity menuEntity = new MenuEntity();
            menuEntity.setImgRes(truckImgArr[i]);
            menuEntity.setImgResSelected(truckImgArrSelected[i]);
            menuEntity.setTitle("");
            menuEntity.setId(i+"");
            menuEntities.add(menuEntity);
            menuEntity = null;
        }
        return menuEntities;
    }

}
