package com.cqkj.snail.system.callbackevent;

import com.cqkj.snail.buy.entity.DictInfoEntity;

/**
 * 字典刷新实体
 *
 * @author 闻维波
 * @since 2019/08/26 09:55
 */
public class DictInfoEvent {
    private DictInfoEntity dictInfoEntity;

    public DictInfoEvent(DictInfoEntity dictInfoEntity) {
        this.dictInfoEntity = dictInfoEntity;
    }

    public DictInfoEntity getDictInfoEntity() {
        return dictInfoEntity;
    }
}
