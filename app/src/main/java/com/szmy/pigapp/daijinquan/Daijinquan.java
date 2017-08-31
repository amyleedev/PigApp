package com.szmy.pigapp.daijinquan;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/26 0026.
 */
public class Daijinquan implements Serializable {
    private String id;
    private String amount;
    private String endTime;//有效期
    private String orientationName;////定向目标名称，那一个栏目的商品可以使用这个代金券，myyx:牧易优选
    private String status;//状态  0:未使用，1己下单未支付，2已支付
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOrientationName() {
        return orientationName;
    }

    public void setOrientationName(String orientationName) {
        this.orientationName = orientationName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
