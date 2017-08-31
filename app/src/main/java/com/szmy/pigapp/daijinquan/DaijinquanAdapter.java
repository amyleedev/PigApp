package com.szmy.pigapp.daijinquan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.ActivityScanResult;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.UrlEntry;

import java.util.List;

public class DaijinquanAdapter extends BaseAdapter {
	private List<Daijinquan> list;
	private LayoutInflater mInflater;
	private Context ctx;

	public DaijinquanAdapter(Context ctx, List<Daijinquan> list) {
		this.ctx = ctx;
		mInflater = LayoutInflater.from(ctx);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Daijinquan getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_daijinquan, null);
			new ViewHolder(convertView);
		}

		final ViewHolder holder = (ViewHolder) convertView.getTag();
		final Daijinquan entry = getItem(arg0);

//		holder.name.setText(entry.getAmount());
		setTextSize(holder.name,entry.getAmount()+"元");
		 holder.remark.setText("* "+entry.getRemark());
		holder.type.setText("* 仅限"+entry.getOrientationName()+"使用");
		if (TextUtils.isEmpty(entry.getEndTime())){
			holder.time.setText("有效期至: 永久有效");
		}else
		holder.time.setText("有效期至: "+entry.getEndTime().substring(0,10));
		setStatus(holder.status,entry.getStatus());
		if (entry.getStatus().equals("0")){
			holder.status.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {


					Intent intent = new Intent(ctx, ActivityScanResult.class);
					intent.putExtra("type", "index");
					intent.putExtra(
							"url",
							UrlEntry.YOUXUAN_SZMY_URL
									+ (TextUtils
									.isEmpty(getUserUUid(ctx)) ? "1"
									: getUserUUid(ctx)));
					ctx.startActivity(intent);
				}
			});

		}
		return convertView;
	}
	private void setTextSize(TextView tvText,String str){

		int end = str.length();
		SpannableString textSpan = new SpannableString (str);
		textSpan.setSpan(new AbsoluteSizeSpan(50),0,end-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		textSpan.setSpan(new ForegroundColorSpan(0xFFFF0000),0,end-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //
		textSpan.setSpan(new AbsoluteSizeSpan(30),end-1,end,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		textSpan.setSpan(new ForegroundColorSpan(0xFFFF0000),end-1,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		tvText.setText(textSpan);
	}
	private String setType(String type){
		String str = "";
		if(type.equals("myyx")){
			str = "牧易优选";
		}
		return str;
	}
	private void setStatus(TextView view,String status){
		//0:未使用，1己下单未支付，2已支付
		if(status.equals("0")){
//			view.setText(Html.fromHtml("<font color = '#ffff4444'>立即\n使用</font>"));
		}else if(status.equals("2")||status.equals("1")){
			view.setText(Html.fromHtml("<font color = '#aaaaaa'>已使用</font>"));
		}else if(status.equals("3")){
			view.setText(Html.fromHtml("<font color = '#aaaaaa'>已过期</font>"));
		}
	}
	public String getUserUUid(Context context) {
		SharedPreferences sp = context.getSharedPreferences("USERINFO", 0);
		if (App.mUserInfo == null) {
			App.mUserInfo = FileUtil.readUser(context);
		}
		App.setDataInfo(App.mUserInfo);
		if (!TextUtils.isEmpty(App.uuid)) {
			return App.uuid;
		}
		return "";
	}
	private class ViewHolder {

		TextView name;
		TextView remark;
		TextView type;
		TextView time;
		TextView status;



		public ViewHolder(View view) {
			name = (TextView) view.findViewById(R.id.tv_money);
			remark = (TextView) view.findViewById(R.id.daijinquan_remark);
			type = (TextView) view.findViewById(R.id.daijinquan_type);
			time = (TextView) view.findViewById(R.id.tv_endtime);
			status = (TextView) view.findViewById(R.id.tv_status);
			view.setTag(this);
		}
	}
}
