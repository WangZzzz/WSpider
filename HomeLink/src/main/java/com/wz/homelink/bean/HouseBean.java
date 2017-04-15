package com.wz.homelink.bean;

/**
 * Created by wz on 2017/4/12.
 */
public class HouseBean {
    //详细url
    private String detailUrl;
    //总价
    private float totalPrice;
    //单价
    private float unitPrice;
    //标签 例如满五唯一
    private String tag;
    //房屋信息  | 2室1厅 | 101.77平米 | 南 北 | 简装 | 有电梯
    private String description;
    //位置信息
    private String positionInfo;
    //小区
    private String community;
    //面积
    private float area;
    //标题
    private String title;
    //区域
    private String district;

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPositionInfo() {
        return positionInfo;
    }

    public void setPositionInfo(String positionInfo) {
        this.positionInfo = positionInfo;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistict(String district) {
        this.district = district;
    }

}
