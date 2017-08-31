package com.szmy.pigapp.pigdiagnosis;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/24 0024.
 */
public class ZhenDuan implements Serializable {
    private String id;
    private String name;
    private String discription;

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
