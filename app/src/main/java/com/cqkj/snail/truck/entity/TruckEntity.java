package com.cqkj.snail.truck.entity;

import com.cqkj.snail.buy.entity.TruckPicEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 货车实体
 *
 * @author 闻维波 2019/08/01
 */
public class TruckEntity implements Serializable {
    // 车源id
    private String id;
    // 创建人
    private String createUser;
    // 上架时间
    private String createTime;
    // 更新人
    private String updateUser;
    // 更新时间
    private String updateTime;
    // 车辆类型
    private String vehicleType;
    // 车辆类型名称
    private String vehicleTypeContent;
    // 里程数
    private String mileage;
    // 发动机品牌
    private String engineBrand;
    // 发动机品牌名称
    private String engineBrandContent;
    // 燃油类型
    private String fuelType;
    // 燃油类型名称
    private String fuelTypeContent;
    // 排放标准
    private String emissionStandard;
    // 排放标准名称
    private String emissionStandardContent;
    // 车源品牌
    private String vehicleBrand;
    // 车源品牌名称
    private String vehicleBrandContent;
    // 车系
    private String vehicleSystem;
    // 车系名称
    private String vehicleSystemContent;
    // 颜色
    private String colour;
    // 颜色名称
    private String colourContent;
    // 马力
    private String horsePower;
    // 驱动方式
    private String drivingMode;
    // 驱动方式名称
    private String drivingModeContent;
    // 图片
    private List<TruckPicEntity> attachmentPic;
    // 报价
    private String price;
    // 发布状态
    private String status;
    // 发布状态名称
    private String statusContent;
    // 看车地点
    private String carWatchingPlace;
    // 看车地点名称
    private String carWatchingPlaceContent;
    // 排序方式
    private String sortCondition;

    public void setVehicleTypeContent(String vehicleTypeContent) {
        this.vehicleTypeContent = vehicleTypeContent;
    }

    public void setEngineBrandContent(String engineBrandContent) {
        this.engineBrandContent = engineBrandContent;
    }

    public void setFuelTypeContent(String fuelTypeContent) {
        this.fuelTypeContent = fuelTypeContent;
    }

    public void setEmissionStandardContent(String emissionStandardContent) {
        this.emissionStandardContent = emissionStandardContent;
    }

    public void setVehicleBrandContent(String vehicleBrandContent) {
        this.vehicleBrandContent = vehicleBrandContent;
    }

    public void setVehicleSystemContent(String vehicleSystemContent) {
        this.vehicleSystemContent = vehicleSystemContent;
    }

    public void setColourContent(String colourContent) {
        this.colourContent = colourContent;
    }

    public void setDrivingModeContent(String drivingModeContent) {
        this.drivingModeContent = drivingModeContent;
    }

    public void setStatusContent(String statusContent) {
        this.statusContent = statusContent;
    }

    public void setCarWatchingPlace(String carWatchingPlace) {
        this.carWatchingPlace = carWatchingPlace;
    }

    public void setCarWatchingPlaceContent(String carWatchingPlaceContent) {
        this.carWatchingPlaceContent = carWatchingPlaceContent;
    }

    public void setSortCondition(String sortCondition) {
        this.sortCondition = sortCondition;
    }

    public String getVehicleTypeContent() {
        return vehicleTypeContent;
    }

    public String getEngineBrandContent() {
        return engineBrandContent;
    }

    public String getFuelTypeContent() {
        return fuelTypeContent;
    }

    public String getEmissionStandardContent() {
        return emissionStandardContent;
    }

    public String getVehicleBrandContent() {
        return vehicleBrandContent;
    }

    public String getVehicleSystemContent() {
        return vehicleSystemContent;
    }

    public String getColourContent() {
        return colourContent;
    }

    public String getDrivingModeContent() {
        return drivingModeContent;
    }

    public String getStatusContent() {
        return statusContent;
    }

    public String getCarWatchingPlace() {
        return carWatchingPlace;
    }

    public String getCarWatchingPlaceContent() {
        return carWatchingPlaceContent;
    }

    public String getSortCondition() {
        return sortCondition;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public void setEngineBrand(String engineBrand) {
        this.engineBrand = engineBrand;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public void setEmissionStandard(String emissionStandard) {
        this.emissionStandard = emissionStandard;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public void setVehicleSystem(String vehicleSystem) {
        this.vehicleSystem = vehicleSystem;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public void setHorsePower(String horsePower) {
        this.horsePower = horsePower;
    }

    public void setDrivingMode(String drivingMode) {
        this.drivingMode = drivingMode;
    }

    public void setAttachmentPic( List<TruckPicEntity>  attachmentPic) {
        this.attachmentPic = attachmentPic;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getCreateUser() {
        return createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getMileage() {
        return mileage;
    }

    public String getEngineBrand() {
        return engineBrand;
    }

    public String getFuelType() {
        return fuelType;
    }

    public String getEmissionStandard() {
        return emissionStandard;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public String getVehicleSystem() {
        return vehicleSystem;
    }

    public String getColour() {
        return colour;
    }

    public String getHorsePower() {
        return horsePower;
    }

    public String getDrivingMode() {
        return drivingMode;
    }

    public  List<TruckPicEntity>  getAttachmentPic() {
        return attachmentPic;
    }

    public String getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }
}
