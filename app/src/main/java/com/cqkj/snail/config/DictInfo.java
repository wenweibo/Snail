package com.cqkj.snail.config;

import com.cqkj.snail.buy.entity.DictInfoEntity;

import java.util.List;

/**
 * 字典
 *
 * @author 闻维波
 * @since 2019/08/14 09:13
 */
public class DictInfo {
    /**
     * 车型
     */
    public static final String VEHICLE_TYPE = "VEHICLE_TYPE";
    public static List<DictInfoEntity> vehicleTypes;
    /**
     * 车源品牌
     */
    public static final String VEHICLE_BRAND = "VEHICLE_BRAND";
    public static List<DictInfoEntity> vehicleBrands;
    // 分组后的品牌列表
    public static List<List<DictInfoEntity>> vehicleBrandsGroup;
    /**
     * 价格
     */
    public static final String PRICE = "PRICE";
    public static List<DictInfoEntity> prices;
    /**
     * 排放标准
     */
    public static final String EMISSION_STANDARD = "EMISSION_STANDARD";
    public static List<DictInfoEntity> emissionStandards;
    /**
     * 行驶里程
     */
    public static final String MILEAGE = "MILEAGE";
    public static List<DictInfoEntity> mileages;
    /**
     * 发布状态：1、“ON_SALE” ： 在售； 2、“SOLD” ： 已售； 3、“UNPUBLISHED” ： 已下架；
     */
    public static final String PUBLICATION_STATUS = "PUBLICATION_STATUS";
    public static List<DictInfoEntity> publicationStatus;
    /**
     * 排序条件：1、“SHELF_TIME” ： 最新上架； 2、“LOW_MILEAGE_FIRST” ： 里程低优先；
     * 3、“HIGHT_PRICE_FIRST” ： 价格高优先； 4、“LOW_PRICE_FIRST” ： 价格低优先；
     */
    public static final String SORT_KIND = "SORT_KIND";
    public static List<DictInfoEntity> sortKinds;
    /**
     * 驱动方式
     */
    public static final String DRIVING_MODE = "DRIVING_MODE";
    public static List<DictInfoEntity> drivingModes;
    /**
     * 发动机品牌
     */
    public static final String ENGINE_BRAND = "ENGINE_BRAND";
    public static List<DictInfoEntity> engineBrands;
    /**
     * 燃油类型
     */
    public static final String FUEL_TYPE = "FUEL_TYPE";
    public static List<DictInfoEntity> fuelTypes;
    /**
     * 车辆颜色
     */
    public static final String COLOUR = "COLOUR";
    public static List<DictInfoEntity> colours;
}
