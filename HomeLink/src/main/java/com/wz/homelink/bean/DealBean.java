package com.wz.homelink.bean;

/**
 * Created by wz on 2017/4/14.
 */
public class DealBean {
    //车公庄北里 2室1厅 70.4平米
    private String title;
    //南 北 | 其他 | 无电梯
    private String houseInfo;
    //2017.03.27
    private String dealDate;
    //挂牌总价
    private float showTotalPrice;
    //成交总价
    private float dealTotalPrice;
    //成交均价
    private float dealUnitPrice;
    //成交周期
    private int dealCircleTime;
    //标签 房屋满五年
    private String tag;
    //链家成交
    private String source;
    //底层(共6层) 1995年建板楼
    private String positionInfo;
    //区
    private String district;
    //详情页
    private String detailUrl;
    //面积
    private float area;
    //小区
    private String community;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHouseInfo() {
        return houseInfo;
    }

    public void setHouseInfo(String houseInfo) {
        this.houseInfo = houseInfo;
    }

    public String getDealDate() {
        return dealDate;
    }

    public void setDealDate(String dealDate) {
        this.dealDate = dealDate;
    }

    public float getShowTotalPrice() {
        return showTotalPrice;
    }

    public void setShowTotalPrice(float showTotalPrice) {
        this.showTotalPrice = showTotalPrice;
    }

    public float getDealTotalPrice() {
        return dealTotalPrice;
    }

    public void setDealTotalPrice(float dealTotalPrice) {
        this.dealTotalPrice = dealTotalPrice;
    }

    public float getDealUnitPrice() {
        return dealUnitPrice;
    }

    public void setDealUnitPrice(float dealUnitPrice) {
        this.dealUnitPrice = dealUnitPrice;
    }

    public int getDealCircleTime() {
        return dealCircleTime;
    }

    public void setDealCircleTime(int dealCircleTime) {
        this.dealCircleTime = dealCircleTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPositionInfo() {
        return positionInfo;
    }

    public void setPositionInfo(String positionInfo) {
        this.positionInfo = positionInfo;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public float getArea() {
        return area;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }
}
