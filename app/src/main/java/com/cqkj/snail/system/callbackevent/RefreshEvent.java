package com.cqkj.snail.system.callbackevent;

/**
 * 刷新广播实体
 * @author 闻维波
 * @since 2019/08/26 09:55
 */
public class RefreshEvent {
    // 0：刷新买车页面
    int flag;

    public RefreshEvent(int flag) {
        this.flag = flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }
}
