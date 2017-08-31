package com.szmy.pigapp.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.materialdesign.widgets.Dialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 更多天气
 */
public class WeatherActivity extends BaseActivity {

    private LinearLayout lin;
    private LayoutInflater inflater;
    private List<Weather> childlist = new ArrayList<>();
    private Map<String, List<Weather>> map = new HashMap<>();
    private TextView mTvTodayWd;
    private TextView mTvTodayType;
    private TextView mTvToadyFl;
    private TextView mTvTital;
    private TextView mTvWeek;
    private TextView mTvType;
    private TextView mTvWendu;
    private TextView mTvFl;
    private TextView mTvFs;
    private TextView mTvSd;
    private TextView mTvTg;
    private TextView mTvXc;
    private TextView mTvYd;
    private TextView mTvLs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        String cityid = getIntent().getStringExtra("cityId");
        loadWeather(cityid);
        initView();
    }

    private void initView() {
        inflater = LayoutInflater.from(this);
        lin = (LinearLayout) findViewById(R.id.mainlayout);
        mTvTodayWd = (TextView) findViewById(R.id.tv_todaywendu);
        mTvTodayType = (TextView) findViewById(R.id.tv_todaytype);
        mTvToadyFl = (TextView) findViewById(R.id.tv_todayfengli);
        mTvTital = (TextView) findViewById(R.id.tv_cityname);
        mTvFs = (TextView) findViewById(R.id.tv_price01);
        mTvSd = (TextView) findViewById(R.id.tv_price02);
        mTvTg = (TextView) findViewById(R.id.tv_price03);
        mTvXc = (TextView) findViewById(R.id.tv_price04);
        mTvYd = (TextView) findViewById(R.id.tv_price05);
        mTvLs = (TextView) findViewById(R.id.tv_price06);
    }

    private void addView(Weather w,String type) {
        LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.item_weather_list, null).findViewById(R.id.ll_itemweather);
        mTvWeek = (TextView) layout.findViewById(R.id.tv_week);
        mTvType = (TextView) layout.findViewById(R.id.tv_type2);
        mTvWendu = (TextView) layout.findViewById(R.id.tv_wendu);
        mTvFl = (TextView) layout.findViewById(R.id.tv_fengli2);
//        lin.removeAllViews();
        mTvWeek.setText(w.getWeek()+" ( " +w.getDate()+" )");
        mTvWendu.setText(w.getHightemp()+"  |  "+w.getLowtemp());
        mTvType.setText(w.getType());
        mTvFl.setText(w.getFengxiang()+"，"+w.getFengli());
        if(type.equals("history")){
            mTvWeek.setTextColor(getResources().getColor(R.color.gray));
            mTvWendu.setTextColor(getResources().getColor(R.color.gray));
            mTvType.setTextColor(getResources().getColor(R.color.gray));
            mTvFl.setTextColor(getResources().getColor(R.color.gray));
        }
        lin.addView(layout);
    }


    private void loadWeather(String cityid) {
        showDialog();
        RequestParams params = new RequestParams();
        params.addHeader("apikey", App.WEATHERAPIKEY);
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                UrlEntry.WEATAHER_MORE_URL + "?cityid="
                        + cityid, params, new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        System.out.println("--" + responseInfo.result +
                                "--");
                        parseWeather(responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        disDialog();
                    }
                });
    }

    private void parseWeather(String data) {
        disDialog();
        map.clear();
        childlist.clear();
        try {
            JSONObject obj = new JSONObject(data);
             String str = obj.toString();
            obj = new JSONObject(str.replace("℃","°"));
            int errNum = obj.optInt("errNum");
            if (errNum != 0) {
                final Dialog dialog = new Dialog(WeatherActivity.this, "提示", "获取天气失败，请稍候再试。");
                dialog.addCancelButton("取消");
                dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        finish();
                    }
                });
                dialog.setOnCancelButtonClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        finish();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
