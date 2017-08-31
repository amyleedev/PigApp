package com.szmy.pigapp.mynotices;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/1 0001.
 */
public class MyNotice implements Serializable {
    private String id;
    private String modelID; // 模版id
    private String senderID; // 发送者的id
    private String receiverID; // 接收者的id（userid）
    private String title; // 消息标题
    private String content; // 消息内容
    private String startTime; // 创建时间
    private String endTime; // 过期时间
    /**
     * xd2u1 下单时对发布人推送，
     * xd2u2 下单时对下单人推送，
     * fb2u2s 发布时对匹配下单人列表推送，
     * cj2zc 订单成交推送给猪场优惠券信息，
     * rz2zc 猪场认证通过推送给猪场优惠券信息
     * rz 认证通过推送给认证用户，
     * gg 公告
     */
    private String type;
    // json串，包含 id（订单id）,userid（发布者id）,ordertype（订单类型，1出售，2收购，）
    private String costom;
    private String status; // 状态，0：未读，1：已读

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModelID() {
        return modelID;
    }

    public void setModelID(String modelID) {
        this.modelID = modelID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCostom() {
        return costom;
    }

    public void setCostom(String costom) {
        this.costom = costom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
