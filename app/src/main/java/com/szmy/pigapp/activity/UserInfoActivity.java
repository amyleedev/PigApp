package com.szmy.pigapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.widgets.Dialog;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.image.CustomConstants;
import com.szmy.pigapp.image.ImageDisplayer;
import com.szmy.pigapp.image.ImageItem;
import com.szmy.pigapp.image.PopupWindows;
import com.szmy.pigapp.tixian.WithdrawalsListActivity;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.MD5;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.zheshang.NewAgreementsigningActivity;
import com.szmy.pigapp.zheshang.NewElectronicAccountsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserInfoActivity extends BaseActivity {
	private LinearLayout mLlBack;
	private SharedPreferences sp;
	private TextView tv_phone;
	private String newphone;
	private RelativeLayout layout_pwd,layout_zfpwd;
	private ImageView ivPhoto;
	private BitmapUtils bitmapUtils;
	/** 拍照获取相片 **/
	private RelativeLayout address_layout, phone_layout;
	private String path = "";
	private LinearLayout mLlExit;
	private RelativeLayout mRlCardLayout,mRlKaihuLayout,mRlDaishouLayout;
	private PopupWindows mPwDialog;
	private int TAKE_PHOTO;
	private int TAKE_PICTURE;
	private File mFile;
	public List<ImageItem> mDataList1 = new ArrayList<ImageItem>();
	private String imagepath1 = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		sp = getSharedPreferences(App.USERINFO, MODE_PRIVATE);
		if (!TextUtils.isEmpty(sp.getString("userinfo", ""))) {
			App.mUserInfo = FileUtil.readUser(UserInfoActivity.this);
			App.setDataInfo(App.mUserInfo);
		}
		initView();
	}

	private void initView() {
		((TextView) findViewById(R.id.tv_username)).setText(App.username);
		mLlBack = (LinearLayout) findViewById(R.id.def_head_back);
		mLlBack.setOnClickListener(mClickListener);
		bitmapUtils = new BitmapUtils(this);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_phone.setText(App.userphone);
		ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
		ivPhoto.setOnClickListener(mClickListener);
		phone_layout = (RelativeLayout) findViewById(R.id.phone_layout);
		phone_layout.setOnClickListener(mClickListener);
		layout_pwd = (RelativeLayout) findViewById(R.id.layout_pwd);
		layout_pwd.setOnClickListener(mClickListener);
		layout_zfpwd = (RelativeLayout) findViewById(R.id.layout_zfpwd);
		layout_zfpwd.setOnClickListener(mClickListener);
		address_layout = (RelativeLayout) findViewById(R.id.address_layout);
		address_layout.setOnClickListener(mClickListener);
		mLlExit = (LinearLayout) findViewById(R.id.btn_exit_info);
		mLlExit.setOnClickListener(mClickListener);
		mRlCardLayout = (RelativeLayout) findViewById(R.id.card_layout);
		mRlKaihuLayout = (RelativeLayout) findViewById(R.id.kaihu_layout);
		mRlDaishouLayout = (RelativeLayout) findViewById(R.id.daishou_layout);
		// mRlCardLayout.setVisibility(View.GONE);
		mRlCardLayout.setOnClickListener(mClickListener);
		mRlKaihuLayout.setOnClickListener(mClickListener);
		mRlDaishouLayout.setOnClickListener(mClickListener);
		if (App.userphoto.equals("")) {
			// ivPhoto.setImageResource(R.drawable.def);
			Bitmap def = BitmapFactory.decodeResource(getResources(),
					R.drawable.def);
			if (def != null) {
				ivPhoto.setImageBitmap(def);
			}
		} else {
			bitmapUtils.configDefaultLoadFailedImage(R.drawable.def);
			bitmapUtils.display(ivPhoto, UrlEntry.ip + App.userphoto);// 下载并显示图片
		}
		mDataList1 = getTempFromPref(CustomConstants.PREF_TEMP_IMAGES1);
		initImage();
	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_exit_info:// 退出登录
				exit();
				break;
			case R.id.def_head_back:// 返回并更新个人信息
				clearTempFromPref();
				setResult(22);
				finish();
				break;
			case R.id.ivPhoto:// 上传头像对话框
				TAKE_PHOTO = CustomConstants.TAKE_PHOTO;
				TAKE_PICTURE = CustomConstants.TAKE_PICTURE1;
				showPopDialog(ivPhoto);
				break;
			case R.id.layout_pwd:// 更新密码
				updatePassword();
				break;
			case R.id.layout_zfpwd:
				updateZfPwd();
				break;
			case R.id.address_layout:// 更改收货地址
				startActivity(new Intent(UserInfoActivity.this, MyAddress.class));
				break;
			case R.id.phone_layout:// 更改手机号
				updatePhoneNumber();
				break;
			case R.id.card_layout:// 更改银行卡
				startActivity(new Intent(UserInfoActivity.this,
						WithdrawalsListActivity.class));
				break;
			case R.id.kaihu_layout:
					Intent intent = new Intent(UserInfoActivity.this, NewElectronicAccountsActivity.class);
					startActivity(intent);
				break;
			case R.id.daishou_layout:
					intent = new Intent(UserInfoActivity.this, NewAgreementsigningActivity.class);
					startActivity(intent);
				break;
			}
		}
	};

	private void updatePhoneNumber() {
		final EditText et = new EditText(UserInfoActivity.this);
		AlertDialog dialog = new AlertDialog.Builder(UserInfoActivity.this)
				.setTitle("请输入要修改的电话号码")
				.setIcon(android.R.drawable.ic_dialog_info).setView(et)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						newphone = et.getText().toString().trim();
						if (newphone.length() < 1) {
							showToast("请输入有效号码！");
						} else
							sendData("phone", newphone, "");

					}
				}).setNegativeButton("取消", null).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	private void updatePassword() {
		Intent intent = new Intent(UserInfoActivity.this,
				UpdatePwdActivity.class);
		startActivity(intent);
	}
	private void updateZfPwd() {
		Intent intent = new Intent(UserInfoActivity.this,
				ActivitySetPayPassword.class);
		startActivity(intent);
	}
	private void exit() {
		final Dialog dialog = new Dialog(UserInfoActivity.this, "提示", "确定退出程序？");
		dialog.addCancelButton("取消");
		dialog.setOnAcceptButtonClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
				try{
					editor.putString("userinfo", "");
					editor.commit();
					App.clearData();
					SharedPreferences  leveSp = getSharedPreferences("leve", 0);
					SharedPreferences.Editor editor1 = leveSp.edit();
					editor1.putString("result","");
					editor1.commit();
					setResult(22);
					finish();
				}catch (Exception e){
					showToast("退出程序失败，请重新操作！");
				}


			}
		});
		dialog.setOnCancelButtonClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		dialog.show();

	}

	@SuppressWarnings("static-access")
	private void sendData(final String type, String str, String str1) {
		showDialog();
		RequestParams params = new RequestParams();
		if (type.equals("phone")) {
			params.addBodyParameter("e.phone", str);
		} else {
			params.addBodyParameter("e.password", (new MD5()).GetMD5Code(str));
			params.addBodyParameter("newPassword", (new MD5()).GetMD5Code(str1));
		}
		params.addBodyParameter("uuid", App.uuid);

		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.UPDATE_USERINFO_URL,
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

						Log.i("login", " login responseInfo.result = "
								+ responseInfo.result);

						JSONObject jsonresult;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.get("success").toString()
									.equals("1")) {
								showToast("修改成功！");
								if (type.equals("phone")) {
									tv_phone.setText(newphone);
									App.userphone = newphone;
									// editor.putString("phone", newphone);
									// editor.commit();
									App.mUserInfo.setPhone(newphone);
									FileUtil.saveUser(UserInfoActivity.this,
											App.mUserInfo);
								}
							} else {
								successType(jsonresult.get("success")
										.toString(), "修改失败！");
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode == RESULT_OK) {
			switch (requestCode) {		
			case CustomConstants.TAKE_PHOTO:
				System.out.println("path" + path);
				if (resultCode == -1 && !TextUtils.isEmpty(path)) {
					mDataList1.clear();
					mDataList1.add(FileUtil.saveDataPic(path));
				}

				saveTempToPref(CustomConstants.PREF_TEMP_IMAGES1, mDataList1);
				initImage();
				upHeadImage();
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
				mDataList1 = setPicToView(data, path, mDataList1,0);

				saveTempToPref(CustomConstants.PREF_TEMP_IMAGES1, mDataList1);
				initView();
				upHeadImage();
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	

	public void upHeadImage() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		params.addBodyParameter("uploadImage", new File(imagepath1));
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.UPLOAD_USERINFO_URL,
				params, new RequestCallBack<String>() {

					@Override
					public void onStart() {
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						// resultText.setText(current + "/" + total);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						Log.i("aaaa", " head  responseInfo.result = "
								+ responseInfo.result);
						getDTOArrayUp(responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("aaaa", " head  msg = " + msg);
					}
				});
	}

	public void getDTOArrayUp(String jsonString) {
		try {
			JSONObject jobj = new JSONObject(jsonString);
			if (jobj != null && jobj.getString("success").equals("1")) {
				clearTempFromPref();
				// 保存头像信息
				App.mUserInfo.setPicture(jobj.getString("picture"));
				FileUtil.saveUser(UserInfoActivity.this, App.mUserInfo);
				
				App.userphoto = jobj.getString("picture");
			} else {
				Toast.makeText(UserInfoActivity.this, "上传失败！", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initImage() {
		if (isShowAddItem(mDataList1, 0)) {

		} else {
			final ImageItem item = mDataList1.get(0);
			ImageDisplayer.getInstance(this).displayBmp(ivPhoto,
					item.thumbnailPath, item.sourcePath);
			imagepath1 = item.sourcePath;
		}

	}

	protected void onPause() {
		super.onPause();
		saveTempToPref(CustomConstants.PREF_TEMP_IMAGES1, mDataList1);

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("path", path);

		super.onSaveInstanceState(outState);
		saveTempToPref(CustomConstants.PREF_TEMP_IMAGES1, mDataList1);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null) {
			path = savedInstanceState.getString("path");

			initView();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 返回这里需要刷新
		initView();
	}

	/****************************************************************/
	class OnClickLintener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.item_popupwindows_camera: // 相机

				if (TAKE_PHOTO == CustomConstants.TAKE_PHOTO) {
					mFile = getFile();
					path = getPath(mFile);
				}
				takePhoto(TAKE_PHOTO, mFile);
				mPwDialog.dismiss();
				break;
			case R.id.item_popupwindows_Photo: // 相册

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

	private void showPopDialog(View parent) {

		// 自定义的单击事件
		OnClickLintener paramOnClickListener = new OnClickLintener();
		mPwDialog = new PopupWindows(UserInfoActivity.this,
				paramOnClickListener, parent);
		// 如果窗口存在，则更新
		mPwDialog.update();
	}

}
