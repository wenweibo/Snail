package com.cqkj.snail.system.callbackevent;

import com.baidu.location.BDLocation;

/**
 * 定位事件实体
 */
public class LocationEvent {
    private BDLocation location;

    public LocationEvent(BDLocation location) {
        this.location = location;
    }

    public void setLocation(BDLocation location) {
        this.location = location;
    }

    public BDLocation getLocation() {
        return location;
    }
}
