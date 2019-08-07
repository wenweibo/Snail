package com.cqkj.snail.truck.entity;

/**
 * 货车实体
 * @author 闻维波 2019/08/01
 */
public class TruckEntity {
    // 车源ID
    private String truckId;
    // 主标题
    private String mainTitle;
    // 副标题
    private String subTitle;
    // 标价
    private String sellingPrice;
    // 发布状态 0：在售，1：已售
    private String publicationStatus;
    // 展示图片路径
    private String pic;
    // 议价标记 0：不可议价，1：可议价
    private String bargainingFlag;
    // 降价 ""/降价额
    private String cutPrice;
    // 新品标记 ""/"最新上架"
    private String newTruck;

    public void setBargainingFlag(String bargainingFlag) {
        this.bargainingFlag = bargainingFlag;
    }

    public void setCutPrice(String cutPrice) {
        this.cutPrice = cutPrice;
    }

    public void setNewTruck(String newTruck) {
        this.newTruck = newTruck;
    }

    public String getBargainingFlag() {
        return bargainingFlag;
    }

    public String getCutPrice() {
        return cutPrice;
    }

    public String getNewTruck() {
        return newTruck;
    }

    public void setTruckId(String truckId) {
        this.truckId = truckId;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public void setPublicationStatus(String publicationStatus) {
        this.publicationStatus = publicationStatus;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTruckId() {
        return truckId;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public String getPublicationStatus() {
        return publicationStatus;
    }

    public String getPic() {
        return pic;
    }
}
