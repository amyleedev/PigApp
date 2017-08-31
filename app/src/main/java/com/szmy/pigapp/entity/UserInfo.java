package com.szmy.pigapp.entity;

import java.io.Serializable;


public class UserInfo implements Serializable {

    /**
     * {"address":"","area":"金水区","authentication":"1","city":"郑州市","endDate":"","id":"2026","list":[],"money":0,"offset":0,"pageSize":20,"pagerSize":0,"pagerUrl":"","password":"e10adc3949ba59abbe56e057f20f883e","phone":"15896325874","picture":"/appUpload/64CCB594-A84E-4296-BFF1-F7F402223510.jpg","presenter":"","province":"河南省","startDate":"","success":"1","total":0,"type":"1","username":"艾米","uuid":"EE644DC1-8313-4F57-9410-AF89E33B981D","x":34.800343,"y":113.692673}
     */
    private static final long serialVersionUID = 1L;

    private String id = "";
    private String address = "";
    private String province = "";
    private String city = "";
    private String area = "";
    private String authentication = "";
    private String password = "";

    private String phone = "";
    private String picture = "";
    private String type = "";
    private String username = "";
    private String uuid = "";
    private String x = "";
    private String y = "";//
    private String presenter = "";//推荐人

    private String fxsType = "";//分销商类型   null不是分销商    1:经销商  2:个人(审核通过才有值)
    private String scrytype = "";//1、普通市场 2、管理员 3、非市场人员

    private String source = "";//3：猪贸通用户 1：神州牧易用户

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getScrytype() {
        return scrytype;
    }

    public void setScrytype(String scrytype) {
        this.scrytype = scrytype;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getPresenter() {
        return presenter;
    }

    public void setPresenter(String presenter) {
        this.presenter = presenter;
    }


    public String getFxsType() {
        return fxsType;
    }

    public void setFxsType(String fxsType) {
        this.fxsType = fxsType;
    }


}