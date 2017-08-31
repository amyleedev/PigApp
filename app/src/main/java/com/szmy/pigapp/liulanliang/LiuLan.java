package com.szmy.pigapp.liulanliang;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public class LiuLan implements Serializable {
    private String userID;//用户id
    private String createTime;//日期
    private String title;//标题
    private String phone;//电话
    private String orderID;//订单id
    private String userName;//用户名称
    private String fbr;//发布人

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFbr() {
        return fbr;
    }

    public void setFbr(String fbr) {
        this.fbr = fbr;
    }
}
