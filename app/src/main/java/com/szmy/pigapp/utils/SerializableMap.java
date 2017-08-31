package com.szmy.pigapp.utils;

/**
 * Created by Administrator on 2017/5/3 0003.
 */

import com.szmy.pigapp.image.ImageItem;

import java.io.Serializable;
import java.util.Map;

/**
 * 序列化map供Bundle传递map使用
 * Created  on 13-12-9.
 */
public class SerializableMap implements Serializable {

    private Map<String, ImageItem> picMaps;
    private Map<String, String> urlmaps ;
    private Map<String,String> datamap;

    public Map<String, String> getDatamap() {
        return datamap;
    }

    public void setDatamap(Map<String, String> datamap) {
        this.datamap = datamap;
    }

    public Map<String, String> getUrlmaps() {
        return urlmaps;
    }

    public void setUrlmaps(Map<String, String> urlmaps) {
        this.urlmaps = urlmaps;
    }

    public Map<String, ImageItem> getPicMaps() {
        return picMaps;
    }

    public void setPicMaps(Map<String, ImageItem> picMaps) {
        this.picMaps = picMaps;
    }
}