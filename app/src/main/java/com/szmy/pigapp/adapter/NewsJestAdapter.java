package com.szmy.pigapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.entity.Jest;

import java.net.URL;
import java.util.List;

public class NewsJestAdapter extends BaseAdapter {
	private List<Jest> list;
	private LayoutInflater mInflater;

	public NewsJestAdapter(Context ctx, List<Jest> list) {
		mInflater = LayoutInflater.from(ctx);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Jest getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_news_jest, null);
			new ViewHolder(convertView);
		}
		final ViewHolder holder = (ViewHolder) convertView.getTag();
		final Jest entry = getItem(arg0);

		holder.tvTitle.setText(Html.fromHtml(entry.getText(),imageGetter,null));

//		holder.tvContent.getSettings().setDefaultTextEncodingName("UTF-8") ;
//		holder.tvContent.setBackgroundColor(0);
//		holder.tvContent.loadDataWithBaseURL(null,entry.getText().toString().trim(), "text/html", "UTF-8",null) ;
		String ct = entry.getCt();
		if (ct.indexOf(":") != -1)
			ct = ct.substring(0, ct.lastIndexOf(":"));
		holder.tvTime.setText(ct);
		return convertView;
	}
	final Html.ImageGetter imageGetter = new Html.ImageGetter() {

		public Drawable getDrawable(String source) {
			Drawable drawable=null;
			URL url;
			try {
				url = new URL(source);
				drawable = Drawable.createFromStream(url.openStream(), "");
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			return drawable;
		};
	};
	private class ViewHolder {
		TextView tvTitle;
		WebView tvContent;
		TextView tvTime;

		public ViewHolder(View view) {
			tvTitle = (TextView) view.findViewById(R.id.tv_title);
			tvContent = (WebView) view.findViewById(R.id.tv_content);
			tvTime = (TextView) view.findViewById(R.id.tv_time);
			view.setTag(this);
		}
	}
}
