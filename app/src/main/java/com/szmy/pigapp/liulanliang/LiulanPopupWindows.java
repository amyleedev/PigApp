package com.szmy.pigapp.liulanliang;//package com.szmy.pigapp.liulanliang;
//import android.app.Activity;
//import android.content.Context;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup.LayoutParams;
//import android.view.animation.AnimationUtils;
//import android.widget.AbsListView;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest;
//import com.szmy.pigapp.R;
//import com.szmy.pigapp.activity.BaseActivity;
//import com.szmy.pigapp.activity.PigFarmListActivity;
//import com.szmy.pigapp.adapter.FarmAdapter;
//import com.szmy.pigapp.entity.ZhuChang;
//import com.szmy.pigapp.utils.App;
//import com.szmy.pigapp.utils.UrlEntry;
//import com.szmy.pigapp.widget.MySwipeRefreshLayout;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 点击拍照弹出框
// *
// * @author qing
// */
//
//public class LiulanPopupWindows extends PopupWindow {
//
//    private LinearLayout mBtnexit;
//    private List<LiuLan> userinfo = new ArrayList<>();
//    private Context context;
//    private MySwipeRefreshLayout mSrlData;
//    private ListView mLv;
//    private View mFooterView;
//    private LinearLayout mLlFooterLoading;
//    private TextView mTvFooterResult;
//    private boolean mIsLoadAll = false;
//    private LinearLayout mLlLoading;
//    private LinearLayout mLlLoadFailure;
//    private boolean mIsLoading;
//    private int mPage;
//    private static final int PAGE_SIZE = 20;
//    private List<LiuLan> childJson = new ArrayList<LiuLan>(); // 子列表
//    private LiulanAdapter groupAdapter;
//
//    public LiulanPopupWindows() {
//        super();
//    }
//    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
//
//        @Override
//        public void onRefresh() {
//            reload();
//        }
//    };
//    private void load() {
//        reload();
//        mLlLoading.setVisibility(View.VISIBLE);
//        mSrlData.setVisibility(View.GONE);
//        mLlLoadFailure.setVisibility(View.GONE);
//    }
//
//    private void reload() {
//        mPage = 1;
//        mIsLoadAll = false;
//        loadData();
//    }
//
//    private View.OnClickListener mClickListener = new View.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.ll_load_failure:
//                    load();
//                    break;
//
//            }
//        }
//    };
//    public LiulanPopupWindows(Activity paramActivity,
//                              View.OnClickListener paramOnClickListener, View parent) {
////		super(paramActivity);
//        View view = View
//                .inflate(paramActivity, R.layout.liulandialog, null);
//        view.startAnimation(AnimationUtils.loadAnimation(paramActivity,
//                R.anim.fade_ins));
//        LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
//        ll_popup.startAnimation(AnimationUtils.loadAnimation(paramActivity,
//                R.anim.push_bottom_in_2));
////        this.userinfo = info;
//        this.context = paramActivity;
//        initView(view);
//
//        setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg));
//        setAnimationStyle(android.R.style.Animation_Dialog);
//        setWidth(LayoutParams.WRAP_CONTENT);
//        setHeight(LayoutParams.WRAP_CONTENT);
//        setFocusable(true);
//        setOutsideTouchable(true);
//        setContentView(view);
//        showAtLocation(parent, Gravity.CENTER, 120, 120);
//
//
//    }
//
//    private void initView(View view) {
//
//        mFooterView = LayoutInflater.from(context).inflate(
//                R.layout.footer_view, null);
//        mLlFooterLoading = (LinearLayout) mFooterView
//                .findViewById(R.id.ll_footer_loading);
//        mTvFooterResult = (TextView) mFooterView
//                .findViewById(R.id.tv_footer_result);
//        mSrlData = (MySwipeRefreshLayout) view.findViewById(R.id.srl_vehicle);
//
//        mSrlData.setOnRefreshListener(mRefreshListener);
//        mSrlData.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);
//        mLv = (ListView) view.findViewById(R.id.info_framgment_xlistview);
//        mLv.setOnScrollListener(mOnScrollListener);
//        LiulanAdapter groupAdapter = new LiulanAdapter(context, userinfo);
//        mLv.setAdapter(groupAdapter);
//        mLlLoading = (LinearLayout) view.findViewById(R.id.ll_loading);
//        mLlLoadFailure = (LinearLayout) view.findViewById(R.id.ll_load_failure);
//        mLlLoadFailure.setOnClickListener(mClickListener);
//
//        mBtnexit = (LinearLayout) view.findViewById(R.id.exit_btn1);
//        mBtnexit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss();
//            }
//        });
//    }
//    private void loadData() {
//        mIsLoading = true;
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("pager.offset", String.valueOf(mPage));
//        params.addBodyParameter("e.pageSize", String.valueOf(PAGE_SIZE));
//        params.addBodyParameter("uuid", App.uuid);
//        params.addBodyParameter("e.orderId", App.uuid);
//        final HttpUtils http = new HttpUtils();
//        http.send(HttpRequest.HttpMethod.POST, UrlEntry.LIULAN_URL, params,
//                new RequestCallBack<String>() {
//                    @Override
//                    public void onStart() {
//                    }
//
//                    @Override
//                    public void onLoading(long total, long current,
//                                          boolean isUploading) {
//                    }
//
//                    @Override
//                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                        mIsLoading = false;
//                        getData(responseInfo.result);
//                        Log.i("liulan", "  responseInfo.result = "
//                                + responseInfo.result);
//                    }
//
//                    @Override
//                    public void onFailure(HttpException error, String msg) {
//                        mIsLoading = false;
//                        if (mLlLoading.getVisibility() == View.VISIBLE) {
//                            mLlLoading.setVisibility(View.GONE);
//                            mLlLoadFailure.setVisibility(View.VISIBLE);
//                        } else {
//                            mSrlData.setRefreshing(false);
//                        }
//                        if (mPage > 1)
//                            mPage--;
//                    }
//                });
//
//    }
//    private void getData(String result) {
//        JSONObject jsonresult;
//        try {
//            jsonresult = new JSONObject(result);
//            if (jsonresult.get("success").toString().equals("1")) {
//                // Toast.makeText(mContext, "查询成功！", Toast.LENGTH_SHORT).show();
//
//                List<LiuLan> childJsonItem = new ArrayList<LiuLan>();
//                JSONArray jArrData = jsonresult.getJSONArray("list");
//                for (int i = 0; i < jArrData.length(); i++) {
//                    JSONObject jobj2 = jArrData.optJSONObject(i);
//                    GsonBuilder gsonb = new GsonBuilder();
//                    Gson gson = gsonb.create();
//                    LiuLan mInfoEntry = gson.fromJson(jobj2.toString(),
//                            LiuLan.class);
//                    childJsonItem.add(mInfoEntry);
//                }
//
//                if (mLlLoading.getVisibility() == View.VISIBLE) {
//                    mLlLoading.setVisibility(View.GONE);
//                    mSrlData.setVisibility(View.VISIBLE);
//                } else {
//                    mSrlData.setRefreshing(false);
//                }
//
//                if (mPage == 1) {
//                    if (childJson.size() >= 0) {
//                        childJson.clear();
//                        mLv.removeFooterView(mFooterView);
//
//                    }
//                    mLv.addFooterView(mFooterView);
//
//                    childJson.addAll(childJsonItem);
//                    groupAdapter = new LiulanAdapter(context,childJson);
//                    mLv.setAdapter(groupAdapter);
//                    if (childJsonItem.size() == 0) {
//                        mLlFooterLoading.setVisibility(View.GONE);
//                        mTvFooterResult.setVisibility(View.VISIBLE);
//                        mTvFooterResult.setText("查询结果为空");
//                        mIsLoadAll = true;
//                    } else if (childJsonItem.size() < 20) {
//                        mLlFooterLoading.setVisibility(View.GONE);
//                        mTvFooterResult.setVisibility(View.VISIBLE);
//                        mTvFooterResult.setText("已加载全部");
//                        mIsLoadAll = true;
//                    } else {
//                        mLlFooterLoading.setVisibility(View.VISIBLE);
//                        mTvFooterResult.setVisibility(View.GONE);
//                    }
//                } else {
//                    childJson.addAll(childJsonItem);
//                    groupAdapter = new LiulanAdapter(context,childJson);
//                    mLv.requestLayout();
//                    groupAdapter.notifyDataSetChanged();
//                    if (childJsonItem.size() < 20) {
//                        mLlFooterLoading.setVisibility(View.GONE);
//                        mTvFooterResult.setVisibility(View.VISIBLE);
//                        mTvFooterResult.setText("已加载全部");
//                        mIsLoadAll = true;
//                    } else {
//                        mLlFooterLoading.setVisibility(View.VISIBLE);
//                        mTvFooterResult.setVisibility(View.GONE);
//                    }
//                }
//
//                groupAdapter.notifyDataSetChanged();
//            } else {
////                successType(jsonresult.get("success").toString(), "查询失败！");
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//    private AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {
//
//        @Override
//        public void onScroll(AbsListView view, int firstVisibleItem,
//                             int visibleItemCount, int totalItemCount) {
//            if (firstVisibleItem + visibleItemCount == totalItemCount) {
//                if (mIsLoading)
//                    return;
//                if (mIsLoadAll)
//                    return;
//                mPage += 1;
//                loadData();
//            }
//        }
//
//        @Override
//        public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//        }
//    };
//}
