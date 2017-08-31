package com.szmy.pigapp.quotedprice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * 新增报价
 */
public class QuotedPriceDetailActivity extends BaseActivity implements
		OnClickListener {

	private TextView mEtFinalPrice,mEtPrice,mEtNum,mEtWeight,mEtRemark;
	private TextView mTvPrice,mTvTime,mTvNum,mTvAddress,mTvType,mTvMarkingTime,mTvBaojiaren,iszhuangzhutai,mTvAddressTip;
	private RelativeLayout mLlFinalPrice,mLlZhuangzhutai,mLlAddress;
	private Button btnSubmit;
	private int newtype = 0;
	private final int ADDRESS_CODE = 0x22;
	private final int PIGTYPE_CODE = 0x23;
	private String province = "";
	private String city = "";
	private String area = "";
	private RadioButton mRbYes,mRbNo;
	private String[] mItems = {"内三元", "外三元", "土杂猪"};
	private int mSingleChoiceID = 0;
	private QuotedPrice mQpEntry;
	private Calendar cal = Calendar.getInstance();
	private String id = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quotedprice_detail);
		((TextView) findViewById(R.id.def_head_tv)).setText("报价详情");
		id = getIntent().getStringExtra("id");
		initView();
		loadlastData();
	}
	private void initView() {

		mEtFinalPrice = (TextView) findViewById(R.id.et_finalprice);
		mEtPrice = (TextView) findViewById(R.id.et_price);
		mEtNum = (TextView) findViewById(R.id.et_count);
		mEtWeight = (TextView) findViewById(R.id.et_weight);
		mTvAddressTip = (TextView) findViewById(R.id.shouzhuquyu_tv);
		mEtRemark = (TextView) findViewById(R.id.et_remark);
		mTvAddress = (TextView) findViewById(R.id.province_city_tv_content);
		mTvBaojiaren = (TextView) findViewById(R.id.baojia_tv_content);
		mTvPrice = (TextView) findViewById(R.id.price_tv);
		mTvTime = (TextView) findViewById(R.id.time_tv);
		mTvMarkingTime = (TextView) findViewById(R.id.leave_start_timebox_tv_content);
//		mTvMarkingTime.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				showDateDialog();
//			}
//		});
		mTvNum = (TextView) findViewById(R.id.count_tv);
		mTvType = (TextView) findViewById(R.id.pinzhong_tv_content);
		mLlFinalPrice = (RelativeLayout) findViewById(R.id.jiesuanjia_layout);
		mLlZhuangzhutai = (RelativeLayout) findViewById(R.id.zhuangzhutailayout);
		iszhuangzhutai = (TextView) findViewById(R.id.iszhuangzhutai);



	}
private  void  setInitView(){
	mTvType.setText(mQpEntry.getProductType());
	mEtPrice.setText(mQpEntry.getPrice());
	mEtFinalPrice.setText(mQpEntry.getFinalPrice());
	mEtNum.setText(mQpEntry.getNumber());
	if (TextUtils.isEmpty(mQpEntry.getMinWeight())&&!TextUtils.isEmpty(mQpEntry.getMaxWeight())){
		mEtWeight.setText(mQpEntry.getMaxWeight());
	}else if(TextUtils.isEmpty(mQpEntry.getMaxWeight())&&!TextUtils.isEmpty(mQpEntry.getMinWeight())){
		mEtWeight.setText(mQpEntry.getMinWeight());
	}else  if(!TextUtils.isEmpty(mQpEntry.getMaxWeight())&&!TextUtils.isEmpty(mQpEntry.getMinWeight()))
	mEtWeight.setText(mQpEntry.getMinWeight()+"--"+mQpEntry.getMaxWeight());
	mTvBaojiaren.setText(mQpEntry.getUserName());
	mEtRemark.setText(mQpEntry.getRemark());
	if (!TextUtils.isEmpty(mQpEntry.getZzt())){
		if (mQpEntry.getZzt().equals("0")){
			iszhuangzhutai.setText("无");
		}else{
			iszhuangzhutai.setText("有");
		}
	}
	province = mQpEntry.getProvince();
	city = mQpEntry.getCity();
	area = mQpEntry.getArea();
	mTvAddress.setText(province + ""+city + ""+area);
	cal.setTimeInMillis(System.currentTimeMillis());
	if (!TextUtils.isEmpty(mQpEntry.getMarketingTime()))
	mTvMarkingTime.setText(mQpEntry.getMarketingTime().substring(5,16));

	if (mQpEntry.getUserType().equals("1")){
		mLlZhuangzhutai.setVisibility(View.VISIBLE);
	}else if(mQpEntry.getUserType().equals("2")){
		mLlFinalPrice.setVisibility(View.VISIBLE);
		mTvPrice.setText("收购价：");
		mTvTime.setText("收购时间：");
		mTvNum.setText("收购数量：");
		mTvAddressTip.setText("收猪区域：");
	}else if (mQpEntry.getUserType().equals("3")){
		mTvPrice.setText("收购价：");
		mTvTime.setText("收购时间：");
		mTvNum.setText("收购数量：");
		mTvAddressTip.setText("收猪区域：");
	}
}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Bundle bundle;
		switch (resultCode) {
			case ADDRESS_CODE:
				if (data == null)
					return;
				bundle = data.getExtras();
				province = bundle.getString("province");
				city = bundle.getString("city");
				area = bundle.getString("area");
				mTvAddress.setText(province + " " + city + " "
						+ area);
				break;
			case PIGTYPE_CODE:
				if (data == null)
					return;
				bundle = data.getExtras();
//				pigType = bundle.getString("pigType");
				String pigTypeName = bundle.getString("pigTypeName");
				mTvType.setText(pigTypeName);
				break;
		}
	}
	private void showDateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = View.inflate(QuotedPriceDetailActivity.this,
				R.layout.dialog_date, null);
		final DatePicker datePicker = (DatePicker) view
				.findViewById(R.id.date_picker);
		builder.setView(view);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				(cal.get(Calendar.DAY_OF_MONTH)+1), null);

			String date = mTvMarkingTime.getText().toString();
			String[] dates=date.split("-");
			if (TextUtils.isEmpty(date)||dates.length!=3) {
				datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
						(cal.get(Calendar.DAY_OF_MONTH)+1), null);
			}else {
				datePicker.init(Integer.parseInt(dates[0]),(Integer.parseInt(dates[1])-1),Integer.parseInt(dates[2]),null);
			}

		builder.setPositiveButton("确 定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				datePicker.clearFocus();
				StringBuffer sb = new StringBuffer();
				sb.append(String.format("%d-%02d-%02d", datePicker.getYear(),
						datePicker.getMonth() + 1, datePicker.getDayOfMonth()));

				mTvMarkingTime.setText(sb.toString());

				dialog.cancel();
			}
		});
		android.app.Dialog dialog = builder.create();
		dialog.show();
	}
	private void loadlastData() {
		showDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		params.addBodyParameter("id", id);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.GET_QUOTED_PRICE_DTEAIL_URL,
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
//						btnSubmit.setEnabled(true);
						Log.i("smrz", " smrz responseInfo.result = "
								+ responseInfo.result);
						JSONObject jsonresult = null;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.optString("success").toString()
									.equals("1")) {

								GsonBuilder gsonb = new GsonBuilder();
								Gson gson = gsonb.create();
								mQpEntry = new QuotedPrice();
								mQpEntry = gson.fromJson(jsonresult.toString(),QuotedPrice.class);
								setInitView();


							}  else {
								successType(jsonresult.optString("success").toString(), "操作失败");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						disDialog();
					}
				});
	}
}
