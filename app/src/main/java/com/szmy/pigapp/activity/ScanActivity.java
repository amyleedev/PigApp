package com.szmy.pigapp.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.materialdesign.widgets.Dialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.zxing.activity.CaptureActivity;

import org.json.JSONObject;

import java.util.List;

public class ScanActivity extends BaseActivity {
	private LinearLayout mLlFodderScan;
	private LinearLayout mLlVeterinaryDrugScan;
	private LinearLayout mLlVaccineScan;
	private LinearLayout mLlAnimalProducts;
	private LinearLayout mLlOtherScan;
	private LinearLayout mLlYdZfScan;
	private TextView mTvTel;
	private TextView mTvTelPhone;
	private String city = "";
	private String area = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);
		city = getIntent().getExtras().getString("city");
		mTvTel = (TextView) findViewById(R.id.tv_tel);
		mTvTelPhone = (TextView) findViewById(R.id.tv_telphone);
		mTvTelPhone.setOnClickListener(mClickListener);
		city = getIntent().getStringExtra("city");
		area = getIntent().getStringExtra("area");
		if (TextUtils.isEmpty(city)) {
			mTvTel.setText("河南省畜牧局监督电话：");
		} else {
			mTvTel.setText(city+area+"畜牧局监督电话：");

		}
		mLlFodderScan = (LinearLayout) findViewById(R.id.ll_fodder_scan);
		mLlFodderScan.setOnClickListener(mClickListener);
		mLlVeterinaryDrugScan = (LinearLayout) findViewById(R.id.ll_veterinary_drug_scan);
		mLlVeterinaryDrugScan.setOnClickListener(mClickListener);
		mLlVaccineScan = (LinearLayout) findViewById(R.id.ll_vaccine_scan);
		mLlVaccineScan.setOnClickListener(mClickListener);
		mLlAnimalProducts = (LinearLayout) findViewById(R.id.ll_animal_products);
		mLlAnimalProducts.setOnClickListener(mClickListener);
		mLlOtherScan = (LinearLayout) findViewById(R.id.ll_other_scan);
		mLlOtherScan.setOnClickListener(mClickListener);
		mLlYdZfScan = (LinearLayout) findViewById(R.id.ll_ydzf);
		mLlYdZfScan.setOnClickListener(mClickListener);
		getPhone();
	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_fodder_scan:
			case R.id.ll_veterinary_drug_scan:
			case R.id.ll_vaccine_scan:
			case R.id.ll_animal_products:
			case R.id.ll_other_scan:
				// 打开扫描界面扫描条形码或二维码
				Intent openCameraIntent = new Intent(ScanActivity.this,
						CaptureActivity.class);
				startActivityForResult(openCameraIntent, 0);
				break;
			case R.id.ll_ydzf:
				PackageInfo packageInfo = null;
				try {
				packageInfo = getPackageManager().getPackageInfo("com.ais.ydzf.henan.gfsy", 0);
				}
			catch (PackageManager.NameNotFoundException e) {
				packageInfo = null;
			}
				if (packageInfo == null) {
					final Dialog dialog = new Dialog(ScanActivity.this, "提示", "您未安装该应用，是否立即下载安装？");
					dialog.addCancelButton("取消");
					dialog.setOnAcceptButtonClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.cancel();
							Uri uri = Uri.parse("http://cert.agridoor.com.cn/dongjian/doc/hnyddj.apk");
							Intent intent = new Intent(Intent.ACTION_VIEW, uri);
							intent.addCategory("android.intent.category.BROWSABLE");
							startActivity(intent);

						}
					});
					dialog.setOnCancelButtonClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.cancel();


						}
					});
					dialog.show();
				} else {
					openPackage(ScanActivity.this,"com.ais.ydzf.henan.gfsy");
//					// 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
//					Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
//					resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//					resolveIntent.setPackage(packageInfo.packageName);
//
//					// 通过getPackageManager()的queryIntentActivities方法遍历
//					List<ResolveInfo> resolveinfoList = getPackageManager()
//							.queryIntentActivities(resolveIntent, 0);
//
//					ResolveInfo resolveinfo = resolveinfoList.iterator().next();
//					if (resolveinfo != null) {
//						// packagename = 参数packname
//						String packageName = resolveinfo.activityInfo.packageName;
//						// 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
//						String className = resolveinfo.activityInfo.name;
//						// LAUNCHER Intent
//						Intent intent = new Intent(Intent.ACTION_MAIN);
//						intent.addCategory(Intent.CATEGORY_LAUNCHER);
//						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						// 设置ComponentName参数1:packagename参数2:MainActivity路径
//						ComponentName cn = new ComponentName(packageName, className);
//
//						intent.setComponent(cn);
//						startActivity(intent);
//					}
				}


				break;
				case R.id.tv_telphone:
					if (!TextUtils.isEmpty(mTvTelPhone.getText().toString())){
						dialog("确认拨打电话？",mTvTelPhone.getText().toString());
					}
					break;
			}
		}
	};
	/**/


	public static Context getPackageContext(Context context, String packageName) {
		Context pkgContext = null;
		if (context.getPackageName().equals(packageName)) {
			pkgContext = context;
		} else {
			// 创建第三方应用的上下文环境
			try {
				pkgContext = context.createPackageContext(packageName,
						Context.CONTEXT_IGNORE_SECURITY
								| Context.CONTEXT_INCLUDE_CODE);
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}
		}
		return pkgContext;
	}

	public static boolean openPackage(Context context, String packageName) {
		Context pkgContext = getPackageContext(context, packageName);
		Intent intent = getAppOpenIntentByPackageName(context, packageName);
		if (pkgContext != null && intent != null) {
			pkgContext.startActivity(intent);
			return true;
		}
		return false;
	}
	public static Intent getAppOpenIntentByPackageName(Context context,String packageName){
		// MainActivity完整名
		String mainAct = null;
		// 根据包名寻找MainActivity
		PackageManager pkgMag = context.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED|Intent.FLAG_ACTIVITY_NEW_TASK);

		List<ResolveInfo> list = pkgMag.queryIntentActivities(intent,0);
		for (int i = 0; i < list.size(); i++) {
			ResolveInfo info = list.get(i);
			if (info.activityInfo.packageName.equals(packageName)) {
				mainAct = info.activityInfo.name;
				break;
			}
		}
		if (TextUtils.isEmpty(mainAct)) {
			return null;
		}
		intent.setComponent(new ComponentName(packageName, mainAct));
		return intent;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			System.out.println("--------result" + scanResult);
			if (scanResult.startsWith("http://")
					|| scanResult.startsWith("https://")) {
				Intent intent = new Intent(ScanActivity.this,
						ActivityScanResult.class);
				intent.putExtra("url", scanResult);
				startActivity(intent);
			} else {
				Intent intent = new Intent(ScanActivity.this,
						ActivityScanResultText.class);
				intent.putExtra("text", scanResult);
				startActivity(intent);
			}
		}
	}

	/**获取检疫监督所电话**/
	public void getPhone() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("e.city", city);
		params.addBodyParameter("e.area", area);
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.GET_DJSPHONE_URL,
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
						Log.i("result", " head  responseInfo.result = "
								+ responseInfo.result);
						try {
							JSONObject jobj = new JSONObject(responseInfo.result);
							if (jobj != null && jobj.optString("success").equals("1")) {
								mTvTelPhone.setText(jobj.optString("phone") );
							} else {

							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("aaaa", " head  msg = " + msg);
					}
				});
	}

}
