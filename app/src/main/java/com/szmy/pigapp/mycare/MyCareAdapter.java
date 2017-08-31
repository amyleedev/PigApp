package com.szmy.pigapp.mycare;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gc.materialdesign.widgets.Dialog;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.szmy.pigapp.R;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.HttpUtil;
import com.szmy.pigapp.utils.UrlEntry;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class MyCareAdapter extends BaseAdapter {
	private List<MyCareEntry> list;
	private LayoutInflater mInflater;
	private Context ctx;
	private BitmapUtils bitmapUtils;
	private Handler handler;

	public MyCareAdapter(Context ctx, List<MyCareEntry> list, Handler handler) {
		this.ctx = ctx;
		mInflater = LayoutInflater.from(ctx);
		this.list = list;
		this.handler = handler;
		bitmapUtils = new BitmapUtils(ctx);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public MyCareEntry getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_mycare, null);
			new ViewHolder(convertView);
		}
		final ViewHolder holder = (ViewHolder) convertView.getTag();
		final MyCareEntry entry = getItem(arg0);
		String name = entry.getCompname();
		holder.tvName.setText(name);
		holder.tvType.setText(getTypeName(entry.getType()));
		holder.tvUsername.setText(entry.getNickname());
		String str = entry.getMobile();
		if (str.length() >= 11) {
			str = str.substring(0, 11);
			holder.tvPhone.setText(str.substring(0,
					str.length() - (str.substring(3)).length())
					+ "****" + str.substring(7));
		} else {
			holder.tvPhone.setText(str.substring(0, str.length()) + "***");
		}
		if (entry.getPicture().toString().equals("")) {
			holder.imgHead.setImageResource(R.drawable.default_title);
		} else {
			try {
				bitmapUtils
						.configDefaultLoadFailedImage(R.drawable.default_title);
				bitmapUtils.display(holder.imgHead, UrlEntry.ip
						+ entry.getPicture().toString());// 下载并显示图片
			} catch (Exception ex) {
				holder.imgHead.setImageResource(R.drawable.default_title);
			}
		}
		holder.tvAddress.setText(entry.getProvince() + entry.getCity()
				+ entry.getArea() + "******");
		holder.tvCancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				final Dialog dialog = new Dialog(ctx, "提示", "确定取消关注该发布者？");
				dialog.addCancelButton("取消");
				dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.cancel();
// 取消关注
						MyRunnable runnable = new MyRunnable(entry.getFriendid(), entry
								.getId());
						new Thread(runnable).start();
					}
				});
				dialog.setOnCancelButtonClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.cancel();
					}
				});
				dialog.show();
//				AlertDialog.Builder builder = new Builder(
//						ctx);
//				builder.setMessage("确定取消关注该发布者？");
//				builder.setTitle("提示");
//				builder.setPositiveButton("确定",
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,
//												int which) {
//								// 取消关注
//								MyRunnable runnable = new MyRunnable(entry.getFriendid(), entry
//										.getId());
//								new Thread(runnable).start();
//							}
//						});
//				builder.setNeutralButton("取消", null);
//				builder.create().show();
			}
		});
		return convertView;
	}

	private String getTypeName(String type){
		String str = "";
		if(type.equals("")){
			return str;
		}
		switch(Integer.parseInt(type)){
			case 1:
				str = "猪场";
				break;
			case 2:
				str = "屠宰场";
				break;
			case 3:
				str = "经纪人";
				break;
			case 6:
				str = "防疫员";
				break;
			case 7:
				str = "检疫员";
				break;
			case 8:
				str = "监督员";
				break;
		}
		return str;
	}
	private class ViewHolder {
		TextView tvName;// 企业名称
		ImageView imgHead;;// 头像
		TextView tvCancle;// 取消关注
		TextView tvUsername;// 用户姓名
		TextView tvPhone;// 用户电话
		TextView tvAddress;// 地址
		TextView tvType;// 角色类型

		public ViewHolder(View view) {
			tvName = (TextView) view.findViewById(R.id.tv_name);
			imgHead = (ImageView) view.findViewById(R.id.imagehead);
			tvCancle = (TextView) view.findViewById(R.id.canclebtn);
			tvUsername = (TextView) view.findViewById(R.id.tv_username);
			tvPhone = (TextView) view.findViewById(R.id.tv_phone);
			tvAddress = (TextView) view.findViewById(R.id.tv_address);
			tvType = (TextView) view.findViewById(R.id.tv_usertype);
			view.setTag(this);
		}
	}

	public class MyRunnable implements Runnable {
		private String friendid;
		private String id;

		public MyRunnable(String friendid, String id) {
			this.friendid = friendid;
			this.id = id;
		}

		@Override
		public void run() {
			String result = "";
			Bundle data = new Bundle();
			Message msg = new Message();
			MultipartEntity params = new MultipartEntity();
			try {
				params.addPart("friendid", new StringBody(friendid));
				params.addPart("uuid", new StringBody(App.uuid));
				HttpUtil http = new HttpUtil();
				result = http
						.postDataMethod(UrlEntry.DELETE_MYCARE_URL, params);
				data.putString("value", result);
				msg.setData(data);
				handler.sendMessage(msg);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
