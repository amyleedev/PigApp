package com.szmy.pigapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.utils.UrlEntry;

public class LineChartActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    public String title = "";
    public static String type = "";
    private WebView mWvLine;
    private String province = "";
    private String city = "";
    private WebSettings webSettings;
    private RadioGroup mRGroup1;
    private RadioGroup mRGroup2;
    private RadioButton checkRadioButton;
    private Boolean changeGroup = false;
    private TextView mTvTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        province = getIntent().getStringExtra("province");
        city = getIntent().getStringExtra("city");
        ((TextView) findViewById(R.id.def_head_tv)).setText("价格走势图");
        mTvTip = (TextView) findViewById(R.id.text_tip);
        mTvTip.setText(city+"最近一月价格走势图");
        mWvLine = (WebView) findViewById(R.id.wv_line);
        mRGroup1 = (RadioGroup) findViewById(R.id.rg_radio);
        mRGroup2 = (RadioGroup) findViewById(R.id.rg_radio2);
        if(type.equals("nsy")){
            // 改变默认选项
            mRGroup1.check(R.id.btn_nsy);
            changeGroup = true;
            mRGroup2.clearCheck();
            changeGroup = false;
        }else if(type.equals("wsy")){
            changeGroup = true;
            mRGroup2.clearCheck();
            changeGroup = false;
            mRGroup1.check(R.id.btn_wsy);
        }else if(type.equals("tzz")){
            mRGroup1.check(R.id.btn_tzz);
            changeGroup = true;
            mRGroup2.clearCheck();
            changeGroup = false;
        }else if(type.equals("ym")){
            mRGroup2.check(R.id.btn_ym);
            changeGroup = true;
            mRGroup1.clearCheck();
            changeGroup = false;
        }else if(type.equals("dp")){
            mRGroup2.check(R.id.btn_dp);
            changeGroup = true;
            mRGroup1.clearCheck();
            changeGroup = false;
        }else if(type.equals("zlb")){
            mRGroup2.check(R.id.btn_zlb);
            changeGroup = true;
            mRGroup1.clearCheck();
            changeGroup = false;
        }
        // 获取默认被被选中值
//        checkRadioButton = (RadioButton) mRGroup.findViewById(mRGroup
//                .getCheckedRadioButtonId());
        // 注册事件
        mRGroup1.setOnCheckedChangeListener(this);
        mRGroup2.setOnCheckedChangeListener(this);

        mWvLine.loadUrl(UrlEntry.GETGRAPHDATA_URL + "?e.province=" + province + "&e.city=" + city + "&priceType=" + type);
        mWvLine.requestFocusFromTouch();
        webSettings = mWvLine.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // �LOAD_CACHE_ELSE_NETWORK


        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWvLine.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (group != null && checkedId > -1 && changeGroup == false) {
            checkRadioButton = (RadioButton) group.findViewById(checkedId);
            if (checkRadioButton.getText().equals("外三元")) {
                type = "wsy";
            } else if (checkRadioButton.getText().equals("内三元")) {
                type = "nsy";
            } else if (checkRadioButton.getText().equals("土杂猪")) {
                type = "tzz";
            } else if (checkRadioButton.getText().equals("玉米")) {
                type = "ym";
            } else if (checkRadioButton.getText().equals("豆粕")) {
                type = "dp";
            } else if (checkRadioButton.getText().equals("猪粮比")) {
                type = "zlb";
            }
            Log.i("ffsfa",UrlEntry.GETGRAPHDATA_URL + "?e.province=" + province + "&e.city=" + city + "&priceType=" + type);

            mWvLine.loadDataWithBaseURL(null, "","text/html", "utf-8",null);
            mWvLine.loadUrl(UrlEntry.GETGRAPHDATA_URL + "?e.province=" + province + "&e.city=" + city + "&priceType=" + type);
            if (group == mRGroup1) {
                changeGroup = true;
                mRGroup2.clearCheck();
                changeGroup = false;
            } else if (group == mRGroup2) {
                changeGroup = true;
                mRGroup1.clearCheck();
                changeGroup = false;
            }
        }
    }

}
