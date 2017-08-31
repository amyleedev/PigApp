package com.szmy.pigapp.quotedprice;

import java.io.Serializable;

/**
 * 报价
 * Created by Administrator on 2017/6/12 0012.
 */
public class QuotedPrice implements Serializable {
    private String id;
    private String marketingTime;//出栏时间
    private String number;//数量
    private String zzt;//有无装猪台0,1
    private String price;//价格
    private String finalPrice;//结算价
    private String type;//报价类型(1.出售 2.收购)
    private String productType;//品种(内三元,外三元,土杂猪)
    private String province;//省
    private String city;//市
    private String area;//区
    private String remark;//备注
    private String maxWeight;//最大体重值
    private String minWeight;//最小体重值
    private String iszd;
    private String userType;
    private String createTime;
    private String userName;
    private String isfree;//是否查看过报价

    public String getIsfree() {
        return isfree;
    }

    public void setIsfree(String isfree) {
        this.isfree = isfree;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getIszd() {
        return iszd;
    }

    public void setIszd(String iszd) {
        this.iszd = iszd;
    }

    public String getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(String maxWeight) {
        this.maxWeight = maxWeight;
    }

    public String getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(String minWeight) {
        this.minWeight = minWeight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarketingTime() {
        return marketingTime;
    }

    public void setMarketingTime(String marketingTime) {
        this.marketingTime = marketingTime;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getZzt() {
        return zzt;
    }

    public void setZzt(String zzt) {
        this.zzt = zzt;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
