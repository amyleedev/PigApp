package com.szmy.pigapp.updateservice;

import android.content.Context;

import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;




/**
 * 获取服务器版本信息
 */
public class UpdateInfoService {
    public UpdateInfoService(Context context) {
    }

    public UpdateInfo getUpDateInfo() throws Exception {
        String path = UrlEntry.CHECK_VERSION_URL;
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        try {
            // 创建一个url对象
            URL url = new URL(path);
            // 通過url对象，创建一个HttpURLConnection对象（连接）
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            // 通过HttpURLConnection对象，得到InputStream
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            // 使用io流读取文件
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        UpdateInfo updateInfo = new UpdateInfo();
         if(sb.toString().equals("")){
             return null;
         }
        System.out.print("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"+sb.toString());
        JSONObject jsonresult = new JSONObject(sb.toString());
        if(jsonresult.optString("success").equals("1")){
            updateInfo.setVersion(jsonresult.optString("versionCode"));
            updateInfo.setVersionName(jsonresult.optString("versionName"));
            updateInfo.setDescription(jsonresult.optString("content"));
            updateInfo.setUrl(UrlEntry.ip + jsonresult.optString("url"));
        }else{
            return null;
        }
        return updateInfo;
    }

}