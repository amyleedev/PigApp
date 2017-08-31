package com.szmy.pigapp.utils;

import com.alibaba.fastjson.JSON;


/**
 * 版本信息
 * */
public class Version {
	public String content="";//更新内容
	public String versionCode="1";//版本CODE
	public String apk_name="";//apk名称
	public String url="";//apk下载地址
	public String versionName ="";

	public static Version parse(String result){
		return JSON.parseObject(result, Version.class);
	}

}