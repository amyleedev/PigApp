package com.szmy.pigapp.weather;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/4 0004.
 */
public class Weather implements Serializable {

    private String daytype;//天气是今天还是后几天还是历史today  forecast history
    private String  date;// "2015-08-03", //今天日期
    private String week;//: "星期一",    //今日星期
    private String curTemp;//: "28℃",    //当前温度
    private String aqi;//: "92",        //pm值
    private String fengxiang;//: "无持续风向", //风向
    private String fengli;//: "微风级",     //风力
    private String hightemp;//: "30℃",   //最高温度
    private String lowtemp;//: "23℃",    //最低温度
    private String type;//: "阵雨",      //天气状态

    public String getDaytype() {
        return daytype;
    }

    public void setDaytype(String daytype) {
        this.daytype = daytype;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getCurTemp() {
        return curTemp;
    }

    public void setCurTemp(String curTemp) {
        this.curTemp = curTemp;
    }

    public String getFengxiang() {
        return fengxiang;
    }

    public void setFengxiang(String fengxiang) {
        this.fengxiang = fengxiang;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getHightemp() {
        return hightemp;
    }

    public void setHightemp(String hightemp) {
        this.hightemp = hightemp;
    }

    public String getFengli() {
        return fengli;
    }

    public void setFengli(String fengli) {
        this.fengli = fengli;
    }

    public String getLowtemp() {
        return lowtemp;
    }

    public void setLowtemp(String lowtemp) {
        this.lowtemp = lowtemp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
