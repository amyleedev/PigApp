package com.szmy.pigapp.tongji;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.activity.ChoseProvinceActivity;
import com.szmy.pigapp.adapter.StatisticsAdminAdapter;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 管理员统计页面
 */
public class StatisticsAdminActivity extends BaseActivity implements
		OnChartValueSelectedListener {
	private TextView mTvStartDate;
	private TextView mTvEndDate;
	private TextView mTvArea;
	private ClearEditText mEtName;
	private String mProvince = "";// 省
	private String mCity = "";// 市
	private String mArea = "";// 区
	private Calendar cal = Calendar.getInstance();

	private Button mBtnStatistics;
	private boolean mIsLoading;
	private boolean mIsLoadAll = false;
	private int mPage;
	private static final int PAGE_SIZE = 20;
	private View mFooterView;
	private LinearLayout mLlFooterLoading;
	private TextView mTvFooterResult;
	private StatisticsAdminAdapter mAdapter;
	private LinearLayout mLlTitle;
	private TextView mTvYzcsl;
	private TextView mTvCjzj;
	private TextView mTvCll;
	private TextView mTvFbts;



	private PieChart mChartJy, mChartYh;
	private Typeface tf;
	private TextView mTvZjje,mTvCdjy,mTvZcjy,mTvSzjc;
	private TextView mTvZYhl,mTvYzc,mTvJjr,mTvTzc;
	protected String[] mPartiesjy = new String[] { "养殖场", "经纪人", "屠宰场" };
	protected String[] mPartiesyh = new String[] { "产地交易", "生猪进厂交易", "直采交易" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics_admin);
		initView();
	}

	private void initView() {

		mFooterView = getLayoutInflater().inflate(R.layout.footer_view, null);
		mLlFooterLoading = (LinearLayout) mFooterView
				.findViewById(R.id.ll_footer_loading);
		mTvFooterResult = (TextView) mFooterView
				.findViewById(R.id.tv_footer_result);
		mTvStartDate = (TextView) findViewById(R.id.tv_start_date);
		mTvStartDate.setOnClickListener(mClickListener);
		mTvEndDate = (TextView) findViewById(R.id.tv_end_date);
		mTvEndDate.setOnClickListener(mClickListener);
		mTvArea = (TextView) findViewById(R.id.tv_area);
		mTvArea.setOnClickListener(mClickListener);
		mEtName = (ClearEditText) findViewById(R.id.et_name);
		mEtName.setOnClickListener(mClickListener);

		mBtnStatistics = (Button) findViewById(R.id.btn_statictics);
		mBtnStatistics.setOnClickListener(mClickListener);
		cal.setTimeInMillis(System.currentTimeMillis());
		mTvStartDate.setText(cal.get(Calendar.YEAR) + "-"
				+ AppStaticUtil.parseMonth(cal.get(Calendar.MONTH)) + "-01");
		// mTvEndDate.setText(cal.get(Calendar.YEAR) + "-"
		// + AppStaticUtil.parseMonth(cal.get(Calendar.MONTH)) + "-"
		// + cal.get(Calendar.DAY_OF_MONTH));
		mLlTitle = (LinearLayout) findViewById(R.id.ll_title);
		mTvZjje = (TextView) findViewById(R.id.tv_zjjy);
        mTvZcjy = (TextView) findViewById(R.id.tv_zcms);
        mTvCdjy = (TextView) findViewById(R.id.tv_cdjy);
        mTvSzjc = (TextView) findViewById(R.id.tv_szjc);
		mTvZYhl = (TextView) findViewById(R.id.tv_zyhl);
		mTvYzc = (TextView) findViewById(R.id.tv_yzc);
		mTvJjr = (TextView) findViewById(R.id.tv_jjr);
		mTvTzc = (TextView) findViewById(R.id.tv_tzc);
		mChartJy = (PieChart) findViewById(R.id.chart1);
		mChartYh = (PieChart) findViewById(R.id.chart2);
//		setStyle(mChartJy);
//		setStyle(mChartYh);
		loadData();
	}

	private void setStyle(PieChart mChart, 
			ArrayList<String> xVals, ArrayList<Entry> yVals1,String total) {
		mChart.setUsePercentValues(true);

		// change the color of the center-hole
		mChart.setHoleColor(Color.rgb(235, 235, 235));

		mChart.setTransparentCircleRadius(64f); // 半透明圈
		tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

		// mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(),
		// "OpenSans-Light.ttf"));

		mChart.setHoleRadius(50f);//半径

		mChart.setDescription("");//描述

		mChart.setDrawCenterText(false);
		/*
		 * 设置饼图中心是否是空心的 true 中间是空心的，环形图 false 中间是实心的 饼图
		 */
		mChart.setDrawHoleEnabled(true);
		mChart.setHoleColorTransparent(false);// 设置中间空心圆孔的颜色是否透明
		mChart.setRotationAngle(0);
		// enable rotation of the chart by touch
		mChart.setRotationEnabled(true);

		// mChart.setUnit(" €");
		// mChart.setDrawUnitsInChart(true);

		// add a selection listener
		mChart.setOnChartValueSelectedListener(this);
		// mChart.setTouchEnabled(false);

//		mChart.setCenterText("合计\n\n100000元");// 饼状图中间的文字
		// mChart.setCenterTextSize(20);
		mChart.setCenterTextColor(getResources().getColor(R.color.red));
		// mChart.setBackgroundColor(getResources().getColor(R.color.blue));
		 setData(mChart,xVals, yVals1,total);

		mChart.animateXY(1000, 1000);
		// mChart.spin(2000, 0, 360);

		Legend l = mChart.getLegend();
		l.setPosition(LegendPosition.BELOW_CHART_RIGHT);
		l.setXEntrySpace(7f);
		l.setYEntrySpace(5f);
	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_start_date:
				showDateDialog(true);
				break;
			case R.id.tv_end_date:
				showDateDialog(false);
				break;
			case R.id.btn_statictics:
				reload();
				break;
			case R.id.tv_area:
				Intent intent = new Intent();
				intent.setClass(StatisticsAdminActivity.this,
						ChoseProvinceActivity.class);
				startActivityForResult(intent, App.ADDRESS_CODE);
				break;
			}
		}
	};

	private void showDateDialog(final boolean isStart) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				StatisticsAdminActivity.this);
		View view = View.inflate(StatisticsAdminActivity.this,
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

	private void reload() {
		mPage = 1;
		mIsLoadAll = false;
		// mLv.removeAllViews();
		showDialog();
		loadData();
	}

	public void loadData() {
		mIsLoading = true;
		System.out.println("--" + mTvStartDate.getText().toString());
		System.out.println("--" + mTvEndDate.getText().toString());
		RequestParams params = new RequestParams();
		Map<String, String> maplist = new TreeMap<String, String>();
		maplist.put("uuid", App.uuid);
//		params.addBodyParameter("api_sign", ZmtSignBean.sign(maplist));
		params.addBodyParameter("uuid", App.uuid);
		params.addBodyParameter("e.province", mProvince);
		params.addBodyParameter("e.city", mCity);
		params.addBodyParameter("e.area", mArea);
		params.addBodyParameter("e.startDate", mTvStartDate.getText()
				.toString()); // 起始日期
		params.addBodyParameter("e.endDate", mTvEndDate.getText().toString());// 结束日期
		// params.addBodyParameter("aod.compName",
		// mEtName.getText().toString());// 企业名称
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.GET_ADMIN_DATA_URL,
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
						mIsLoading = false;
						System.out.println("--responseInfo.result = "
								+ responseInfo.result);
						parseData(responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						disDialog();
						mIsLoading = false;
						if (mPage > 1)
							mPage--;
						else
							disDialog();
						;
					}
				});
	}

	private void parseData(String result) {
		disDialog();
		JSONObject jsonresult;
		try {
			jsonresult = new JSONObject(result);
			if (jsonresult.get("success").toString().equals("1")) {
                mTvZjje.setText(jsonresult.optString("amount")+"元");
                mTvCdjy.setText(jsonresult.optString("cdjy")+"元");
                mTvZcjy.setText(jsonresult.optString("zcjy")+"元");
                mTvSzjc.setText(jsonresult.optString("jcjy")+"元");
				mTvZYhl.setText(jsonresult.optString("userall")+"家");
				mTvJjr.setText(jsonresult.optString("jjr")+"人");
				mTvTzc.setText(jsonresult.optString("tzc")+"家");
				mTvYzc.setText(jsonresult.optString("yzc")+"家");

				ArrayList<Entry> yVals1 = new ArrayList<Entry>();
				Map<String,Float> map1 =new HashMap<>();
				if(jsonresult.optDouble("cdjy")>0)
					map1.put("cdjy",(float) jsonresult.optDouble("cdjy"));
				if(jsonresult.optDouble("jcjy")>0)
					map1.put("jcjy", (float) jsonresult.optDouble("jcjy"));
				if(jsonresult.optDouble("zcjy")>0)
					map1.put("zcjy",(float) jsonresult.optDouble("zcjy"));
				int i = 0;
				for (String key : map1.keySet()) {
					System.out.println("key= "+ key + " and value= " + map1.get(key));
					yVals1.add(new Entry(map1.get(key),i));
					i++;
				}


				ArrayList<String> xVals = new ArrayList<String>();	
				xVals.add("产地");
				xVals.add("生猪进厂");
				xVals.add("直采");

				setStyle(mChartJy, xVals, yVals1, jsonresult.optDouble("amount")+"元");
				ArrayList<Entry> yVals2 = new ArrayList<Entry>();
				Map<String,Float> map =new HashMap<>();
				if(jsonresult.optInt("yzc")>0)
					map.put("yzc",(float) jsonresult.optInt("yzc"));
				if(jsonresult.optInt("jjr") > 0)
					map.put("jjr", (float) jsonresult.optInt("jjr"));
				if(jsonresult.optInt("tzc") > 0)
				map.put("tzc",(float) jsonresult.optInt("tzc"));
				int j = 0;
				for (String key : map.keySet()) {
					System.out.println("key= "+ key + " and value= " + map.get(key));
					yVals2.add(new Entry(map.get(key),j));
					j++;
				}
				ArrayList<String> xVals2 = new ArrayList<String>();	
				xVals2.add("养殖场");
				xVals2.add("经纪人");
				xVals2.add("屠宰场");
				setStyle(mChartYh, xVals2, yVals2, jsonresult.optString("userall")+"家");

			}
		} catch (JSONException e) {
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case App.ADDRESS_CODE:
			if (data == null)
				return;
			Bundle b = data.getExtras();
			mProvince = b.getString("province");
			if (mProvince.equals("不限")) {
				mProvince = "";
				mCity = "";
				mArea = "";
			}
			mCity = b.getString("city");
			if (mCity.equals("不限")) {
				mCity = "";
				mArea = "";
			}
			mArea = b.getString("area");
			if (mArea.equals("不限"))
				mArea = "";
			if (TextUtils.isEmpty(mProvince) && TextUtils.isEmpty(mCity)
					&& TextUtils.isEmpty(mArea))
				mTvArea.setText("");
			else
				mTvArea.setText(mProvince + " " + mCity + " " + mArea);
			break;
		}
	}

	private void setData(PieChart mChart, 
			ArrayList<String> xVals, ArrayList<Entry> yVals1,String total) {


		mChart.setCenterText("合计\n\n"+total);// 饼状图中间的文字
		PieDataSet dataSet = new PieDataSet(yVals1, "");
		dataSet.setSliceSpace(3f);

		// add a lot of colors
		 int[] COLORFUL_COLORS = new int[]{Color.rgb(211, 84, 84), Color.rgb(255, 221, 0), Color.rgb(25,140,206)};
		ArrayList<Integer> colors = new ArrayList<Integer>();

//		for (int c : ColorTemplate.VORDIPLOM_COLORS)
//			colors.add(c);

//		for (int c : ColorTemplate.JOYFUL_COLORS)
//			colors.add(c);

		for (int c : COLORFUL_COLORS)
			colors.add(c);
//
//		for (int c : ColorTemplate.LIBERTY_COLORS)
//			colors.add(c);
//
//		for (int c : ColorTemplate.PASTEL_COLORS)
//			colors.add(c);

//		colors.add(ColorTemplate.getHoloBlue());

		dataSet.setColors(colors);

		PieData data = new PieData(xVals, dataSet);
		data.setValueFormatter(new PercentFormatter());
		data.setValueTextSize(11f);
		data.setValueTextColor(Color.BLACK);
		data.setValueTypeface(tf);
		mChart.setData(data);

		// undo all highlights
		mChart.highlightValues(null);

		mChart.invalidate();
	}

	@Override
	public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

		if (e == null)
			return;
		Log.i("VAL SELECTED",
				"Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
						+ ", DataSet index: " + dataSetIndex);
	}

	@Override
	public void onNothingSelected() {
		Log.i("PieChart", "nothing selected");
	}

}
