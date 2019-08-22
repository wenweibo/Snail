package com.cqkj.snail.requestdata;

/**
 *  接口请求常量
 * @author 闻维波
 * @since 2019/08/08 16:43
 */

public class RequestUrl {
    // 登录
    public static final short request_do_login = 0;
    // 退出登录
    public static final short request_do_logOut = 1;
    // 注册
    public static final short request_register = 2;
    // 地域查询（无条件）
    public static final short request_area = 3;
    // 字典详情查询(树形)
    public static final short request_dictIfo = 4;
   // 车源列表查询(列表)
    public static final short request_trucks = 5;
  // 多个附件上传
    public static final short post_file = 6;
  // 卖车(插入)
    public static final short post_sell = 7;

    public static final String[] url = {
            // 0:登录
            "/api/login",
            // 1:退出登录
            "mobile/logOut.fxaction",
            // 2:注册
            "/userInfo/save",
            // 3:地域查询(列表)
            "/AreaInfo/page",
            // 4:字典详情查询(树形)
            "/dictInfo/getByCode",
            // 5:车源列表查询(列表)
            "/truckInfo/page",
            // 6:多个附件上传
            "/attach/multiUpload",
            // 7:卖车(插入)
            "truckInfo/save"
    };
}



