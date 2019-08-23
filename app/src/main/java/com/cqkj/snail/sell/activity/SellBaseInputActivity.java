package com.cqkj.snail.sell.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cqkj.publicframework.beans.CallBackObject;
import com.cqkj.publicframework.requestdata.CallBack;
import com.cqkj.publicframework.tool.ToastUtil;
import com.cqkj.publicframework.widget.data_pick.CustomDatePicker;
import com.cqkj.publicframework.widget.data_pick.CustomSinglePicker;
import com.cqkj.snail.AppApplication;
import com.cqkj.snail.R;
import com.cqkj.publicframework.activity.BaseTitleActivity;
import com.cqkj.snail.buy.activity.BrandSelectActivity;
import com.cqkj.snail.buy.entity.DictInfoEntity;
import com.cqkj.snail.config.DictInfo;
import com.cqkj.snail.config.ResultCode;
import com.cqkj.snail.requestdata.RequestManager;
import com.cqkj.snail.requestdata.RequestUrl;
import com.cqkj.snail.system.entity.CityEntity;
import com.cqkj.snail.tool.CommonRequest;
import com.cqkj.snail.tool.CommonUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import win.smartown.android.library.certificateCamera.TruckPic;

/**
 * 买车基本信息输入
 *
 * @author 闻维波
 * @since 2019/08/05 09:35
 */
public class SellBaseInputActivity extends BaseTitleActivity {
    // 最外层滚动控件
    @BindView(R.id.sv_main)
    ScrollView svMain;
    // 照片模块
    @BindView(R.id.lin_pic_block)
    LinearLayout linPicBock;
    // 车辆信息模块
    @BindView(R.id.lin_baseinfo_block)
    LinearLayout linBaseinfoBlock;
    // 车辆信息模块
    @BindView(R.id.lin_truckinfo_block)
    LinearLayout linBruckinfoBlock;
    // 看车地点
    @BindView(R.id.lin_place)
    LinearLayout linPlace;
    // 联系人文本
    @BindView(R.id.tv_name)
    TextView tvName;
    // 联系人电话文本
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    // 看车地点文本
    @BindView(R.id.tv_place)
    TextView tvPlace;
    // 车型文本
    @BindView(R.id.tv_vehicle)
    TextView tvVehicle;
    // 添加图片按钮
    @BindView(R.id.iv_add_pic)
    ImageView ivAddPic;
    // 品牌选择按钮
    @BindView(R.id.lin_brand)
    LinearLayout linBrand;
    // 品牌文本
    @BindView(R.id.tv_brand)
    TextView tvBrand;
    // 价格
    @BindView(R.id.et_price)
    EditText etPrice;
    // 年月选择按钮
    @BindView(R.id.lin_date)
    LinearLayout linDate;
    @BindView(R.id.tv_date)
    TextView tvDate;
    // 排放标准选择按钮
    @BindView(R.id.lin_emission)
    LinearLayout linEmission;
    // 排放标准显示文本
    @BindView(R.id.tv_emission)
    TextView tvEmission;
    // 里程输入框
    @BindView(R.id.et_mileage)
    EditText etMileage;
    // 驱动方式选择按钮
    @BindView(R.id.lin_drive_mode)
    LinearLayout linDriveMode;
    // 驱动方式显示文本
    @BindView(R.id.tv_drive_mode)
    TextView tvDriveMode;
    // 发动机品牌选择按钮
    @BindView(R.id.lin_engine_brand)
    LinearLayout linEngineBrand;
    // 发动机品牌显示文本
    @BindView(R.id.tv_engine_brand)
    TextView tvEngineBrand;
    // 马力
    @BindView(R.id.et_horsepower)
    EditText etHorsepower;
    // 燃料类型选择按钮
    @BindView(R.id.lin_faul_type)
    LinearLayout linFaulType;
    // 燃料类型显示文本
    @BindView(R.id.tv_faul_type)
    TextView tvFaulType;
    // 汽车颜色选择按钮
    @BindView(R.id.lin_color)
    LinearLayout linColor;
    // 汽车颜色显示文本
    @BindView(R.id.tv_color)
    TextView tvColor;
    // 图片数量
    @BindView(R.id.tv_pic_num)
    TextView tvPicNum;
    // 提交按钮
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    // 时间选择器
    private CustomDatePicker customDatePicker;
    // 排放标准选择器
    private CustomSinglePicker ePicker;
    // 驱动方式选择器
    private CustomSinglePicker dmPicker;
    // 发动机破选择器
    private CustomSinglePicker ebPicker;
    // 燃料类型选择器
    private CustomSinglePicker ftPicker;
    // 汽车颜色选择器
    private CustomSinglePicker cPicker;
    // 必传图片id，多个的场合以“，”隔开
    private String attachmentPic;
    // 图片附件_车辆牌照：图片id
    private String attachmentPicLicensePlates;
    // 	图片附件_驾驶室：图片id
    private String attachmentPicCab;

