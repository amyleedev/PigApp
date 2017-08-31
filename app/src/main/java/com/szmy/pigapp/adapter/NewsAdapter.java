package com.szmy.pigapp.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.entity.NewsEntity;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.UrlEntry;

import java.util.List;

public class NewsAdapter extends BaseAdapter {
	private List<NewsEntity> list;
	private LayoutInflater mInflater;
	private Context ctx;

	public NewsAdapter(Context ctx, List<NewsEntity> list) {
		this.ctx = ctx;
		mInflater = LayoutInflater.from(ctx);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public NewsEntity getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_news, null);
			new ViewHolder(convertView);
		}
		final ViewHolder holder = (ViewHolder) convertView.getTag();
		final NewsEntity entry = getItem(arg0);
		holder.tvName.setText(entry.getTitle());
//		if (entry.getType().equals("price")) {
////			holder.tvName.setText("[报 价]   " + entry.getTitle());
//
//			holder.tvName.setText(
//					Html.fromHtml("<font color = '#ff0000'>[报 价]      </font>"+ entry.getTitle().toString()));
//		} else {
////			holder.tvName.setText("[新 闻]   " + entry.getTitle());
//			holder.tvName.setText(
//					Html.fromHtml("<font color = '#ff0000'>[新 闻]      </font>"+ entry.getTitle().toString()));
//		}
		holder.tvTime.setText(entry.getCreatetime());
		holder.news_hits.setText(entry.getHit().toString());
		if (entry.getType().equals("price")) {
			if (!TextUtils.isEmpty(entry.getPicture())) {
				// 价格
				com.nostra13.universalimageloader.core.ImageLoader
						.getInstance().displayImage(
								UrlEntry.ip + entry.getPicture(),
								holder.news_image, AppStaticUtil.getOptions());
			} else {
				// 图片为空
				holder.news_image.setImageResource(R.drawable.default_offer);
			}
		} else {
			// 新闻
			if (!TextUtils.isEmpty(entry.getPicture())) {
				com.nostra13.universalimageloader.core.ImageLoader
						.getInstance().displayImage(
								UrlEntry.ip + entry.getPicture(),
								holder.news_image, AppStaticUtil.getOptions());
			} else {
				// 图片为空
				holder.news_image.setImageResource(R.drawable.default_news);
			}
		}
		return convertView;
	}

	private class ViewHolder {
		TextView tvName;
		TextView tvTime;
		ImageView news_image;
		TextView news_hits;//浏览量

		public ViewHolder(View view) {
			tvName = (TextView) view.findViewById(R.id.news_name_text);
			tvTime = (TextView) view.findViewById(R.id.news_time_text);
			news_image = (ImageView) view.findViewById(R.id.news_image);
			news_hits = (TextView) view.findViewById(R.id.textView2);
			view.setTag(this);
		}
	}
}
