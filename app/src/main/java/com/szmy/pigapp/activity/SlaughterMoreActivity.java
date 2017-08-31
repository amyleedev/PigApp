package com.szmy.pigapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.szmy.pigapp.entity.SlaughterhouseEntry;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONException;
import org.json.JSONObject;

public class SlaughterMoreActivity extends BaseActivity{
	ImageButton add_btn;
	ImageView picture;
	TextView more_name, more_business, more_phone, more_qq, more_address,
			more_slaughter_name, more_intro,more_number;
	TextView more_detial_address;
	SlaughterhouseEntry entry;
	private Button mBtnRz;
	private String id = "";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slaughter_more);
		((TextView) findViewById(R.id.def_head_tv)).setText("屠宰场详情");
		add_btn = (ImageButton) findViewById(R.id.add_btn);
		add_btn.setVisibility(View.GONE);
		initView();

	}

	private void initView() {
		Intent intent=getIntent();
		mBtnRz = (Button) findViewById(R.id.btn_rz);
		more_name = (TextView) findViewById(R.id.more_name);
		more_phone = (TextView) findViewById(R.id.more_phone);
		more_number =(TextView) findViewById(R.id.more_number);
		more_qq =(TextView) findViewById(R.id.more_qq);
		more_address =(TextView) findViewById(R.id.more_address);
		more_business =(TextView) findViewById(R.id.more_business);
		more_slaughter_name =(TextView) findViewById(R.id.more_slaughter_name);
		more_intro =(TextView) findViewById(R.id.more_intro);
		picture = (ImageView) findViewById(R.id.more_imageview);
		entry=(SlaughterhouseEntry) intent.getSerializableExtra("entity");
		id = entry.getId();
		RequestParams params = new RequestParams();
		params.addBodyParameter("e.id", entry.getId());
		if (!TextUtils.isEmpty(App.uuid)){
			params.addBodyParameter("uuid",App.uuid);
		}
		loadData(params);
//		setData(entry);
		mBtnRz.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TextUtils.isEmpty(App.uuid)) {
					showToast("请先登录！");
					Intent intent = new Intent (SlaughterMoreActivity.this,LoginActivity.class);
					startActivityForResult(intent,22);
				} else if(App.authentication.equals("0")){
					Intent intent = new Intent(SlaughterMoreActivity.this,SlaughterhouseRenZhengActivity.class);
					intent.putExtra("entity", entry);
					startActivityForResult(intent, App.SLAUGHTERRZ_CODE);
				}else{

				}
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		RequestParams params = new RequestParams();
		params.addBodyParameter("e.id", entry.getId());
		loadData(params);

	}

	private void setData(SlaughterhouseEntry entry){
		String address =entry.getProvince()+ " "+entry.getCity()+" "+entry.getArea();

		more_name.setText(TextUtils.isEmpty(entry.getRealName())?"暂无":entry.getRealName());
		String str=entry.getPhone();

		if (str.length() >= 11) {
			str=str.substring(0, 11);
			more_phone.setText(str.substring(0,
					str.length() - (str.substring(3)).length())
					+ "****" + str.substring(7));
		} else {
			more_phone.setText(str.substring(0, str.length())+"***");
		}

		if (entry.getNumber()==0) {
			more_number.setText("暂无");
		} else {
			more_number.setText(entry.getNumber().toString()+"头");
		}
		if (entry.getQQ().trim().equals("")) {
			more_qq.setText("暂无");
		} else {
			more_qq.setText(entry.getQQ());
		}
		if (address.trim().equals("")) {
			more_address.setText("暂无");
		} else {
			more_address.setText(address+"*****");
		}
		if (entry.getBusiness().trim().equals("")) {
			more_business.setText("暂无");
		} else {
			more_business.setText(entry.getBusiness());
		}
		String name = entry.getCompanyName();
//		if (entry.getStatus().equals("0")){
//			more_slaughter_name.setText(TextUtils.isEmpty(name)?"暂无":name);
//		}else{
//			if (name.indexOf("屠宰") != -1 && name.indexOf("屠宰") >= 1) {
//				name = "****" + name.substring(name.indexOf("屠宰"));
//			}
//			if (name.indexOf("双汇") != -1 && name.indexOf("双汇") >= 1) {
//				name = "****" + name.substring(name.indexOf("双汇"));
//			}
//			if (name.indexOf("有限公司") != -1 && name.indexOf("有限公司") >= 1) {
//				name = "****" + name.substring(name.indexOf("有限公司"));
//			}
//			if (name.indexOf("加工厂") != -1 && name.indexOf("加工厂") >= 1) {
//				name = "****" + name.substring(name.indexOf("加工厂"));
//			}
//			if (name.indexOf("肉联厂") != -1 && name.indexOf("肉联厂") >= 1) {
//				name = "****" + name.substring(name.indexOf("肉联厂"));
//			}
//			if (name.indexOf("猪厂") != -1 && name.indexOf("猪厂") >= 1) {
//				name = "****" + name.substring(name.indexOf("猪厂"));
//			}
//			if (name.indexOf("食品") != -1 && name.indexOf("食品") >= 1) {
//				name = "****" + name.substring(name.indexOf("食品"));
//			}
			if (name.trim().equals("")) {
				more_slaughter_name.setText("暂无");
			} else {
				more_slaughter_name.setText(name);
			}
//		}

		Log.i("intro", entry.getIntro());
		if (entry.getIntro().trim().equals("")) {
			more_intro.setText("暂无介绍");
		} else {
			more_intro.setText(entry.getIntro());
		}

		if (!TextUtils.isEmpty(entry.getPicture())) {
			com.nostra13.universalimageloader.core.ImageLoader
					.getInstance().displayImage(
					UrlEntry.ip + entry.getPicture(), picture,
					AppStaticUtil.getOptions());
		} else {
			picture.setImageResource(R.drawable.default_title);
		}
		if (entry.getStatus().equals("0")){
			if (!TextUtils.isEmpty(App.uuid)){
				//已登录
				if (App.authentication.equals("0")&&App.usertype.equals("2")){
					mBtnRz.setVisibility(View.VISIBLE);
				}else{
					mBtnRz.setVisibility(View.GONE);
				}
			}else{
				mBtnRz.setVisibility(View.VISIBLE);
			}

		}else{
			mBtnRz.setVisibility(View.GONE);
		}
	}

	public void loadData(RequestParams params) {
showDialog();
		System.out.println(App.uuid);

		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERYBYID_SLAUGHTERHOUSE_URL, params,
				new RequestCallBack<String>() {
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

						Log.i("skaughterhouse",
								" skaughterhouse responseInfo.result = "
										+ responseInfo.result);

						JSONObject jsonresult;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.get("success").toString()
									.equals("1")) {
							} else if(jsonresult.get("success").toString()
									.equals("0")) {
								App.mUserInfo.setUuid("");
								FileUtil.saveUser(SlaughterMoreActivity.this,
									App.mUserInfo);
							App.setDataInfo(App.mUserInfo);
							}
							SlaughterhouseEntry mInfoEntry;
							GsonBuilder gsonb = new GsonBuilder();
							Gson gson = gsonb.create();
							mInfoEntry = gson.fromJson(
									jsonresult.toString(),
									SlaughterhouseEntry.class);
							entry = mInfoEntry;
							setData(mInfoEntry);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						showToast("操作失败,请稍后再试！");
						disDialog();

					}
				});

	}
}