    // 必输列表
    ArrayList<TruckPic> mustTruckPics;
    // 非必输列表
    ArrayList<TruckPic> unmustTruckPics;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sell_base_input;
    }


    @Override
    protected void initView() {
        super.initView();
        title_text.setText(R.string.input_msg);
        back.setVisibility(View.VISIBLE);
        title_do.setVisibility(View.GONE);
        // 照片模块标题赋值
        ((TextView) linPicBock.findViewById(R.id.tv_title)).setText(R.string.truck_pic);
        // 车辆信息（必填）模块标题赋值
        ((TextView) linBaseinfoBlock.findViewById(R.id.tv_title)).setText(R.string.truck_info);
        // 车辆信息（非必填）模块标题赋值
        ((TextView) linBruckinfoBlock.findViewById(R.id.tv_title)).setText(R.string.truck_info);
        TextView pic_remark = (TextView) linBruckinfoBlock.findViewById(R.id.tv_remark);
        pic_remark.setText(getString(R.string.un_must_fill));
        pic_remark.setTextColor(getResources().getColor(R.color.hint_color));

        tvName.setText(AppApplication.userEntity.getUserName());
        tvPhone.setText(AppApplication.userEntity.getPhone());
    }

    @Override
    protected void initData() {
        super.initData();
        // 获取已选车型
        DictInfoEntity vehicleEntity = (DictInfoEntity) getIntent().getBundleExtra("bundle")
                .getSerializable("vehicleEntity");
        tvVehicle.setText(vehicleEntity.getDictName());
        tvVehicle.setTag(vehicleEntity.getDictCode());
    }

    @Override
    protected void initListener() {
        super.initListener();
        linPlace.setOnClickListener(this);
        ivAddPic.setOnClickListener(this);
        linBrand.setOnClickListener(this);
        linDate.setOnClickListener(this);
        linEmission.setOnClickListener(this);
        linDriveMode.setOnClickListener(this);
        linEngineBrand.setOnClickListener(this);
        linFaulType.setOnClickListener(this);
        linFaulType.setOnClickListener(this);
        linColor.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.lin_place:
                // 看车地点选择
                startActivityForResult(new Intent(this, CarWatchPlaceSelectActivity.class), 0);
                break;
            case R.id.iv_add_pic:
                // 添加照片
                Intent intent1 = new Intent(this, UploadActivity.class);
                intent1.putParcelableArrayListExtra("mustTruckPics", mustTruckPics);
                intent1.putParcelableArrayListExtra("unmustTruckPics", unmustTruckPics);
                startActivityForResult(intent1, 0);
                break;
            case R.id.lin_brand:
                // 跳转至品牌选择页面
                Intent intent = new Intent(this, BrandSelectActivity.class);
                intent.putExtra("openSecond", true);
                startActivityForResult(intent, 0);
                break;
            case R.id.lin_date:
                // 年份选择
                if (customDatePicker == null) {
                    initDatePicker();
                }
                customDatePicker.show(tvDate.getText().toString());
                break;
            case R.id.lin_emission:
                // 排放标准
                ePicker = showEmission(DictInfo.emissionStandards, DictInfo.EMISSION_STANDARD, ePicker,
                        tvEmission, getString(R.string.emission_standard_hint));
                break;
            case R.id.lin_drive_mode:
                // 驱动方式
                dmPicker = showEmission(DictInfo.drivingModes, DictInfo.DRIVING_MODE, dmPicker,
                        tvDriveMode, getString(R.string.drive_mode_hint));
                break;
            case R.id.lin_engine_brand:
                // 发动机品牌
                ebPicker = showEmission(DictInfo.engineBrands, DictInfo.ENGINE_BRAND, ebPicker,
                        tvEngineBrand, getString(R.string.engine_brand_hint));
                break;
            case R.id.lin_faul_type:
                // 燃料类型
                ftPicker = showEmission(DictInfo.fuelTypes, DictInfo.FUEL_TYPE, ftPicker,
                        tvFaulType, getString(R.string.fuel_mode_hint));
                break;
            case R.id.lin_color:
                // 车身颜色
                cPicker = showEmission(DictInfo.colours, DictInfo.COLOUR, cPicker,
                        tvColor, getString(R.string.truck_color_hint));
                break;
            case R.id.btn_submit:
                // 提交卖车
                String result = checkParams();
                if (TextUtils.isEmpty(result)) {
                    showDialog("");
                    postSell();
                } else {
                    ToastUtil.showShort(this, result);
                }
                break;
        }
    }


    @Override
    public void onSuccess(int flag, CallBackObject obj) throws ParseException {
        super.onSuccess(flag, obj);
        switch (flag) {
            case RequestUrl.request_dictIfo:
                // 获取字典集合完成
                HashMap params = obj.getParams();
                String dictCode = (String) params.get("dictCode");
                switch (dictCode) {
                    case DictInfo.EMISSION_STANDARD:
                        // 排放标准
                        linEmission.performClick();
                        break;
                    case DictInfo.DRIVING_MODE:
                        // 驱动方式
                        linDriveMode.performClick();
                        break;
                    case DictInfo.ENGINE_BRAND:
                        // 发动机品牌
                        linEngineBrand.performClick();
                        break;
                    case DictInfo.FUEL_TYPE:
                        // 燃料类型
                        linFaulType.performClick();
                        break;
                    case DictInfo.COLOUR:
                        // 车身颜色
                        linColor.performClick();
                        break;
                }
                break;
            case RequestUrl.post_sell:
                // 卖车保存成功
                ToastUtil.showShort(this, R.string.submit_success);
                finish();
                break;
        }
    }

    @Override
    public void onFailure(int flag, String message) {
        super.onFailure(flag, message);
        ToastUtil.showShort(this, message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ResultCode.SELECT_PLACE) {
            if (data != null) {
                CityEntity cityEntity = (CityEntity) data.getExtras()
                        .getSerializable("cityEntity");
                if (cityEntity != null) {
                    tvPlace.setText(cityEntity.getParentName() + " " + cityEntity.getName());
                    tvPlace.setTag(cityEntity.getAdcode());
                }
            }
        } else if (resultCode == ResultCode.SELECT_BRAND) {
            // 选择完品牌
            if (data != null) {
                DictInfoEntity dictInfoEntity = (DictInfoEntity) data.getExtras()
                        .getSerializable("dictInfoEntity");
                tvBrand.setText(dictInfoEntity.getDictName());
                tvBrand.setTag(dictInfoEntity.getDictCode());
            }
        } else if (resultCode == ResultCode.UPLOAD_PIC) {
            // 上传完照片
            if (data != null) {
                // 第一张照片
                String firstPic = data.getExtras().getString("firstPic");
                RequestOptions mRequestOptions = new RequestOptions()
                        .error(R.mipmap.defaultimage)
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(context)
                        .asBitmap()
                        .apply(mRequestOptions)
                        .load(firstPic).into(ivAddPic);
                mustTruckPics = data.getExtras().getParcelableArrayList("mustTruckPics");
                unmustTruckPics = data.getExtras().getParcelableArrayList("unmustTruckPics");
                // 已上传图片数量
                int num = 0;
                // 修改必传图片id
                if (mustTruckPics != null) {
                    attachmentPic = "";
                    for (TruckPic truckPic : mustTruckPics) {
                        if (!TextUtils.isEmpty(truckPic.getImgId())) {
                            attachmentPic += truckPic.getImgId() + ",";
                            num++;
                        }
                    }
                    attachmentPic = attachmentPic.isEmpty() ?
                            attachmentPic : attachmentPic.substring(0, attachmentPic.length() - 1);
                }

                if (unmustTruckPics != null && unmustTruckPics.size() == 2) {
                    attachmentPicLicensePlates = unmustTruckPics.get(0).getImgId();
                    if (!TextUtils.isEmpty(attachmentPicLicensePlates)){
                        num += 1;
                    }
                    attachmentPicCab = unmustTruckPics.get(1).getImgId();
                    if (!TextUtils.isEmpty(attachmentPicCab)){
                        num += 1;
                    }
                }
                // 更新图片数量显示
                tvPicNum.setText(num + "/5");
            }
        }
    }

    /**
     * 初始化日期选择控件
     */
    private void initDatePicker() {
        // 回调接口，获得选中的时间
        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                tvDate.setText(time);
                // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
            }
        }, "1980-01", "2050-12", "yyyy-MM");
        customDatePicker.setTitle("请选择年月");
        customDatePicker.showDay(false); // 是否显示日，如果不允许，则只显示年月
        customDatePicker.setIsLoop(true); // 允许循环滚动
    }

    /**
     * 选择对应字典值
     *
     * @param dictInfoEntities 字典集合
     * @param type             字典类型
     * @param picker           字典选择器
     * @param textView         字典显示文本
     * @param remark           字典提示
     * @return
     */
    private CustomSinglePicker showEmission(final List<DictInfoEntity> dictInfoEntities, String type,
                                            CustomSinglePicker picker, final TextView textView,
                                            String remark) {
        // 如果对应的字典集合中有数据，则直接弹窗
        if (dictInfoEntities != null && !dictInfoEntities.isEmpty()) {
            if (picker == null) {
                picker = new CustomSinglePicker(this,
                        CommonUtil.ChangeDictsToList(dictInfoEntities),
                        new CustomSinglePicker.ResultHandler() {
                            @Override
                            public void handle(int position, String text) {
                                // 将选中的文本赋值给对应的文本控件
                                textView.setText(text);
                                // 将选中的字典码设置为对应文本的标记
                                for (int i = 0; i < dictInfoEntities.size(); i++) {
                                    DictInfoEntity dictInfoEntity = dictInfoEntities.get(i);
                                    if (text.equals(dictInfoEntity.getDictName())) {
                                        textView.setTag(dictInfoEntity.getDictCode());
                                    }
                                }

                            }
                        });
                picker.setTitle(remark);
            }
            picker.show();
        } else {
            // 否则，去获对应的字典集合
            showDialog("");
            CommonRequest.getDictByCode(type, this);
        }
        return picker;
    }

    /**
     * 校验表单数据
     *
     * @return
     */
    private String checkParams() {
        // 如果没有附件id，则说明没有上传图片
        if (TextUtils.isEmpty(attachmentPic)) {
            // 默认点击添加图片按钮，也就是跳转到上传页面
            ivAddPic.performClick();
            return getString(R.string.pic_explain);
        }

        String placeTag = (String) tvPlace.getTag();
        // 如果看车地点没有选
        if (TextUtils.isEmpty(placeTag)) {
            // 默认点击看车地点选择按钮
            linPlace.performClick();
            return getString(R.string.place_hint);
        }

        String brandTag = (String) tvBrand.getTag();
        // 如果没有选择车辆品牌
        if (TextUtils.isEmpty(brandTag)) {
            // 默认点击品牌选择
            linBrand.performClick();
            return getString(R.string.truck_brand_hint);
        }

        // 如果没有输入价格
        if (TextUtils.isEmpty(etPrice.getText().toString())) {
            int[] eScreen = new int[2];
            etPrice.getLocationInWindow(eScreen);
            svMain.scrollTo(eScreen[0], eScreen[1]);
            return getString(R.string.truck_price_hint);
        }

        // 如果没有选择年份
        if (TextUtils.isEmpty(tvDate.getText().toString())) {
            // 默认点击年份选择
            linDate.performClick();
            return getString(R.string.truck_particular_hint);
        }

        String emissionTag = (String) tvEmission.getTag();
        // 如果没有选择排放标准
        if (TextUtils.isEmpty(emissionTag)) {
            linEmission.performClick();
            return getString(R.string.emission_standard_hint);
        }

        // 如果没有输入里程
        if (TextUtils.isEmpty(etMileage.getText().toString())) {
            int[] eScreen = new int[2];
            etMileage.getLocationInWindow(eScreen);
            svMain.scrollTo(eScreen[0], eScreen[1]);
            return getString(R.string.mileage_hint);
        }

        String driveModeTag = (String) tvDriveMode.getTag();
        // 如果没有选择驱动方式
        if (TextUtils.isEmpty(driveModeTag)) {
            // 默认点击驱动方式按钮
            linDriveMode.performClick();
            return getString(R.string.drive_mode_hint);
        }

        String engineBrandTag = (String) tvEngineBrand.getTag();
        // 如果没有选择发动机品牌
        if (TextUtils.isEmpty(engineBrandTag)) {
            // 默认点击发动机品牌按钮
            linEngineBrand.performClick();
            return getString(R.string.engine_brand_hint);
        }
        // 如果没有输入马力
        if (TextUtils.isEmpty(etHorsepower.getText().toString())) {
            int[] eScreen = new int[2];
            etHorsepower.getLocationInWindow(eScreen);
            svMain.scrollTo(eScreen[0], eScreen[1]);
            return getString(R.string.max_horsepower_hint);
        }
        return "";
    }

    /**
     * 提交买车数据
     */
    public void postSell() {
        HashMap<String, String> params = new HashMap<>();
        // 用户Id
        params.put("createUser", AppApplication.userEntity.getId());
        // 车型
        params.put("vehicleType", tvVehicle.getTag().toString());
        // 里程数
        params.put("mileage", etMileage.getText().toString());
        // 发动机品牌
        params.put("engineBrand", tvEngineBrand.getTag().toString());
        // 	燃油类型
        params.put("fuelType", tvFaulType.getTag() == null ? "" : tvFaulType.getTag().toString());
        // 	排放标准
        params.put("emissionStandard", tvEmission.getTag() == null ? "" : tvEmission.getTag().toString());
        // 	型号(车系)
        String vehicleBrandTag = (String) tvBrand.getTag();
        if (vehicleBrandTag.contains(",")) {
            String[] arr = vehicleBrandTag.split(",");
            params.put("vehicleBrand", arr[0]);
            params.put("vehicleSystem", arr[1]);
        }else{
            params.put("vehicleBrand", vehicleBrandTag);
            params.put("vehicleSystem", "");
        }
        // 	颜色
        params.put("colour", tvColor.getTag() == null ? "" : tvColor.getTag().toString());
        // 	马力
        params.put("horsePower", etHorsepower.getText().toString());
        // 	必传图片id，多个的场合以“，”隔开
        params.put("attachmentPic", attachmentPic);
        // 	图片附件_车辆牌照：图片id
        params.put("attachmentPicLicensePlates",
                com.cqkj.publicframework.tool.CommonUtil.changeStringNotNull(attachmentPicLicensePlates));
        // 	图片附件_驾驶室：图片id
        params.put("attachmentPicCab",
                com.cqkj.publicframework.tool.CommonUtil.changeStringNotNull(attachmentPicCab));
        // 	驱动方式
        params.put("drivingMode", tvDriveMode.getTag() == null ? "" : tvDriveMode.getTag().toString());
        // 	车辆报价
        params.put("price", etPrice.getText().toString());
        // 	看车地点
        params.put("carWatchingPlace", tvPlace.getTag() == null ? "" : tvPlace.getTag().toString());
        // 	车源年份
        params.put("vehicleYear", tvDate.getText().toString().replace("-", ""));

        RequestManager.getRequestManager().post(RequestUrl.post_sell, params, false, this);
    }
}
