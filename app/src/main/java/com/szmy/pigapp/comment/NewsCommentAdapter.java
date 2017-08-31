package com.szmy.pigapp.comment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.szmy.pigapp.R;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.CircleDisplayer;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class NewsCommentAdapter extends BaseAdapter {
	private List<NewsComment> list;
	private LayoutInflater mInflater;
	private Context ctx;
	private DisplayImageOptions options;
	public NewsCommentAdapter(Context ctx, List<NewsComment> list) {
		this.ctx = ctx;
		mInflater = LayoutInflater.from(ctx);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public NewsComment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_news_comment, null);
			new ViewHolder(convertView);
		}

		final ViewHolder holder = (ViewHolder) convertView.getTag();
		final NewsComment entry = getItem(arg0);

			holder.name.setText(entry.getCreateAccount());


		holder.time.setText(entry.getCreateTime().substring(5,16));
		holder.mTvCommentContent.setText(entry.getContent());
		if(entry.getType().equals("1")){
			holder.comment_zan_count.setVisibility(View.GONE);
			holder.comment_topinglun.setVisibility(View.GONE);
		}else {
//			if (entry.getSfdz().equals("y")){
//				holder.mIvZan.setImageResource(R.drawable.zan_on);
//			}else{
//				holder.mIvZan.setImageResource(R.drawable.zan_off);
//			}
			holder.comment_zan_count.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View view) {
					SendZan(holder,entry);
				}
			});
			holder.comment_topinglun.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(ctx,CommentListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("newsComment", entry);
					intent.putExtras(bundle);
					ctx.startActivity(intent);
				}
			});
			holder.zanCount.setText(entry.getDzsl());
			holder.commentCount.setText(entry.getPlsl());
		}

		if (!TextUtils.isEmpty(entry.getPicture())) {
//			com.nostra13.universalimageloader.core.ImageLoader.getInstance()
//					.displayImage(UrlEntry.ip + entry.getPicture(),
//							holder.picture, AppStaticUtil.getOptions());
			options =new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.def)
					.showImageOnFail(R.drawable.def)
					.cacheInMemory(true)
					.cacheOnDisc(true)
					.imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.bitmapConfig(Bitmap.Config.ARGB_8888)  //设置图片的解码类型
					.displayer(new CircleDisplayer())
					.build();
			ImageLoader.getInstance().displayImage(UrlEntry.ip + entry.getPicture(), holder.picture, options);
		} else {
			holder.picture.setImageResource(R.drawable.def);
		}
		return convertView;
	}

	private void SendZan(final ViewHolder view,NewsComment entry){
		RequestParams params = new RequestParams();
		params.addBodyParameter("e.cid", entry.getId());
		params.addBodyParameter("e.newId", entry.getNewId());
		params.addBodyParameter("uuid", App.uuid);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.ADD_DIANZAN_URL,
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

						String result = responseInfo.result;
						Log.i("content",result);
						try {
							JSONObject json = new JSONObject(result);

							if (json.optString("success").equals("1")) {
								view.zanCount.setText(json.optString("dzsl"));
								view.mIvZan.setImageResource(R.drawable.zan_on);
							}else if (json.optString("success").equals("2")){
								Toast.makeText(ctx, "点赞失败,"+json.optString("msg"), Toast.LENGTH_SHORT).show();
								view.mIvZan.setImageResource(R.drawable.zan_on);
							} else if (json.optString("success").equals("0")) {
								Toast.makeText(ctx, "点赞失败，用户信息错误，请重新登录。", Toast.LENGTH_SHORT).show();
							}else{
								Toast.makeText(ctx, "点赞失败", Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							Toast.makeText(ctx, "点赞失败", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {

					}
				});
	}

	private class ViewHolder {


		TextView name;
		TextView time;
		TextView zanCount;
		TextView commentCount;
		TextView mTvCommentContent;
		ImageView picture;
		ImageView mIvZan;
		LinearLayout comment_zan_count;
		LinearLayout comment_topinglun;

		public ViewHolder(View view) {


			name = (TextView) view.findViewById(R.id.comment_name_text);
			time = (TextView) view.findViewById(R.id.comment_time_text);
			zanCount = (TextView) view.findViewById(R.id.textView1);
			commentCount = (TextView) view.findViewById(R.id.textView2);
			mTvCommentContent = (TextView) view.findViewById(R.id.comment_concent_text);
			picture = (ImageView) view.findViewById(R.id.news_image);
			mIvZan = (ImageView) view.findViewById(R.id.imageView1);
			comment_topinglun = (LinearLayout) view.findViewById(R.id.comment_topinglun);
			comment_zan_count = (LinearLayout) view.findViewById(R.id.comment_zan_count);
			view.setTag(this);
		}
	}
}
