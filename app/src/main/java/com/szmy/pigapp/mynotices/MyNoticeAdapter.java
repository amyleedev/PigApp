package com.szmy.pigapp.mynotices;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szmy.pigapp.R;

import java.util.List;

public class MyNoticeAdapter extends BaseAdapter {
	private List<MyNotice> bulbList;
	private Context context;
	private OnClickListenerEditOrDelete onClickListenerEditOrDelete;

	public MyNoticeAdapter(Context context, List<MyNotice> bulbList){
		this.bulbList=bulbList;
		this.context=context;
	}

	@Override
	public int getCount() {
		return bulbList.size();
	}

	@Override
	public MyNotice getItem(int position) {
		return bulbList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final MyNotice bulb=bulbList.get(position);
		View view;
		ViewHolder viewHolder;
		if(null == convertView) {
			view = View.inflate(context, R.layout.item_notices_layout, null);
			viewHolder=new ViewHolder();
			viewHolder.tvName=(TextView)view.findViewById(R.id.tvName);
			viewHolder.tvContent=(TextView)view.findViewById(R.id.tvContent);
			viewHolder.tvTime=(TextView)view.findViewById(R.id.tv_time);
//			viewHolder.img=(ImageView)view.findViewById(R.id.imgLamp);
			viewHolder.tvDelete=(TextView)view.findViewById(R.id.tv_delete);
			viewHolder.tvEdit=(TextView)view.findViewById(R.id.tv_read);
			viewHolder.mLlContent = (LinearLayout) view.findViewById(R.id.ll_content);
			viewHolder.sll_main = (SwipeListLayout) view
					.findViewById(R.id.sll_main);
			view.setTag(viewHolder);//store up viewHolder
		}else {
			view=convertView;
			viewHolder=(ViewHolder)view.getTag();
		}
		viewHolder.sll_main.setOnSwipeStatusListener(new MyNoticeListActivity.MyOnSlipStatusListener(
				viewHolder.sll_main));
		if (bulb.getStatus().equals("1")){
			viewHolder.tvEdit.setVisibility(View.GONE);
			viewHolder.tvName.setText(Html.fromHtml("<font color = '#676767'>"+bulb.getTitle()+"</font>"));
			viewHolder.tvContent.setText(Html.fromHtml("<font color = '#676767'>"+bulb.getContent()+"</font>"));
		}else{
			viewHolder.tvEdit.setVisibility(View.VISIBLE);
			viewHolder.tvName.setText(Html.fromHtml("<font color = '#ff222222'>"+bulb.getTitle()+"</font>"));
			viewHolder.tvContent.setText(Html.fromHtml("<font color = '#ff222222'>"+bulb.getContent()+"</font>"));
		}
  			viewHolder.tvTime.setText(bulb.getStartTime());
		viewHolder.mLlContent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (onClickListenerEditOrDelete!=null){
					onClickListenerEditOrDelete.OnClickListenerLook(position);
				}
			}
		});
		viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onClickListenerEditOrDelete!=null){
					onClickListenerEditOrDelete.OnClickListenerDelete(position);
				}
			}
		});
		viewHolder.tvEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onClickListenerEditOrDelete!=null){
					onClickListenerEditOrDelete.OnClickListenerEdit(position);
				}
			}
		});
		return view;
	}

	private class ViewHolder{
		TextView tvName,tvEdit,tvDelete,tvContent,tvTime;
		ImageView img;
		SwipeListLayout sll_main;
		LinearLayout mLlContent;
	}

	public interface OnClickListenerEditOrDelete{
		void OnClickListenerEdit(int position);
		void OnClickListenerDelete(int position);
		void OnClickListenerLook(int position);

	}

	public void setOnClickListenerEditOrDelete(OnClickListenerEditOrDelete onClickListenerEditOrDelete1){
		this.onClickListenerEditOrDelete=onClickListenerEditOrDelete1;
	}

}
