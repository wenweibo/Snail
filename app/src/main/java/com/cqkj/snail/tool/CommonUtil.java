package com.cqkj.snail.tool;

import android.graphics.Rect;

import com.cqkj.publicframework.tool.PinYinUtil;
import com.cqkj.snail.requestdata.RequestManager;
import com.cqkj.snail.requestdata.RequestUrl;
import com.cqkj.snail.system.entity.CityEntity;
import com.cqkj.snail.truck.entity.ImageViewInfo;
import com.cqkj.publicframework.requestdata.CallBack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 公共工具类
 */
public class CommonUtil {
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
        RequestManager.getRequestManager().post(RequestUrl.request_do_login, params, false, callBack);
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
            if (!list.isEmpty() && list.get(0).getLetters().charAt(0) != cityEntity.getLetters().charAt(0)) {
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