//                AlertDialog.Builder builder = new AlertDialog.Builder(
//                        WeatherActivity.this);
//                builder.setMessage("获取天气失败，请稍候再试。");
//                builder.setTitle("提示");
//                builder.setPositiveButton("确定",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                finish();
//                            }
//                        });
//                builder.setCancelable(false);
//                builder.create().show();
                return;
            }
            JSONObject o = obj.optJSONObject("retData");
            mTvTital.setText(o.optString("city"));
            if (o.has("today")) {
                List<Weather> todaylist = new ArrayList<>();
                JSONObject today = o.optJSONObject("today");
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                Weather mInfoEntry = gson.fromJson(today.toString(),
                        Weather.class);
                mInfoEntry.setWeek("今天");
                mInfoEntry.setDate((today.optString("date").substring(5,10)).replace("-","/"));
                childlist.add(mInfoEntry);
                todaylist.add(mInfoEntry);

                map.put("today", todaylist);
                setTopView(mInfoEntry);
                setZhiShu(today);

            }
            if (o.has("forecast")) {
                List<Weather> forecastlist = new ArrayList<>();
                JSONArray jArrData = o.getJSONArray("forecast");
                for (int i = 0; i < jArrData.length(); i++) {
                    JSONObject jobj2 = jArrData.optJSONObject(i);
                    GsonBuilder gsonb = new GsonBuilder();
                    Gson gson = gsonb.create();
                    Weather mInfoEntry = gson.fromJson(jobj2.toString(),
                            Weather.class);
                    if (i == 0){
                        mInfoEntry.setWeek("明天");
                    }
                    mInfoEntry.setDate((jobj2.optString("date").substring(5,10)).replace("-","/"));
                    childlist.add(mInfoEntry);
                    forecastlist.add(mInfoEntry);
                }
                map.put("forecast",forecastlist);
            }
            if(o.has("history")){
                List<Weather> historylist = new ArrayList<>();
                JSONArray jArrData = o.getJSONArray("history");
//                for (int i = 0; i < jArrData.length(); i++) {
                    JSONObject jobj2 = jArrData.optJSONObject(jArrData.length()-1);
                    GsonBuilder gsonb = new GsonBuilder();
                    Gson gson = gsonb.create();
                    Weather mInfoEntry = gson.fromJson(jobj2.toString(),
                            Weather.class);
                    mInfoEntry.setDate((jobj2.optString("date").substring(5,10)).replace("-","/"));

                        mInfoEntry.setWeek("昨天");

                    childlist.add(mInfoEntry);
                    historylist.add(mInfoEntry);
//                }
                map.put("history", historylist);
            }

            setListView();

        } catch (JSONException e) {
            final Dialog dialog = new Dialog(WeatherActivity.this, "提示", "获取天气失败，请稍候再试。");
            dialog.addCancelButton("取消");
            dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    finish();
                }
            });
            dialog.setOnCancelButtonClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    finish();
                }
            });
            dialog.setCancelable(false);
            dialog.show();
//            AlertDialog.Builder builder = new AlertDialog.Builder(
//                    WeatherActivity.this);
//            builder.setMessage("获取天气失败，请稍候再试。");
//            builder.setTitle("提示");
//            builder.setPositiveButton("确定",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog,
//                                            int which) {
//                            finish();
//                        }
//                    });
//            builder.setCancelable(false);
//            builder.create().show();
        }
    }
    private void setTopView(Weather w){
        mTvTodayWd.setText(w.getCurTemp());
        mTvTodayType.setText(w.getType());
        mTvToadyFl.setText(w.getFengxiang()+"，"+ w.getFengli());
    }

    private void setListView(){
        List<Weather> list = map.get("history");
        for(int i = 0 ;i <list.size() ;i ++){
            addView(list.get(i),"history");
        }
        List<Weather> list2 = map.get("today");
        addView(list2.get(0),"");
        List<Weather> list3 = map.get("forecast");
        for(int i = 0 ;i <list3.size() ;i ++){
            addView(list3.get(i),"");
        }
    }
    private void setZhiShu(JSONObject o){
        JSONArray jArrData = null;
        try {
            jArrData = o.getJSONArray("index");
            System.out.print(jArrData);
            for (int i = 0; i < jArrData.length(); i++) {
                JSONObject jobj2 = jArrData.optJSONObject(i);
                if(jobj2.optString("code").equals("fs")){
                 mTvFs.setText(jobj2.optString("name")+":"+jobj2.optString("index"));
                }
                if(jobj2.optString("code").equals("ct")){
                    mTvTg.setText(jobj2.optString("name")+":"+jobj2.optString("index"));
                }
                if(jobj2.optString("code").equals("yd")){
                    mTvYd.setText(jobj2.optString("name")+":"+jobj2.optString("index"));
                }
                if(jobj2.optString("code").equals("xc")){
                    mTvXc.setText(jobj2.optString("name")+":"+jobj2.optString("index"));
                }
                if(jobj2.optString("code").equals("ls")){
                    mTvLs.setText(jobj2.optString("name")+":"+jobj2.optString("index"));
                }
                if(jobj2.optString("code").equals("gm")){
                    mTvSd.setText(jobj2.optString("name")+":"+(jobj2.optString("index").equals("")?"暂无":jobj2.optString("index")));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
