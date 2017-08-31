package com.szmy.pigapp.vehicle;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.image.CustomConstants;
import com.szmy.pigapp.image.ImageItem;
import com.szmy.pigapp.image.ImagePublishAdapter;
import com.szmy.pigapp.image.ImageZoomActivity;
import com.szmy.pigapp.image.PopupWindows;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.HttpUtil;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@SuppressLint("SimpleDateFormat")
public class NewVehicleActivity extends BaseActivity {


	private EditText et_chexing, et_count, et_carnumber,
			et_phonePeople, et_phone, et_price, et_remark;

	private TextView et_startdate, et_enddate;
	private Button btn_submit;
	private String id;
	private String province = "";
	private String city = "";
	private String area = "";
	private List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
	private TimePopupWindows timepopuwindow;
	private Button mNextBtn, mBackBtn;
	private LinearLayout mLlFirst;
	private LinearLayout mLlTwo;
	private LinearLayout mLlBack;
	private String[] mItems = { "生猪", "仔猪" };
	private TextView mTvPigType;
	private EditText mEtJuli;
	private int mSingleChoiceID = 0;
	public static List<ImageItem> mDataList = new ArrayList<ImageItem>();
	private ImagePublishAdapter mAdapter;
	private GridView mGridView;
	private PopupWindow mPwDialog;
	private int TAKE_PHOTO;
	private int TAKE_PICTURE;
	private File mFile;
	private String path = "";
	private TextView mTvTishi;
	private Vehicle mVehicle;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_vehicle);
		if (App.mUserInfo == null) {
			App.mUserInfo = FileUtil.readUser(NewVehicleActivity.this);
		}
		App.setDataInfo(App.mUserInfo);
		initView();
		if (getIntent().hasExtra("upp")) {
			url = UrlEntry.UPDATE_VEHIVLE_BYID_URL;

			mVehicle = (Vehicle) getIntent().getSerializableExtra("mVehicle");
			((TextView) findViewById(R.id.def_head_tv)).setText("修改物流信息");

			setViewData(mVehicle);
		} else {
			url = UrlEntry.ADD_VEHIVLE_BYID_URL;
			((TextView) findViewById(R.id.def_head_tv)).setText("发布物流信息");
		}

		
		
	}

	public void initView() {
		mLlBack = (LinearLayout) findViewById(R.id.def_head_back);
		et_chexing = (EditText) findViewById(R.id.et_chexing);
		et_count = (EditText) findViewById(R.id.et_count);
//		et_type = (EditText) findViewById(R.id.et_type);
		et_carnumber = (EditText) findViewById(R.id.et_carnumber);
		et_phonePeople = (EditText) findViewById(R.id.et_phonePeople);
		et_phone = (EditText) findViewById(R.id.et_phone);
		mTvTishi = (TextView) findViewById(R.id.tishi);
		et_startdate = (TextView) findViewById(R.id.et_startdate);
		et_startdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideInput();
				timepopuwindow = new TimePopupWindows(NewVehicleActivity.this,
						et_startdate, "start");

			}
		});
		et_enddate = (TextView) findViewById(R.id.et_enddate);
		et_enddate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideInput();
				timepopuwindow = new TimePopupWindows(NewVehicleActivity.this,
						et_enddate, "end");

			}
		});
		et_price = (EditText) findViewById(R.id.et_price);
		et_remark = (EditText) findViewById(R.id.et_remark);

		btn_submit = (Button) findViewById(R.id.btn_submit);

		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(mTvPigType.getText().toString())
						|| TextUtils.isEmpty(mEtJuli.getText().toString())) {
					showToast("带*为必填项,请检查！");
					return;
				}
				if (!FileUtil.isPrice(et_price.getText().toString())) {
					showToast("您输入的价格格式不正确!");
					return;
				}
				if (App.usertype.equals("5") || App.usertype.equals("2")
						|| App.usertype.equals("1")) {
					showDialog1(NewVehicleActivity.this,
							"发布信息失败，认证物流公司才可以发布物流信息。");
					// showToast("发布信息失败，认证物流公司才可以发布物流信息。");

				} else
					sendData();
			}
		});
		mDataList = getTempFromPref(CustomConstants.PREF_TEMP_IMAGES);
		mGridView = (GridView) findViewById(R.id.gridview);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mAdapter = new ImagePublishAdapter(this, mDataList);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == getDataSize()) {
					TAKE_PHOTO = CustomConstants.TAKE_PICTURE;
					TAKE_PICTURE = CustomConstants.TAKE_PICTURE1;
					showPopDialog(mGridView);
				} else {
					Intent intent = new Intent(NewVehicleActivity.this,
							ImageZoomActivity.class);
					intent.putExtra(CustomConstants.EXTRA_IMAGE_LIST,
							(Serializable) mDataList);
					intent.putExtra(CustomConstants.EXTRA_CURRENT_IMG_POSITION,
							position);
					intent.putExtra("name", "newvehicle");
					startActivity(intent);
				}
			}
		});

		mLlFirst = (LinearLayout) findViewById(R.id.layoutone);
		mLlTwo = (LinearLayout) findViewById(R.id.layouttwo);

		mNextBtn = (Button) findViewById(R.id.btn_next);
		mNextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(et_chexing.getText().toString())
						|| TextUtils.isEmpty(et_startdate.getText().toString())
						|| TextUtils.isEmpty(et_enddate.getText().toString())
						|| TextUtils.isEmpty(et_carnumber.getText().toString())
						|| TextUtils.isEmpty(et_count.getText().toString())
						|| TextUtils.isEmpty(et_phonePeople.getText()
								.toString())
						|| TextUtils.isEmpty(et_price.getText().toString())
						|| TextUtils.isEmpty(et_phone.getText().toString())) {
					showToast("带*为必填项,请检查！");
					return;
				}
				hideInput();
				mLlFirst.setVisibility(View.GONE);
				mLlTwo.setVisibility(View.VISIBLE);

			}
		});

		mBackBtn = (Button) findViewById(R.id.btn_goback);
		mBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mLlTwo.setVisibility(View.GONE);
				mLlFirst.setVisibility(View.VISIBLE);

			}
		});
		mLlBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((mLlTwo.getVisibility() == View.VISIBLE)
						&& (mLlFirst.getVisibility() == View.GONE)) {
					mLlTwo.setVisibility(View.GONE);
					mLlFirst.setVisibility(View.VISIBLE);
				} else if ((mLlTwo.getVisibility() == View.GONE)
						&& (mLlFirst.getVisibility() == View.VISIBLE)) {
					clearTempFromPref();
					finish();
				}

			}
		});

		mTvPigType = (TextView) findViewById(R.id.tvPigType);
		mTvPigType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						NewVehicleActivity.this);

				mSingleChoiceID = 0;
				builder.setTitle("请选择");
				builder.setSingleChoiceItems(mItems, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mSingleChoiceID = whichButton;

							}
						});
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mTvPigType.setText(mItems[mSingleChoiceID]);
							}
						});
				builder.setNegativeButton("取消", null);
				builder.create().show();

			}
		});
		mEtJuli = (EditText) findViewById(R.id.etjuli);
	}

	private void sendData() {
		showDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("e.type", et_chexing.getText().toString());
		String startTime = "";
		if (TextUtils.isEmpty(et_startdate.getText().toString())) {
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			startTime = sDateFormat.format(new Date());
		} else {
			startTime = et_startdate.getText().toString();
		}
		params.addBodyParameter("e.startTime", startTime);
		String endTime = "";
		if (TextUtils.isEmpty(et_enddate.getText().toString())
				|| et_enddate.getText().toString().equals("不限")) {

			endTime = "";
		} else {
			endTime = et_enddate.getText().toString();
		}

		if (url.equals(UrlEntry.UPDATE_VEHIVLE_BYID_URL)) {
			params.addBodyParameter("e.id", id);
		}
		params.addBodyParameter("e.endTime", endTime);
		params.addBodyParameter("e.carNum", et_carnumber.getText().toString());
		params.addBodyParameter("e.capacity", et_count.getText().toString());
		params.addBodyParameter("e.linkman", et_phonePeople.getText()
				.toString());
		params.addBodyParameter("e.price", et_price.getText().toString());
		params.addBodyParameter("e.phone", et_phone.getText().toString());
		params.addBodyParameter("e.province", province);
		params.addBodyParameter("e.city", city);
		params.addBodyParameter("e.area", area);
		params.addBodyParameter("e.createAccount", App.username);
		params.addBodyParameter("e.remark", et_remark.getText().toString());
		params.addBodyParameter("uuid", App.uuid);
		params.addBodyParameter("e.pigType", mTvPigType.getText().toString());
		params.addBodyParameter("e.carRadius", mEtJuli.getText().toString());

		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, url, params,
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

						Log.i("login", " login responseInfo.result = "
								+ responseInfo.result);

						JSONObject jsonresult;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.get("success").toString()
									.equals("1")) {
								// showToast("发布成功！");
								if (url.equals(UrlEntry.ADD_VEHIVLE_BYID_URL)) {
									id = jsonresult.get("id").toString();
								}
								System.out.println("id：" + id);

								// 上传图片
								if (mDataList.size() > 0) {
									showDialog();
									new Thread(runnable).start();
								} else {
									clearTempFromPref();
									showToast("发布成功！");
									setResult(27);
									finish();
								}
							} else if (jsonresult.get("success").toString()
									.equals("0")) {
								clearTempFromPref();
								showToast("用户信息错误,请重新登录！");
								relogin(NewVehicleActivity.this);
							} else if (jsonresult.get("success").toString()
									.equals("3")) {
								showToast("发布失败！");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						showToast("新增失败,请稍后再试！");
						disDialog();

					}
				});
	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			Message msg = new Message();
			Bundle data = new Bundle();
			String result = "";
			for (int i = 0; i < mDataList.size(); i++) {
				HttpUtil http = new HttpUtil();
				result = http.postFileMethod(new File(
						mDataList.get(i).sourcePath),
						UrlEntry.UPLOAD_VEHIVLEFILE_URL, App.uuid, id, String
								.valueOf((i + 1)));

			}

			data.putString("value", result);
			msg.setData(data);
			msg.what = 3;

			handler.sendMessage(msg);
		}
	};

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				notifyDataChanged();
				break;
			case 2:
				Bundle data = msg.getData();
				String val = data.getString("value");
				Log.i("mylog", "请求结果为-->" + val);
				break;
			case 3:
				disDialog();
				clearTempFromPref();
				Bundle data1 = msg.getData();
				String result = data1.getString("value");
				JSONObject jsonresult;
				if (!result.equals("")) {
					try {
						jsonresult = new JSONObject(result);
						if (jsonresult.get("success").toString().equals("1")) {
							showToast("发布成功！");
							// setResult(27);
							finish();
						} else if (jsonresult.get("success").toString()
								.equals("0")) {

						} else if (jsonresult.get("success").toString()
								.equals("3")) {
							showToast("发布失败");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log.i("aaaa",  " , "
				+ requestCode);
		switch (requestCode) {
		case CustomConstants.TAKE_PICTURE:
			System.out.println("path" + path);
			if (mDataList.size() < CustomConstants.MAX_IMAGE_SIZE
					&& resultCode == -1 && !TextUtils.isEmpty(path)) {

				mDataList.add(FileUtil.saveDataPic(path));
				saveTempToPref(CustomConstants.PREF_TEMP_IMAGES, mDataList);
			}

			break;

		case CustomConstants.TAKE_PICTURE1:
			if(resultCode!=-1){
				return;
			}
			if (data == null) {
				return;
			}
			path = getPath(getFile());
			startPhotoZoom(data.getData(), path,
					CustomConstants.CORP_TAKE_PICTURE1);
			break;

		case CustomConstants.CORP_TAKE_PICTURE1:
			if(resultCode!=-1){
				return;
			}
			mDataList = setPicToView(data, path, mDataList, 1);
			saveTempToPref(CustomConstants.PREF_TEMP_IMAGES, mDataList);
			// initView();
			break;
		default:
			break;
		}

	}

	public class TimePopupWindows extends PopupWindow {

		@SuppressWarnings("deprecation")
		public TimePopupWindows(Context mContext, View parent, final String type) {

			View view = View.inflate(mContext, R.layout.time_dialog, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			TextView exittv = (TextView) view.findViewById(R.id.exit);
			exittv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();

				}
			});
			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.WRAP_CONTENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);

			update();

			ListView mListviewPro = (ListView) view
					.findViewById(R.id.lv_list_date);
			gettimedata(type);
			SimpleAdapter adapter = new SimpleAdapter(NewVehicleActivity.this,
					data, R.layout.item_date, new String[] { "time" },
					new int[] { R.id.text1 });
			mListviewPro.setAdapter(adapter);
			mListviewPro.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					@SuppressWarnings("unchecked")
					HashMap<String, String> maplist = (HashMap<String, String>) parent
							.getItemAtPosition(position);
					System.out.println(maplist.get("time"));
					if (type.equals("start")) {

						if (!TextUtils.isEmpty(et_enddate.getText().toString())) {
							int result = et_enddate.getText().toString()
									.compareTo(maplist.get("time"));
							if (result < 0) {
								showToast("开始日期不能小于结束时间,请重新选择！");
								return;
							}
						}
						et_startdate.setText(maplist.get("time"));

					} else {
						if (!TextUtils.isEmpty(et_startdate.getText()
								.toString())) {
							int result = et_startdate.getText().toString()
									.compareTo(maplist.get("time"));
							if (result > 0) {
								showToast("结束日期不能小于开始时间,请重新选择！");
								return;
							}
						}

						et_enddate.setText(maplist.get("time"));
					}

					dismiss();
				}
			});

		}
	}

	private void gettimedata(String type) {
		data.clear();
		if (type.equals("end")) {
			HashMap<String, String> maplist = new HashMap<String, String>();
			maplist.put("time", "不限");
			data.add(maplist);
		}
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = sDateFormat.format(new Date());
		HashMap<String, String> maplist = new HashMap<String, String>();
		maplist.put("time", date1);
		data.add(maplist);
		for (int i = 1; i < 30; i++) {
			HashMap<String, String> maplist1 = new HashMap<String, String>();
			Date date;
			try {
				date = (new SimpleDateFormat("yyyy-MM-dd")).parse(date1);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.DATE, 1);

				maplist1.put("time", (new SimpleDateFormat("yyyy-MM-dd"))
						.format(cal.getTime()));
				date1 = (new SimpleDateFormat("yyyy-MM-dd")).format(cal
						.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			data.add(maplist1);
		}
	}

	protected void onResume() {
		super.onResume();
		notifyDataChanged(); // 当在ImageZoomActivity中删除图片时，返回这里需要刷新
	}

	private int getDataSize() {
		return mDataList == null ? 0 : mDataList.size();
	}

	protected void onRestart() {

		super.onRestart();
	}

	class OnClickLintener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.item_popupwindows_camera: // 相机
				mFile = getFile();
				path = getPath(mFile);
				takePhoto(TAKE_PHOTO, mFile);
				mPwDialog.dismiss();
				break;
			case R.id.item_popupwindows_Photo: // 相册

				// Intent intent = new Intent(NewInfoActivity.this,
				// TestPicActivity.class);
				// intent.putExtra(CustomConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
				// getAvailableSize());
				// startActivity(intent);

				takeAlbum(TAKE_PICTURE);
				mPwDialog.dismiss();
				break;
			case R.id.item_popupwindows_cancel:// 取消
				mPwDialog.dismiss();
				break;
			default:
				break;
			}

		}

	}

	@SuppressWarnings("unused")
	private int getAvailableSize() {
		int availSize = CustomConstants.MAX_IMAGE_SIZE - mDataList.size();
		if (availSize >= 0) {
			return availSize;
		}
		return 0;
	}

	private void showPopDialog(View parent) {

		// 自定义的单击事件
		OnClickLintener paramOnClickListener = new OnClickLintener();
		mPwDialog = new PopupWindows(NewVehicleActivity.this,
				paramOnClickListener, parent);
		mPwDialog.update();
	}

	protected void onPause() {
		super.onPause();
		saveTempToPref(CustomConstants.PREF_TEMP_IMAGES, mDataList);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	
		 outState.putString("pigtype", mTvPigType.getText().toString());
		outState.putString("url", url);
		outState.putString("id", id);
		outState.putString("uuid", App.uuid);
		super.onSaveInstanceState(outState);
		saveTempToPref(CustomConstants.PREF_TEMP_IMAGES, mDataList);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null) {
			path = savedInstanceState.getString("path");
			
			mTvPigType.setText(savedInstanceState.getString("pigtype"));
			url = savedInstanceState.getString("url");
			id = savedInstanceState.getString("id");
			App.uuid = savedInstanceState.getString("uuid");
			initView();
		}
	}

	private void notifyDataChanged() {
		mAdapter.notifyDataSetChanged();
	}

	private void setViewData(Vehicle v) {
		id = v.getId();
		et_chexing.setText(v.getType());
		et_count.setText(v.getCapacity());
		et_carnumber.setText(v.getCarNum());
		et_phonePeople.setText(v.getLinkman());
		et_phone.setText(v.getPhone());
		et_startdate.setText(v.getStartTime());
		et_enddate.setText(v.getEndTime());
		et_remark.setText(v.getRemark());
		et_price.setText(v.getPrice());
		mEtJuli.setText(v.getCarRadius());
		mTvPigType.setText(v.getPigType());
		mTvTishi.setVisibility(View.VISIBLE);
	}

}
