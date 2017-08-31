package com.szmy.pigapp.distributor;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/1 0001.
 */
public class Distributor implements Serializable {

    private String id;
    private String userID;//绑定帐号id
    private String createTime;//创建时间
    private String username;//绑定帐号
    private String phone;//联系方式
    private String area;//区
    private String reason;//不通过原因
    private String status;//状态:1为申请,2为通过,3不通过
    private String province;//省
    private String audit;//0:初次审查，1再次审查
    private String y;//坐标y
    private String x;//坐标x
    private String city;//市
    private String yyzz;//营业执照
    private String gspzs;//GSP证书
    private String mt;//门头照片
    private String address;
    private String fxsType;//分销商类型   null不是分销商    1:经销商  2:个人
    private String sfzZm;
    private String sfzFm;

    public String getSfzZm() {
        return sfzZm;
    }

    public void setSfzZm(String sfzZm) {
        this.sfzZm = sfzZm;
    }

    public String getSfzFm() {
        return sfzFm;
    }

    public void setSfzFm(String sfzFm) {
        this.sfzFm = sfzFm;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFxsType() {
        return fxsType;
    }

    public void setFxsType(String fxsType) {
        this.fxsType = fxsType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getAudit() {
        return audit;
    }

    public void setAudit(String audit) {
        this.audit = audit;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getYyzz() {
        return yyzz;
    }

    public void setYyzz(String yyzz) {
        this.yyzz = yyzz;
    }

    public String getGspzs() {
        return gspzs;
    }

    public void setGspzs(String gspzs) {
        this.gspzs = gspzs;
    }

    public String getMt() {
        return mt;
    }

    public void setMt(String mt) {
        this.mt = mt;
    }
}
