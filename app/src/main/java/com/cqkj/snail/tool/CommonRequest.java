package com.cqkj.snail.tool;

import com.cqkj.publicframework.requestdata.CallBack;
import com.cqkj.snail.requestdata.RequestManager;
import com.cqkj.snail.requestdata.RequestUrl;

import java.util.HashMap;

/**
 * 公共请求
 * @author 闻维波
 * @since 2019/08/14 08:59
 */
public class CommonRequest {
    /**
     *  每页加载数据条数
     */
    public static final int PAGE_SIZE = 20;
    /**
     * 获取城市数据
     */
    public static void getCitys(CallBack callBack) {
        HashMap<String, String> params = new HashMap<>();
        params.put("areaLevel", "province");
        RequestManager.getRequestManager().post(RequestUrl.request_area, params, false, callBack);
    }

    /**
     * 根据数据字典id，获取对应的数据字典内容
     */
    public static void getDictByCode(String dictCode,CallBack callBack) {
        HashMap<String, String> params = new HashMap<>();
        params.put("dictCode", dictCode);
        RequestManager.getRequestManager().post(RequestUrl.request_dictIfo, params, false, callBack);
    }

}
