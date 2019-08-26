package com.cqkj.snail.tool;

import android.content.Context;
import android.graphics.Rect;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cqkj.publicframework.tool.PinYinUtil;
import com.cqkj.snail.AppApplication;
import com.cqkj.snail.R;
import com.cqkj.snail.buy.entity.DictInfoEntity;
import com.cqkj.snail.config.DictInfo;
import com.cqkj.snail.requestdata.RequestManager;
import com.cqkj.snail.requestdata.RequestUrl;
import com.cqkj.snail.system.entity.CityEntity;
import com.cqkj.snail.truck.entity.ImageViewInfo;
import com.cqkj.publicframework.requestdata.CallBack;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公共工具类
 */
public class CommonUtil {

    /**
     * 将字典集合转换为字符串集合
     *
     * @param dictInfoEntities
     * @return
     */
    public static List<String> ChangeDictsToList(List<DictInfoEntity> dictInfoEntities) {
        List<String> list = new ArrayList<>();
        if (dictInfoEntities != null) {
            for (DictInfoEntity dictInfoEntity : dictInfoEntities) {
                list.add(dictInfoEntity.getDictName());
            }
        }
        return list;
    }

    /**
     * 解析城市数据
     */
    public static Map<String, Object> initCitys() {
        List<CityEntity> cityEntities = new ArrayList<>();
        // 获取缓存好的省份列表，并遍历将省份中的市级信息存入本页面源数据中
        if (AppApplication.cityEntities != null) {
            for (CityEntity cityEntity : AppApplication.cityEntities) {
                List<CityEntity> children = cityEntity.getChildren();
                if (children != null && !children.isEmpty()) {
                    try {
                        cityEntities.addAll(com.cqkj.publicframework.tool.CommonUtil.deepCopy(children));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        // 将已选的城市标记为已选
        if (AppApplication.selectCitys != null) {
            for (CityEntity cityEntity : AppApplication.selectCitys) {
                for (CityEntity cityEntity1 : cityEntities) {
                    if (cityEntity.getAdcode().equals(cityEntity1.getAdcode())){
                        cityEntity1.setSelectFlag(cityEntity.getSelectFlag());
                    }
                }
            }
        }

        List<List<CityEntity>> lists = sortAndGroupCitys(cityEntities);
        Map<String, Object> map = new HashMap<>();
        map.put("cityEntities", cityEntities);
        map.put("lists", lists);
        return map;
    }


    /**
     * 城市定位赋值
     *
     * @param context
     * @param tvLocation
     */
    public static void location(Context context, TextView tvLocation) {
        //如果有选中的城市信息
        if (AppApplication.selectCity != null) {
            // 则将城市赋值为选中城市
            tvLocation.setText(com.cqkj.publicframework.tool.CommonUtil.changeStringEllipsis(AppApplication.selectCity.getName()));
        } else if (AppApplication.locationCity != null) {
            // 如果有定位城市信息，则将城市赋值为定位城市
            tvLocation.setText(com.cqkj.publicframework.tool.CommonUtil.changeStringEllipsis(AppApplication.locationCity.getName()));
            AppApplication.selectCity = AppApplication.locationCity;
        } else {
            // 否则，去重新定位
            // 否则显示定位中...
            tvLocation.setText(R.string.locationing);
            // 重新进行定位
            LocationClient mLocationClient = CommonUtil.initLocation(context);
            mLocationClient.start();
        }
    }

    /**
     * 初始化定位
     */
    public static LocationClient initLocation(Context context) {
        // 声明LocationClient类
        LocationClient mLocationClient = new LocationClient(context.getApplicationContext());
        // 注册监听函数
        MyLocationListener myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        // 设置定位模式，默认高精度
        // LocationMode.Hight_Accuracy：高精度；
        // LocationMode. Battery_Saving：低功耗；
        // LocationMode. Device_Sensors：仅使用设备；
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
        option.setCoorType("bd09ll");
        // 可选，设置是否使用gps，默认false
        option.setOpenGps(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setLocationNotify(true);
        // 可选，是否需要地址信息，默认为不需要，即参数为false
        // 如果开发者需要获得当前点的地址信息，此处必须为true
        option.setIsNeedAddress(true);
        // mLocationClient为第二步初始化过的LocationClient对象
        // 需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        // 更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        mLocationClient.setLocOption(option);
        return mLocationClient;
    }

    /**
     * 注册EventBus
     *
     * @param obj
     */
    public static void evenRegister(Object obj) {
        if (!EventBus.getDefault().isRegistered(obj)) {
            EventBus.getDefault().register(obj);
        }
    }

    /**
     * 注销
     *
     * @param obj
     */
    public static void evenUnregister(Object obj) {
        if (EventBus.getDefault().isRegistered(obj)) {
            EventBus.getDefault().unregister(obj);
        }
    }

    /**
     * 查找信息
     *
     * @return
     */
    public static List<ImageViewInfo> computeBoundsBackward(ArrayList<String> list_path) {
        List<ImageViewInfo> list = new ArrayList<>();
        for (int i = 0; i < list_path.size(); i++) {
            ImageViewInfo imageViewInfo = new ImageViewInfo(list_path.get(i));
            Rect bounds = new Rect();
            imageViewInfo.setBounds(bounds);
            list.add(imageViewInfo);
        }
        return list;
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     */
    public static void login(String username, String password, CallBack callBack) {
        HashMap<String, String> params = new HashMap<>();
        // 登录名
        params.put("loginName", username);
        // 	登录密码
        params.put("loginPassword", password);
        // 	登录方式
        params.put("loginType", "");
        // 获取数据字典
        RequestManager.getRequestManager().post(RequestUrl.request_do_login, params, false,
                callBack);
    }

    /**
     * 通用字典列表排序及设置拼音的方法
     *
     * @param dictInfoEntities
     */
    public static void sortAndGroupDict(List<DictInfoEntity> dictInfoEntities) {
        if (dictInfoEntities == null || dictInfoEntities.isEmpty()) {
            return;
        }
        // 设置拼音
        DictInfo.vehicleBrandsGroup = new ArrayList<>();
        for (DictInfoEntity dictInfoEntity : dictInfoEntities) {
            dictInfoEntity.setLetter(PinYinUtil.getFirstSpell(dictInfoEntity.getDictName()));
        }
        // 排序
        Collections.sort(dictInfoEntities);
        // 分组
        List<DictInfoEntity> list = null;
        for (int i = 0; i < dictInfoEntities.size(); i++) {
            DictInfoEntity dictInfoEntity = dictInfoEntities.get(i);
            if (list == null) {
                list = new ArrayList<>();
            }
            // 如果子列表不为空，且上一次存的list的中的开头字母和当前的开头字母不一样
            if (!list.isEmpty() &&
                    list.get(0).getLetter().charAt(0) != dictInfoEntity.getLetter().charAt(0)) {
                // 把上一次创建的子列表，添加至大列表中
                DictInfo.vehicleBrandsGroup.add(list);
                // 再创建一个新的子列表
                list = new ArrayList<>();
            }
            list.add(dictInfoEntity);
            if (i == dictInfoEntities.size() - 1) {
                DictInfo.vehicleBrandsGroup.add(list);
            }
        }
    }

    /**
     * 对城市列表排序并分组
     *
     * @return
     */
    public static List<List<CityEntity>> sortAndGroupCitys(List<CityEntity> cityEntities) {
        if (cityEntities == null || cityEntities.isEmpty()) {
            return null;
        }
        // 设置拼音
        for (CityEntity cityEntity : cityEntities) {
            cityEntity.setLetters(PinYinUtil.getFirstSpell(cityEntity.getName()));
        }
        List<List<CityEntity>> groupList = new ArrayList<>();
        Collections.sort(cityEntities);
        List<CityEntity> list = null;
        for (int i = 0; i < cityEntities.size(); i++) {
            CityEntity cityEntity = cityEntities.get(i);
            if (list == null) {
                list = new ArrayList<>();
            }
            // 如果子列表不为空，且上一次存的list的中的开头字母和当前的开头字母不一样
            if (!list.isEmpty() &&
                    list.get(0).getLetters().charAt(0) != cityEntity.getLetters().charAt(0)) {
                // 把上一次创建的子列表，添加至大列表中
                groupList.add(list);
                // 再创建一个新的子列表
                list = new ArrayList<>();
            }
            list.add(cityEntity);
            if (i == cityEntities.size() - 1) {
                groupList.add(list);
            }
        }

        return groupList;
    }
}
