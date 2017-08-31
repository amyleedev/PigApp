package com.szmy.pigapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.entity.ZhuChang;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONException;
import org.json.JSONObject;

public class PigFarmMoreActivity extends BaseActivity{
	ImageButton add_btn;
	LinearLayout mLlBack;
	ZhuChang entry;
	LinearLayout mLlRz;
	LinearLayout mLlSfrz;
	LinearLayout mLlYhkrz;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pigfarm_more);
		((TextView) findViewById(R.id.def_head_tv)).setText("养殖场详情");
		add_btn = (ImageButton) findViewById(R.id.add_btn);
		mLlBack = (LinearLayout) findViewById(R.id.def_head_back);
		mLlBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		add_btn.setVisibility(View.GONE);
		initView();

	}
	private void initView() {
		mLlRz = (LinearLayout) findViewById(R.id.rzlayout);
		mLlSfrz = (LinearLayout) findViewById(R.id.order_del);
		mLlYhkrz = (LinearLayout) findViewById(R.id.order_toedit);
		Intent intent=getIntent();
		entry=(ZhuChang) intent.getSerializableExtra("entity");
		String address =entry.getProvince()+ " "+entry.getCity()+" "+entry.getArea();
		
		
		String name = entry.getName();
		if (name.indexOf("养") != -1) {
			name = "***" + name.substring(name.indexOf("养"));
		} else if (name.indexOf("猪") != -1) {
			name = "***" + name.substring(name.indexOf("猪"));
		} else {
			name = "***" + name;
		}
		
		((TextView) findViewById(R.id.pigfarm_name)).setText(name);
		
		String str=entry.getPhone();
		if (str.length() >= 11) {
			str=str.substring(0, 11);
			((TextView) findViewById(R.id.pigfarm_phone)).setText(str.substring(0,
					str.length() - (str.substring(3)).length())
					+ "****" + str.substring(7));
		} else {
			((TextView) findViewById(R.id.pigfarm_phone)).setText(str.substring(0, str.length())+"***");
		}
		
		
		if (entry.getAmount().trim().equals("")) {
			((TextView) findViewById(R.id.pigfarm_amount)).setText("暂无");
		} else {
			((TextView) findViewById(R.id.pigfarm_amount)).setText(entry.getAmount()+"头");
		}
		
		
		if (entry.getSales().trim().equals("")) {
			((TextView) findViewById(R.id.pigfarm_sales)).setText("暂无");
		} else {
			((TextView) findViewById(R.id.pigfarm_sales)).setText(entry.getSales()+"头/年");
		}

		if (address.trim().equals("")) {
			((TextView) findViewById(R.id.pigfarm_address)).setText("暂无");
		} else {
			((TextView) findViewById(R.id.pigfarm_address)).setText(address+"*****");
		}

		if (entry.getUsername().trim().equals("")) {
			((TextView) findViewById(R.id.pigfarm_username)).setText("暂无");
		} else {
			((TextView) findViewById(R.id.pigfarm_username)).setText(entry.getUsername());
		}

		
		if (entry.getEmail().trim().equals("")) {
			((TextView) findViewById(R.id.pigfarm_email)).setText("暂无");
		} else {
			((TextView) findViewById(R.id.pigfarm_email)).setText(entry.getEmail());
		}
		
		String pigtype=pigType(entry.getPigType());
		if (pigtype.equals("")) {
			((TextView) findViewById(R.id.pigfarm_pigType)).setText("暂无");
		} else {
			((TextView) findViewById(R.id.pigfarm_pigType)).setText(pigtype);
		}
		
		String type=farmType(entry.getType());
		if (type.equals("")) {
			((TextView) findViewById(R.id.pigfarm_Type)).setText("暂无");
		} else {
			((TextView) findViewById(R.id.pigfarm_Type)).setText(type);
		}
		
	
		if (!TextUtils.isEmpty(entry.getCoverPicture())) {
			com.nostra13.universalimageloader.core.ImageLoader.getInstance()
					.displayImage(UrlEntry.ip + entry.getCoverPicture(),
							((ImageView)findViewById(R.id.more_imageview)), AppStaticUtil.getOptions());
		} else {
			((ImageView)findViewById(R.id.more_imageview)).setImageResource(R.drawable.default_title);
		}
		if (!TextUtils.isEmpty(entry.getStatus().toString())){
			if (entry.getStatus().toString().equals("2")){
				mLlRz.setVisibility(View.VISIBLE);
				mLlSfrz.setVisibility(View.VISIBLE);
			}
		}
		if(!TextUtils.isEmpty(entry.getUserID())){
			getIsSmrz(entry.getUserID());
		}
	}
	
	

	
	
	private String farmType(String type) {
		if (TextUtils.isEmpty(type))
			return "";
		if (type.equals("1")) {
			return "小规模养殖";
		} else if (type.equals("2"))
			return "中规模养殖";
		else if (type.equals("3"))
			return "大规模养殖";
		else if (type.equals("4"))
			return "集团规模养殖";
		return type;
	}

	private String pigType(String type) {
		if (TextUtils.isEmpty(type))
			return "";
		if (type.equals("1")) {
			return "内三元";
		} else if (type.equals("2")) {
			return "外三元";
		} else if (type.equals("3")) {
			return "土杂猪";
		}else if (type.equals("4")) {
			return "肥猪";
		}else if (type.equals("5")) {
			return "仔猪";
		}else if (type.equals("6")) {
			return "种猪";
		}
		return type;
	}

	private void getIsSmrz(String userid) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("userid", userid);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.IS_QIANYUE_CARD,
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
						Log.i("pay", " pay responseInfo.result = "
								+ responseInfo.result);
						JSONObject jsonresult = null;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.get("success").toString()
									.equals("1")) {
								if(jsonresult.optString("issmrz").equals("1")){
									mLlRz.setVisibility(View.VISIBLE);
									mLlYhkrz.setVisibility(View.VISIBLE);

								}
							} else {

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
