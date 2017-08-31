package com.szmy.pigapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ����
 * 
 * @author Administrator
 * 
 */
public class EcaluateActivity extends BaseActivity {
	private Button mSubmit;
	private EditText mContent;
	private RatingBar mRatingBar;
	private String orderType;
	private String orderID;
	private String productName;
	private String bpjrType;
	private String bpjr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.evaluate_activity);
		((TextView) findViewById(R.id.def_head_tv)).setText("评价");
		Intent intent = getIntent();
		orderType = intent.getStringExtra("orderType");
		orderID = intent.getStringExtra("orderID");
		productName = intent.getStringExtra("productName");
		if (intent.hasExtra("bpjrType")) {
			bpjrType = intent.getStringExtra("bpjrType");
			bpjr = intent.getStringExtra("bpjr");
		}
		initView();
	}

	private void initView() {
		mSubmit = (Button) findViewById(R.id.btn_submit);
		mSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadData();
			}
		});
		mContent = (EditText) findViewById(R.id.ecaluate_et);
		mRatingBar = (RatingBar) findViewById(R.id.app_ratingbar);
		mRatingBar
				.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						// showToast("rating:"+String.valueOf(rating));
					}
				});

	}

	public void loadData() {
		if (mRatingBar.getRating() == 0.0) {
			showToast("请选择等级评分！");
			return;
		}
		if (mContent.getText().toString().length() < 5) {
			showToast("评价内容不能少于5个字！");
			return;
		}
		showDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		params.addBodyParameter("e.content", mContent.getText().toString());
		params.addBodyParameter("e.star",
				String.valueOf(mRatingBar.getRating()));
		params.addBodyParameter("e.orderType", orderType);// 订单类型(出售:1,收购:2)(物流:3
															// )(平台:4)
		params.addBodyParameter("e.orderID", orderID);
		params.addBodyParameter("e.productID", orderID);
		params.addBodyParameter("e.productName", productName);
		params.addBodyParameter("e.userName", App.username);
		params.addBodyParameter("e.parentID", "0");// 被评价人用户类型(买方:1)(卖方:2)
		params.addBodyParameter("e.bpjr", bpjr);// 被评价人
		params.addBodyParameter("e.bpjrType", bpjrType); // 被评价人用户类型(买方:1)(卖方:2)
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.INSERT_EVALUATE_URL,
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
						String result = responseInfo.result;
						try {
							JSONObject json = new JSONObject(result);
							if (json.optString("success").equals("1")) {
								showToast("评价成功！");
								setResult(11);
								finish();
							} else {
								successType(json.optString("success"), "评论失败！");
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
