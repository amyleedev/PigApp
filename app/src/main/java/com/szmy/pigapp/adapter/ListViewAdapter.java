package com.szmy.pigapp.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.szmy.pigapp.R;
import com.szmy.pigapp.entity.InfoEntry;
import com.szmy.pigapp.utils.UrlEntry;

import java.util.List;


public class ListViewAdapter extends BaseAdapter {

	private ViewHolder holder;
	private List<InfoEntry> childJson;
	private LayoutInflater mInflater;
	private BitmapUtils bitmapUtils ;
	private class ViewHolder {
		ImageView mInfoIcon;
		TextView mInfoTitle;
		TextView mInfoStatus;
		TextView mInfoTime;
		TextView mInfoType;
		TextView mInfoPrice;
		TextView mInfoDealType;
		TextView mInfoCount;
	}
	public ListViewAdapter(List<InfoEntry> list,LayoutInflater mInflater,BitmapUtils bitmapUtils) {
		this.childJson = list;
		this.mInflater= mInflater;
		this.bitmapUtils = bitmapUtils;
	}

	
	@Override
	public int getCount() {
		return childJson.size();
	}

	@Override
	public Object getItem(int position) {
		return childJson.get(position);
	}

	@Override
	public long getItemId(int posotion) {
		return posotion;
	}

	@Override
	public View getView(int posotion, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_info_fragment_list, null);
			holder.mInfoIcon = (ImageView) convertView.findViewById(R.id.imagehead);
			holder.mInfoTitle = (TextView) convertView.findViewById(R.id.info_list_name_text);
			holder.mInfoType = (TextView) convertView.findViewById(R.id.info_list_type_text);
			holder.mInfoTime = (TextView) convertView.findViewById(R.id.info_list_data_text);
			holder.mInfoStatus = (TextView) convertView.findViewById(R.id.info_list_status_text);
			holder.mInfoDealType = (TextView) convertView.findViewById(R.id.info_list_mm_text);
			holder.mInfoPrice = (TextView) convertView.findViewById(R.id.info_list_price_text);
			holder.mInfoCount = (TextView) convertView.findViewById(R.id.info_list_count_text);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		InfoEntry info = childJson.get(posotion);
			if ( info.getCity().contains(info.getProvince())) {
				holder.mInfoTitle.setText("【" + info.getCity() + "】 "
						+ info.getTitle());
			} else {
				holder.mInfoTitle.setText("【" + info.getProvince()+info.getCity() + "】 "
						+ info.getTitle());
			}
			String pigType  =info.getPigType().toString();
//		String pigType  ="";
			if(pigType.equals("1")){
				pigType = "内三元";
			}else if(pigType.equals("2")){
				pigType = "外三元";
			}else if(pigType.equals("3")){
				pigType = "土杂猪";
			}else if(pigType.equals("4")){
				pigType = "肥猪";
			}else if(pigType.equals("5")){
				pigType = "仔猪";
			}else if(pigType.equals("6")){
				pigType = "种猪";
			}else
			{
				pigType = "";
			}

			holder.mInfoType.setText(pigType);

			holder.mInfoDealType.setText(info.getOrderType().equals("1")?"出售":"收购");
			if (info.getPrice().equals("0")) {
				holder.mInfoPrice.setText(R.string.xieshang);
			} else
				holder.mInfoPrice.setText(info.getPrice()+"元/公斤");

			holder.mInfoCount.setText(info.getNumber()+"头");
			holder.mInfoTime.setText(info.getCreateTime().substring(5, 16));
			switch(Integer.parseInt(info.getOrderStatus().toString())){
				case 1:
					//判断审核状态为1未审核和3审核不通过
					if(info.getCheckStatus().equals("1")){
						holder.mInfoStatus.setText(Html
								.fromHtml("<font color = '#ff0000'>【审核中】</font>"));
					}else if(info.getCheckStatus().equals("3")){
						holder.mInfoStatus.setText(Html
								.fromHtml("<font color = '#ff0000'>【审核不通过】</font>"));
					}else
						holder.mInfoStatus.setText("【未成交】");
					break;
				case 2:
					holder.mInfoStatus.setText("【已下单】");
					break;
				case 3:
					holder.mInfoStatus.setText("【已成交】");
					break;
				case 4:
					holder.mInfoStatus.setText("【已确认订单】");
					break;
				case 5:
					holder.mInfoStatus.setText("【已支付】");
					break;
				case 6:
					holder.mInfoStatus.setText("【已发货】");
					break;
				case 7:
					holder.mInfoStatus.setText("【等待后台确认】");
					break;
				case 8:
					holder.mInfoStatus.setText("【订单已失效】");
					break;
			}


			if (info.getCoverPicture().toString().equals("")) {
				holder.mInfoIcon.setImageResource(R.drawable.default_title);
			} else {
				try{
					bitmapUtils.configDefaultLoadFailedImage(R.drawable.default_title);
					bitmapUtils.display(holder.mInfoIcon, UrlEntry.ip
							+ info.getCoverPicture().toString());// 下载并显示图片
				}catch(Exception ex){
					holder.mInfoIcon.setImageResource(R.drawable.default_title);
				}
			}


		
		return convertView;
	}
	 /**
     * 添加数据列表项
     * @param info
     */
    public void addInfoItem(InfoEntry info){
    	childJson.add(info);
    }


}
