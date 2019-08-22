package com.cqkj.snail.system.callbackevent;

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
