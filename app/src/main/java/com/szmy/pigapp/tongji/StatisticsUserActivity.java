package com.szmy.pigapp.tongji;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


/**
 * 用户统计页面
 */
public class StatisticsUserActivity extends BaseActivity {
    private TextView mTvTotalPrice;
    private TextView mTvTotalNum;
    private TextView mTvTotalChulan;
    private TextView mTvStartDate;
    private TextView mTvEndDate;
    private Calendar cal = Calendar.getInstance();
    private String userId = "";
    private Button mBtnCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_user);
        userId = App.userID;
        if (getIntent().hasExtra("userID")) {
            userId = getIntent().getStringExtra("userID");
        }
        initView();
        loadData();
    }

    private void initView() {
        mTvTotalPrice = (TextView) findViewById(R.id.totalprice_tv);
        mTvTotalChulan = (TextView) findViewById(R.id.tv_totalchulan);
        mTvTotalNum = (TextView) findViewById(R.id.tv_totalnum);
        mTvStartDate = (TextView) findViewById(R.id.tv_start_date);
        mTvEndDate = (TextView) findViewById(R.id.tv_end_date);
        mBtnCheck = (Button) findViewById(R.id.checkbtn);

        cal.setTimeInMillis(System.currentTimeMillis());
        mTvStartDate.setText(cal.get(Calendar.YEAR) + "-"
                + AppStaticUtil.parseMonth(cal.get(Calendar.MONTH)) + "-01");
//        mTvEndDate.setText(cal.get(Calendar.YEAR) + "-"
//                + AppStaticUtil.parseMonth(cal.get(Calendar.MONTH)) + "-"
//                + cal.get(Calendar.DAY_OF_MONTH));

    }

    private void loadData() {
        showDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userID", userId);
        params.addBodyParameter("aod.startDate", mTvStartDate.getText().toString());
        params.addBodyParameter("aod.endDate", mTvEndDate.getText().toString());
        params.addBodyParameter("uuid", App.uuid);
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.GET_USER_DATA_URL,
                params, new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                    }
                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        disDialog();
                        Log.i("usertj", "  responseInfo.result = "
                                + responseInfo.result);
                        JSONObject jsonresult;
                        try {
                            jsonresult = new JSONObject(responseInfo.result);
                            if (jsonresult.get("success").toString()
                                    .equals("1")) {
                                if(!jsonresult.optString("allPrice").toString().equals("")&&!jsonresult.optString("allPrice").toString().equals("0")) {
                                    mTvTotalPrice.setText(jsonresult.optString("allPrice").toString());
                                }
                                if(!jsonresult.optString("allNumber").toString().equals("")&&!jsonresult.optString("allNumber").toString().equals("0")) {
                                    mTvTotalChulan.setText(jsonresult.optString("allNumber").toString());
                                }
                                if(!jsonresult.optString("allCount").toString().equals("")&&!jsonresult.optString("allCount").toString().equals("0")) {
                                    mTvTotalNum.setText(jsonresult.optString("allCount").toString());
                                }

                            } else {
                                successType(jsonresult.get("success")
                                        .toString(), "查询失败！");
                            }
                        } catch (JSONException e) {
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        showToast("服务器连接失败,请稍后再试！");
                        disDialog();

                    }
                });

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_start_date:
                showDateDialog(true);
                break;
            case R.id.tv_end_date:
                showDateDialog(false);
                break;
            case R.id.checkbtn:
                loadData();
                break;
        }
    }

    private void showDateDialog(final boolean isStart) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(StatisticsUserActivity.this,
                R.layout.dialog_date, null);
        final TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        if (isStart)
            tvTitle.setText("请选择开始日期");
        else
            tvTitle.setText("请选择结束日期");
        final DatePicker datePicker = (DatePicker) view
                .findViewById(R.id.date_picker);
        builder.setView(view);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH), null);
        if (isStart) {
            String date = mTvStartDate.getText().toString();
            String[] dates=date.split("-");
            if (TextUtils.isEmpty(date)||dates.length!=3) {
                datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH), null);
            }else {
                datePicker.init(Integer.parseInt(dates[0]),(Integer.parseInt(dates[1])-1),Integer.parseInt(dates[2]),null);
            }
        }else{
            String date = mTvEndDate.getText().toString();
            String[] dates=date.split("-");
            if (TextUtils.isEmpty(date)||dates.length!=3) {
                datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH), null);
            }else {
                datePicker.init(Integer.parseInt(dates[0]),Integer.parseInt(dates[1]),Integer.parseInt(dates[2]),null);
            }
        }
        builder.setPositiveButton("确 定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                datePicker.clearFocus();
                StringBuffer sb = new StringBuffer();
                sb.append(String.format("%d-%02d-%02d", datePicker.getYear(),
                        datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
                if (isStart)
                    mTvStartDate.setText(sb.toString());
                else
                    mTvEndDate.setText(sb.toString());
                dialog.cancel();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }
}
