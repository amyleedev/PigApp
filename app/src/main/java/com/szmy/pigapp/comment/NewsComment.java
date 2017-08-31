package com.szmy.pigapp.comment;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/22 0022.
 */
public class NewsComment implements Serializable {
    private String id;
    private String content;//内容
    private String createAccount;//创建人
    private String type;//0:评论   1:回复
    private String cid;//评论Id
    private String createTime;
    private String newId;//资讯Id
    private String picture;//头像
    private String dzsl;//点赞数量
    private String plsl;//评论数量
    private String sfdz;//是否点赞

    public String getSfdz() {
        return sfdz;
    }

    public void setSfdz(String sfdz) {
        this.sfdz = sfdz;
    }

    public String getPlsl() {
        return plsl;
    }

    public void setPlsl(String plsl) {
        this.plsl = plsl;
    }

    public String getDzsl() {
        return dzsl;
    }

    public void setDzsl(String dzsl) {
        this.dzsl = dzsl;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateAccount() {
        return createAccount;
    }

    public void setCreateAccount(String createAccount) {
        this.createAccount = createAccount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getNewId() {
        return newId;
    }

    public void setNewId(String newId) {
        this.newId = newId;
    }
}
