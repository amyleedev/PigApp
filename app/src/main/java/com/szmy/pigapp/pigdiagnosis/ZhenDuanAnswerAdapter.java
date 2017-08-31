package com.szmy.pigapp.pigdiagnosis;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.ActivityScanResult;
import com.szmy.pigapp.utils.UrlEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZhenDuanAnswerAdapter extends BaseAdapter {
	private List<ZhenDuan> list;
	private LayoutInflater mInflater;
	private Context ctx;
	private Map<Integer,String> maplist;

	public ZhenDuanAnswerAdapter(Context ctx, List<ZhenDuan> list) {
		this.ctx = ctx;
		mInflater = LayoutInflater.from(ctx);
		this.list = list;
		getNum();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public ZhenDuan getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_zbzd_answer_list, null);
			new ViewHolder(convertView);
		}
		final ViewHolder holder = (ViewHolder) convertView.getTag();
		final ZhenDuan entry = getItem(arg0);
		String str = "";
		if(arg0<10){
			str ="疾病"+ maplist.get(arg0+1)+":";
		}

		holder.tvName.setText(Html.fromHtml("<font color=red>"+str+"</font>"+entry.getName()));
		holder.tvContent.setText(entry.getDiscription());
		holder.tvInfoBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(ctx, ActivityScanResult.class);
				intent.putExtra("url", UrlEntry.GET_ANSWER_INFO_URL+entry.getId());
				intent.putExtra("type","zhenduan");
				ctx.startActivity(intent);
			}
		});
		return convertView;
	}

	private void getNum(){
		 maplist = new HashMap<>();
		maplist.put(1,"一");
		maplist.put(2,"二");
		maplist.put(3,"三");
		maplist.put(4,"四");
		maplist.put(5,"五");
		maplist.put(6,"六");
		maplist.put(7,"七");
		maplist.put(8,"八");
		maplist.put(9,"九");
		maplist.put(10,"十");
	}
	private class ViewHolder {
		TextView tvName;
		TextView tvContent;
		TextView tvInfoBtn;

		public ViewHolder(View view) {
			tvName = (TextView) view.findViewById(R.id.name);
			tvContent = (TextView) view.findViewById(R.id.info_content);
			tvInfoBtn = (TextView) view.findViewById(R.id.info_btn);

			view.setTag(this);
		}
	}
}
