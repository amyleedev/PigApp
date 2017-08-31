package com.szmy.pigapp.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.entity.ProFit;

import java.util.List;

public class ProfitAdapter extends BaseAdapter {
	private List<ProFit> list;
	private LayoutInflater mInflater;
	private Context ctx;

	public ProfitAdapter(Context ctx, List<ProFit> list) {
		this.ctx = ctx;
		mInflater = LayoutInflater.from(ctx);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public ProFit getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_integral_list, null);
			new ViewHolder(convertView);
		}
		final ViewHolder holder = (ViewHolder) convertView.getTag();
		final ProFit entry = getItem(arg0);
		holder.tvName.setText(Html.fromHtml("<font color = '#ff0000'>来源:</font>"+entry.getSourceName().toString()));
//		holder.tvName.setText(Html.fromHtml("<font color = '#ff0000'>来源:</font>"+getTypeNme(entry.getSource().toString())));
		holder.tvTime.setText(entry.getCreateTime().substring(0, 10));
		String profit = "+ 0";
		if(!TextUtils.isEmpty(entry.getFinalProfit().toString())){
				profit = entry.getFinalProfit().toString();
				if(entry.getStatus().equals("n")){
					holder.tvPoint.setText(Html.fromHtml("<font color = '#8A8A8A'>"+profit+"</font>"));
				}else{
					if(Integer.parseInt(entry.getFinalProfit().toString())>0){
						profit = "+" + entry.getFinalProfit().toString();

						holder.tvPoint.setText(Html.fromHtml("<font color = '#EC2638'>"+profit+"</font>"));
					}else{
						holder.tvPoint.setText(Html.fromHtml("<font color = '#7EB148'>"+profit+"</font>"));
					}
				}


		}
//		holder.tvPoint.setText(profit);
		if(entry.getSource().toString().equals("3")){
			if(TextUtils.isEmpty(entry.getPresentee().toString())){
				holder.tvType.setText("");
			}else
			holder.tvType.setText(Html.fromHtml("<font color = '#FFD097'>推荐账号:</font>"+entry.getPresentee().toString()));
		}else{
			holder.tvType.setText("");
		}
		
		
		if(!TextUtils.isEmpty(entry.getRemark())){
			holder.llRemark.setVisibility(View.VISIBLE);
			holder.tvRemark.setText("备注："+entry.getRemark());
		}else{
			holder.llRemark.setVisibility(View.GONE);
		}
		return convertView;
	}
/*****
 * 8:被推广人认证猪场)(9:被推广人认证经纪人)(10:被推广人认证屠宰场)
	 * (11:被推广人每天首条消息)(12:提现)
 * @param type
 * @return
 */
	private String getTypeNme(String type){
		String st = "";
		switch(Integer.parseInt(type)){
		case 1:
			st = "被推荐人购买";
			break;
		case 2:
			st = "平台购买返利";
			break;
		case 3:
			st = "猪贸通首次登录";
			break;
		case 4:
			st = "猪贸通签到";
			break;
		case 5:
			st = "猪贸通个人认证";
			break;
		case 6:
			st = "猪贸通发布信息";
			break;
		case 7:
			st = "猪贸通成功交易";
			break;
		case 8:
			st = "被推广人认证猪场";
			break;
		case 9:
			st = "被推广人认证经纪人";
			break;
		case 10:
			st = "被推广人认证屠宰场";
			break;
		case 11:
			st = "被推广人每天首条消息";
			break;
		case 12:
			st = "提现";
			break;
		case 13:
			st = "提现失败";
			break;
		case 14:
			st = "补贴";
			break;
		case 15:
			st = "等待后台确认";
			break;
			default:
				st = "其他";
				break;
		}
		return st;
	}
	private class ViewHolder {
		TextView tvName;
		TextView tvTime;
		TextView tvType;
		TextView tvPoint;
		LinearLayout llRemark;
		TextView tvRemark;
		

		public ViewHolder(View view) {
			tvName = (TextView) view.findViewById(R.id.name_text);
			tvTime = (TextView) view.findViewById(R.id.recommend_date_text);
			tvType = (TextView) view.findViewById(R.id.login_type_text);
			tvPoint = (TextView) view.findViewById(R.id.getpoint_text);
			llRemark = (LinearLayout) view.findViewById(R.id.remark);
			tvRemark = (TextView) view.findViewById(R.id.pointremark);
			view.setTag(this);
		}
	}
}
