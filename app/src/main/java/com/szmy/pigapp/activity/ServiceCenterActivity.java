package com.szmy.pigapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.pigdiagnosis.ZhenDuanActivity;
import com.szmy.pigapp.utils.UrlEntry;

/*
 * 
 */
public class ServiceCenterActivity extends BaseActivity {
    private LinearLayout mLlZcjs, mLlFygl, mLlSygl, mLlZcgl, mLlYzjs;
    private RelativeLayout mLlYzjs1,mLlZbsy1,mLlJczl1;
    private LinearLayout mLlZbfy, mLlZbjx, mLlSycs, mLlZbdq, mLlZbsy;
    private LinearLayout mLlJczl,mLlFbksj,mLlZjzx,mLlZddiy;
    private RadioButton mTvYzJiantou, mTvZbJiantou,mRbJcJiantou;

    /**
     *
     *
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_center);
        initView();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        ((TextView) findViewById(R.id.def_head_tv)).setText("服务中心");
        mLlZcjs = (LinearLayout) findViewById(R.id.ll_zcjs);
        mLlZcjs.setOnClickListener(mClickListener);
        mLlZbsy1 = (RelativeLayout) findViewById(R.id.zbsy_layout);
        mLlZbsy1.setOnClickListener(mClickListener2);
        mLlYzjs1 = (RelativeLayout) findViewById(R.id.yzjs_layout);
        mLlYzjs1.setOnClickListener(mClickListener2);
         mLlJczl1 = (RelativeLayout) findViewById(R.id.jczl_layout);
        mLlJczl1.setOnClickListener(mClickListener2);

        mLlFygl = (LinearLayout) findViewById(R.id.ll_fygl);
        mLlFygl.setOnClickListener(mClickListener);
        mLlSygl = (LinearLayout) findViewById(R.id.ll_sygl);
        mLlSygl.setOnClickListener(mClickListener);
        mLlZcgl = (LinearLayout) findViewById(R.id.ll_zcgl);
        mLlZcgl.setOnClickListener(mClickListener);
        mLlZbfy = (LinearLayout) findViewById(R.id.ll_zbfy);
        mLlZbfy.setOnClickListener(mClickListener);
        mLlZbjx = (LinearLayout) findViewById(R.id.ll_zbjx);
        mLlZbjx.setOnClickListener(mClickListener);
        mLlSycs = (LinearLayout) findViewById(R.id.ll_sycs);
        mLlSycs.setOnClickListener(mClickListener);
        mLlZbdq = (LinearLayout) findViewById(R.id.ll_zbdq);
        mLlZbdq.setOnClickListener(mClickListener);
        mLlFbksj = (LinearLayout) findViewById(R.id.ll_fbksj);
        mLlFbksj.setOnClickListener(mClickListener);
        mLlZjzx = (LinearLayout) findViewById(R.id.ll_zjzx);
        mLlZjzx.setOnClickListener(mClickListener);
        mLlZddiy = (LinearLayout) findViewById(R.id.ll_zddiy);
        mLlZddiy.setOnClickListener(mClickListener);
        mLlYzjs = (LinearLayout) findViewById(R.id.yzjslayout);
        mLlZbsy = (LinearLayout) findViewById(R.id.zbsylayout);
        mLlJczl = (LinearLayout) findViewById(R.id.jczllayout);
        mTvYzJiantou = (RadioButton) findViewById(R.id.yzjs);
        mTvYzJiantou.setOnClickListener(mClickListener2);
//        mTvYzJiantou.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (mLlYzjs.getVisibility() == View.VISIBLE) {
//                    mLlYzjs.setVisibility(View.GONE);
//                    mTvYzJiantou.setChecked(true);
//                } else {
//                    mLlYzjs.setVisibility(View.VISIBLE);
//                    mTvYzJiantou.setChecked(false);
//                }
//
//            }
//        });
        mTvZbJiantou = (RadioButton) findViewById(R.id.zbxuanz);
        mTvZbJiantou.setOnClickListener(mClickListener2);
//        mTvZbJiantou.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(mLlZbsy.getVisibility() ==View.VISIBLE){
//						mLlZbsy.setVisibility(View.GONE);
//                    mTvZbJiantou.setChecked(true);
//					}else{
//						mLlZbsy.setVisibility(View.VISIBLE);
//                    mTvZbJiantou.setChecked(false);
//					}
//            }
//        });
        mRbJcJiantou = (RadioButton) findViewById(R.id.jczljt);
        mRbJcJiantou.setOnClickListener(mClickListener2);
//        mRbJcJiantou.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(mLlJczl.getVisibility() ==View.VISIBLE){
//                    mLlJczl.setVisibility(View.GONE);
//                    mRbJcJiantou.setChecked(true);
//                }else{
//                    mLlJczl.setVisibility(View.VISIBLE);
//                    mRbJcJiantou.setChecked(false);
//                }
//            }
//        });
    }
    private OnClickListener mClickListener2 = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.yzjs:
                case R.id.yzjs_layout:
                    if (mLlYzjs.getVisibility() == View.VISIBLE) {
                        mLlYzjs.setVisibility(View.GONE);
                        mTvYzJiantou.setChecked(true);
                    } else {
                        mLlYzjs.setVisibility(View.VISIBLE);
                        mTvYzJiantou.setChecked(false);
                    }
                    break;
                case R.id.zbsy_layout:
                case R.id.zbxuanz:
                    if (mLlZbsy.getVisibility() == View.VISIBLE) {
                        mLlZbsy.setVisibility(View.GONE);
                        mTvZbJiantou.setChecked(true);
                    } else {
                        mLlZbsy.setVisibility(View.VISIBLE);
                        mTvZbJiantou.setChecked(false);
                    }
                    break;
                case R.id.jczl_layout:
                case R.id.jczljt:
                    if (mLlJczl.getVisibility() == View.VISIBLE) {
                        mLlJczl.setVisibility(View.GONE);
                        mRbJcJiantou.setChecked(true);

                    } else {
                        mLlJczl.setVisibility(View.VISIBLE);
                        mRbJcJiantou.setChecked(false);

                    }

                    break;
            }
        }
    };

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ServiceCenterActivity.this,
                    NewsListActivity.class);
            switch (v.getId()) {
                case R.id.ll_zcjs:
                    intent.putExtra("type", "猪场建设");
                    break;
                case R.id.ll_fygl:
                    intent.putExtra("type", "繁育管理");
                    break;
                case R.id.ll_sygl:
                    intent.putExtra("type", "饲养管理");
                    break;
                case R.id.ll_zcgl:
                    intent.putExtra("type", "猪场管理");
                    break;
                case R.id.ll_zbfy:
                    intent.putExtra("type", "猪病防疫");
                    break;
                case R.id.ll_zbjx:
                    intent.putExtra("type", "猪病解析");
                    break;
                case R.id.ll_sycs:
                    intent.putExtra("type", "兽药常识");
                    break;
                case R.id.ll_zbdq:
                    intent.putExtra("type", "猪病大全");
                    break;
                case R.id.ll_fbksj:
                    intent = new Intent(ServiceCenterActivity.this, ActivityScanResult.class);
                    intent.putExtra("url", UrlEntry.GET_SONGJIAN_INFO_URL);
//                    intent.putExtra("type","zhenduan");
                    break;
                case R.id.ll_zjzx:
                     intent = new Intent(ServiceCenterActivity.this, ActivityScanResult.class);
                    intent.putExtra("url", UrlEntry.GET_ZHUANJIA_INFO_URL);
                    intent.putExtra("type","zhenduan");
                    break;
                case R.id.ll_zddiy:
                    intent = new Intent(ServiceCenterActivity.this,
                            ZhenDuanActivity.class);
                    break;


            }
            startActivity(intent);
        }
    };
}
