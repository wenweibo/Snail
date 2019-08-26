package com.cqkj.snail.tool;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.cqkj.snail.AppApplication;
import com.cqkj.snail.system.callbackevent.LocationEvent;
import com.cqkj.snail.system.entity.CityEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MyLocationListener extends BDAbstractLocationListener {
    @Override
    public void onReceiveLocation(BDLocation location) {
        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
        //以下只列举部分获取地址相关的结果信息
        //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
//        double latitude = location.getLatitude();
//        String addr = location.getAddrStr();    //获取详细地址信息
//        String country = location.getCountry();    //获取国家
//        String province = location.getProvince();    //获取省份
//        String adCode = location.getAdCode();
//        String cityCode = location.getCityCode();
//        String city = location.getCity();    //获取城市
//        String district = location.getDistrict();    //获取区县
//        String street = location.getStreet();    //获取街道信息

        // 将定位的城市信息保存到AppApplication中
        // 如果有省份列表信息
        if (AppApplication.cityEntities != null) {
            // 变量省份列表
            for (CityEntity cityEntity : AppApplication.cityEntities) {
                // 获取省份里的城市信息
                List<CityEntity> children = cityEntity.getChildren();
                if (children != null) {
                    for (CityEntity cityEntity1 : children) {
                        // 如果城市编码和定位到的城市编码一致
                        if (cityEntity1.getAdcode().equals(location.getCityCode())) {
                            // 则将城市选中设为1，以备后面列表中的默认选中效果
//                            cityEntity1.setSelectFlag(1);
                            // 定位到的城市为当前城市
                            AppApplication.locationCity = cityEntity1;
                            AppApplication.locationCity.setParentName(location.getProvince());
                        }
                    }
                }
            }
        } else {
            AppApplication.locationCity = new CityEntity();
            AppApplication.locationCity.setAdcode(location.getCityCode());
            AppApplication.locationCity.setName(location.getCity());
            AppApplication.locationCity.setParentId(location.getAdCode());
            AppApplication.locationCity.setParentName(location.getProvince());
        }
        // 默认设定已选城市
        if (AppApplication.selectCity == null) {
            AppApplication.selectCity = AppApplication.locationCity;
        }
        // 默认设定已选城市列表
        if (AppApplication.selectCitys == null){
            AppApplication.selectCitys = new ArrayList<>();
            AppApplication.selectCitys.add(AppApplication.selectCity);
        }

        EventBus.getDefault().post(new LocationEvent(location));
    }
}