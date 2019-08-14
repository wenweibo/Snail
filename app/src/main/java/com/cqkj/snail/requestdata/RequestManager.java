package com.cqkj.snail.requestdata;

import com.cqkj.publicframework.beans.CallBackObject;
import com.cqkj.publicframework.requestdata.CallBack;
import com.cqkj.publicframework.tool.NetUtils;
import com.cqkj.publicframework.tool.PinYinUtil;
import com.cqkj.publicframework.tool.SpUtils;
import com.cqkj.snail.AppApplication;
import com.cqkj.snail.buy.entity.DictInfoEntity;
import com.cqkj.snail.config.DictInfo;
import com.cqkj.snail.system.entity.CityEntity;
import com.cqkj.snail.system.entity.UserEntity;
import com.cqkj.snail.tool.CommonRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * 接口请求管理类
 *
 * @author 闻维波
 * @since 2019/08/08 16:48
 */
public class RequestManager extends com.cqkj.publicframework.requestdata.RequestManager {
    private static RequestManager requestManager;
    /***余善钢*/
//    private String ipurl = "http://172.17.24.83:8080/";
//    public static String fileipurl = "http://172.17.24.99:8080/jdyhgl/a.up";
    /***陶赠元*/
    private String ipurl = "http://172.17.24.103:8080/";
    public static String fileipurl = "http://172.17.24.99:8080/jdyhgl/a.up";

    public static RequestManager getRequestManager() {
        if (requestManager == null) {
            requestManager = new RequestManager();
        }
        return requestManager;
    }

    private HashMap checkParams(HashMap params) {
        HashMap newParams = new HashMap();
        Set<String> set = params.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            newParams.put(key, params.get(key) == null ? "" : params.get(key));
        }
        return newParams;
    }

    public void get(int tag, HashMap params, boolean isCache, CallBack callBack) {
        if (NetUtils.hasNetWork(AppApplication.context)) {
            String url = ipurl + RequestUrl.url[tag];
            get(tag, url, checkParams(params), isCache, callBack, AppApplication.context);
        } else {
            callBack.onFailure(tag, "网络连接不可用");
//            ToastUtil.showShort(MainApplication.context, "网络连接不可用");
        }
    }

    public void post(int tag, HashMap params, boolean isCache, CallBack callBack) {
        if (NetUtils.hasNetWork(AppApplication.context)) {
            String url = ipurl + RequestUrl.url[tag];
            post(tag, url, checkParams(params), isCache, callBack, AppApplication.context);
        } else {
            callBack.onFailure(tag, "网络连接不可用");
//            ToastUtil.showShort(MainApplication.context, "网络连接不可用");
        }
    }

    public void postFile(File _file, CallBack callBack) {
        super.postFile2(fileipurl, _file, callBack, null);
    }

    public void postFile(File _file, CallBack callBack, HashMap params) {
        super.postFile2(fileipurl, _file, callBack, params);
    }

    public void postFile(File _file, CallBack callBack, int type, int index) {
        super.postFile(fileipurl, _file, callBack, type, index);
    }


    @Override
    protected CallBackObject AnalyzeJson(JSONObject obj, int flag, HashMap params) throws Exception {
        CallBackObject callBackObject = new CallBackObject();
        callBackObject.setParams(params);
        Gson gson = new Gson();

        switch (flag) {
            // 登录
            case RequestUrl.request_do_login:
                JSONObject loginObj = obj.getJSONObject("data");
                String id = loginObj.getString("id");
                String loginName = loginObj.getString("loginName");
                String userName = loginObj.getString("userName");
                // 存储用户信息
                AppApplication.userEntity = new UserEntity();
                AppApplication.userEntity.setId(id);
                AppApplication.userEntity.setLoginName(loginName);
                AppApplication.userEntity.setUserName(userName);
                SpUtils.putParam(AppApplication.context, "id", id);
                SpUtils.putParam(AppApplication.context, "loginName", loginName);
                SpUtils.putParam(AppApplication.context, "userName", userName);
                SpUtils.putParam(AppApplication.context, "loginPassword", (String) params.get("loginPassword"));
                break;
            case RequestUrl.request_area:
                // 获取城市数据
                JSONObject cityObj = obj.getJSONObject("data");
                JSONArray contentArr = cityObj.getJSONArray("content");
                AppApplication.cityEntities = new ArrayList<>();
                for (int i = 0; i < contentArr.length(); i++) {
                    JSONObject jsonObject = contentArr.getJSONObject(i);
                    CityEntity cityEntity = gson.fromJson(jsonObject.toString(), CityEntity.class);
                    cityEntity.setLetters(PinYinUtil.getFirstSpell(cityEntity.getName()));
                    AppApplication.cityEntities.add(cityEntity);
                }
                break;
            case RequestUrl.request_dictIfo:
                JSONObject dictObj = obj.getJSONObject("data");
                DictInfoEntity dictInfoEntity = gson.fromJson(dictObj.toString(), DictInfoEntity.class);
                // 获取数据字典后的处理
                String dictCode = (String) params.get("dictCode");
                if (DictInfo.VEHICLE_TYPE.equals(dictCode)) {
                    // 获取车型成功后
                    DictInfo.vehicleTypes = dictInfoEntity.getChildren();
                } else if (DictInfo.VEHICLE_BRAND.equals(dictCode)) {
                    // 获取车源品牌字典成功后
                    DictInfo.vehicleBrands = dictInfoEntity.getChildren();
                } else if (DictInfo.PRICE.equals(dictCode)) {
                    // 获取价格字典成功后
                    DictInfo.prices = dictInfoEntity.getChildren();
                } else if (DictInfo.EMISSION_STANDARD.equals(dictCode)) {
                    // 获取排放标准字典成功后
                    DictInfo.emissionStandards = dictInfoEntity.getChildren();
                } else if (DictInfo.MILEAGE.equals(dictCode)) {
                    // 获取行驶里程字典成功后
                    DictInfo.mileages = dictInfoEntity.getChildren();
                } else if (DictInfo.PUBLICATION_STATUS.equals(dictCode)) {
                    // 获取发布状态字典成功后
                    DictInfo.publicationStatus = dictInfoEntity.getChildren();
                } else if (DictInfo.SORT_KIND.equals(dictCode)) {
                    // 获取发布状态字典成功后
                    DictInfo.sortKinds = dictInfoEntity.getChildren();
                }
                break;
        }
        return callBackObject;
    }


}
