package com.szmy.pigapp.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.szmy.pigapp.R;
import com.szmy.pigapp.entity.InfoEntry;
import com.szmy.pigapp.utils.UrlEntry;

import java.util.List;

public class OrderAdapter extends BaseAdapter {

    private Context ctx;
    private List<InfoEntry> childJson;
    private LayoutInflater mInflater;
    private boolean mBusy = false;//
    private BitmapUtils bitmapUtils;

    public OrderAdapter(Context ctx, List<InfoEntry> list) {
        this.childJson = list;
        mInflater = LayoutInflater.from(ctx);
        bitmapUtils = new BitmapUtils(ctx);
    }

    public void setFlagBusy(boolean busy) {
        this.mBusy = busy;
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
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_info_fragment_list,
                    null);
            holder.mLlItem = (LinearLayout) convertView.findViewById(R.id.item);
            holder.mInfoIcon = (ImageView) convertView
                    .findViewById(R.id.imagehead);
            holder.mInfoTitle = (TextView) convertView
                    .findViewById(R.id.info_list_name_text);
            holder.mInfoType = (TextView) convertView
                    .findViewById(R.id.info_list_type_text);
            holder.mInfoTime = (TextView) convertView
                    .findViewById(R.id.info_list_data_text);
            holder.mInfoStatus = (TextView) convertView
                    .findViewById(R.id.info_list_status_text);
            holder.mInfoDealType = (TextView) convertView
                    .findViewById(R.id.info_list_mm_text);
            holder.mInfoPrice = (TextView) convertView
                    .findViewById(R.id.info_list_price_text);
            holder.mInfoCount = (TextView) convertView
                    .findViewById(R.id.info_list_count_text);
            holder.mInfoTzcName = (TextView) convertView.findViewById(R.id.info_tuzaichang_name_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        InfoEntry info = childJson.get(posotion);
            if (info.getCity().contains(info.getProvince())) {
                holder.mInfoTitle.setText(Html.fromHtml("【" + info.getCity() + "】 "
                        + info.getTitle() + "<font color = '#ff0000'>" + (TextUtils.isEmpty(info.getIszd()) ? "" : "[顶]") + "</font>"));
            } else {
                holder.mInfoTitle.setText(Html.fromHtml("【" + info.getProvince() + info.getCity() + "】 "
                        + info.getTitle() + "<font color = '#ff0000'>" + (TextUtils.isEmpty(info.getIszd()) ? "" : "[顶]") + "</font>"));
            }
            if (!TextUtils.isEmpty(info.getIszd())) {
                holder.mInfoTzcName.setVisibility(View.VISIBLE);
                holder.mInfoTzcName.setText("来源：" + info.getCompName());
            } else {
                holder.mInfoTzcName.setVisibility(View.GONE);
            }
            String pigType = info.getPigType().toString();
            // String pigType ="";
            if (pigType.equals("1")) {
                pigType = "内三元";
            } else if (pigType.equals("2")) {
                pigType = "外三元";
            } else if (pigType.equals("3")) {
                pigType = "土杂猪";
            } else if (pigType.equals("4")) {
                pigType = "肥猪";
            } else if (pigType.equals("5")) {
                pigType = "仔猪";
            } else if (pigType.equals("6")) {
                pigType = "种猪";
            } else {
                pigType = "";
            }
            holder.mInfoType.setText(pigType);
            holder.mInfoDealType.setText(info.getOrderType().equals("1") ? "出售"
                    : "收购");
            if (info.getPrice().equals("0")) {
                holder.mInfoPrice.setText(R.string.xieshang);
            } else {
                holder.mInfoPrice.setText(info.getPrice() + "元/公斤");
            }

            holder.mInfoCount.setText(info.getNumber() + "头");
            holder.mInfoTime.setText(info.getCreateTime().substring(5, 16));
            switch (Integer.parseInt(info.getOrderStatus().toString())) {
                case 1:
                    //判断审核状态为1未审核和3审核不通过
                    if (info.getCheckStatus().equals("1")) {
                        holder.mInfoStatus.setText(Html
                                .fromHtml("<font color = '#ff0000'>【审核中】</font>"));
                    } else if (info.getCheckStatus().equals("3")) {
                        holder.mInfoStatus.setText(Html
                                .fromHtml("<font color = '#ff0000'>【审核不通过】</font>"));
                    } else {
//                        holder.mInfoStatus.setText("【未成交】");
                        holder.mInfoStatus.setText(Html
                                .fromHtml("<font color = '#ff0000'>【未成交】</font>"));
                    }
                    break;
                case 2:
                    holder.mInfoStatus.setText("【已下单】");
                    break;
                case 3:
                    holder.mInfoStatus.setText(Html
                            .fromHtml("<font color = '#24B349'>【已成交】</font>"));
//                    holder.mInfoStatus.setText("【已成交】");
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
            if (TextUtils.isEmpty(info.getCoverPicture())) {
                holder.mInfoIcon.setImageResource(R.drawable.default_title);
            } else {
                try {
                    bitmapUtils.configDefaultLoadFailedImage(R.drawable.default_title);
                    bitmapUtils.display(holder.mInfoIcon, UrlEntry.ip
                            + info.getCoverPicture().toString());// 下载并显示图片
                } catch (Exception ex) {
                    holder.mInfoIcon.setImageResource(R.drawable.default_title);
                }
        }



//			if (!mBusy) {
//				// mImageLoader.DisplayImage(UrlEntry.ip +
//				// info.getCoverPicture(),
//				// holder.mInfoIcon, false, 10, false, true);
//				com.nostra13.universalimageloader.core.ImageLoader
//						.getInstance().displayImage(
//								UrlEntry.ip + info.getCoverPicture(),
//								holder.mInfoIcon, AppStaticUtil.getOptions(),
//								new SimpleImageLoadingListener() {
//									@Override
//									public void onLoadingFailed(
//											String imageUri, View view,
//											FailReason failReason) {
//										// TODO Auto-generated method stub
//										super.onLoadingFailed(imageUri, view, failReason);
//									}
//
//									@Override
//									public void onLoadingComplete(
//											String imageUri, View view,
//											android.graphics.Bitmap loadedImage) {
//										super.onLoadingComplete(imageUri, view,
//												loadedImage);
//										if(loadedImage==null){
//											return;
//										}
//										holder.mInfoIcon.setImageBitmap(AppStaticUtil
//												.getRoundedCornerBitmap(
//														loadedImage, 10));
//									}
//								}, new ImageLoadingProgressListener() {
//
//									@Override
//									public void onProgressUpdate(
//											String imageUri, View view,
//											int current, int total) {
//
//									}
//
//								});
//			}

        return convertView;
    }

    public void addInfoItem(InfoEntry info) {
        childJson.add(info);
    }

    private class ViewHolder {
        ImageView mInfoIcon;
        TextView mInfoTitle;
        TextView mInfoStatus;
        TextView mInfoTime;
        TextView mInfoType;
        TextView mInfoPrice;
        TextView mInfoDealType;
        TextView mInfoCount;
        TextView mInfoTzcName;
        LinearLayout mLlItem;
    }
}
