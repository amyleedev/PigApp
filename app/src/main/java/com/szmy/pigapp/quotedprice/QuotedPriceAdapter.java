package com.szmy.pigapp.quotedprice;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.szmy.pigapp.R;
import com.szmy.pigapp.utils.AppStaticUtil;

import java.util.List;

public class QuotedPriceAdapter extends BaseAdapter {

    private Context ctx;
    private List<QuotedPrice> childJson;
    private LayoutInflater mInflater;
    private boolean mBusy = false;//
    private BitmapUtils bitmapUtils;
    private String type = "0";
    private View.OnClickListener paramOnClickListener;

    public QuotedPriceAdapter(Context ctx, List<QuotedPrice> list,String looktype,View.OnClickListener paramOnClickListener) {
        this.childJson = list;
        mInflater = LayoutInflater.from(ctx);
        bitmapUtils = new BitmapUtils(ctx);
        this.type = looktype;
        this.paramOnClickListener = paramOnClickListener;
        this.ctx = ctx;
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
            convertView = mInflater.inflate(R.layout.item_quoted_fragment_list,
                    null);
            holder.mLlItem = (LinearLayout) convertView.findViewById(R.id.item);
            holder.mInfoTitle = (TextView) convertView
                    .findViewById(R.id.info_list_name_text);
            holder.mInfoType = (TextView) convertView
                    .findViewById(R.id.info_list_usertype_text);
            holder.mInfoTime = (TextView) convertView
                    .findViewById(R.id.info_list_time_text);
            holder.mInfoAddress = (TextView) convertView
                    .findViewById(R.id.info_list_address_text);
            holder.mInfoPrice = (TextView) convertView
                    .findViewById(R.id.tvPrice);
            holder.mInfoCount = (TextView) convertView
                    .findViewById(R.id.info_list_count_text);
            holder.mInfoisZd = (TextView) convertView.findViewById(R.id.info_list_zd_text);
            holder.mTvLookPrice = (TextView) convertView.findViewById(R.id.info_list_status_text);
            holder.mRlLookPrice = (RelativeLayout) convertView.findViewById(R.id.rl_lookprice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final QuotedPrice info = childJson.get(posotion);
        if (!TextUtils.isEmpty(info.getIszd())){
            holder.mInfoisZd.setVisibility(View.VISIBLE);
        }
        holder.mInfoTitle.setText(info.getProductType().toString());
        if (TextUtils.isEmpty(info.getArea().toString())){
            if (TextUtils.isEmpty(info.getCity().toString())){
                holder.mInfoAddress.setText(info.getProvince().toString());
            }else{
                holder.mInfoAddress.setText(info.getCity().toString());
            }
        }else
        holder.mInfoAddress.setText(info.getArea().toString());
        holder.mInfoCount.setText(info.getNumber()+"头");
        holder.mInfoPrice.setText(info.getPrice()+"元/公斤");

        if (TextUtils.isEmpty(info.getUserType())){
            holder.mInfoType.setText("网络");
            holder.mInfoPrice.setVisibility(View.VISIBLE);
            holder.mTvLookPrice.setVisibility(View.GONE);
        }else {
            if(TextUtils.isEmpty(info.getUserName())||AppStaticUtil.isNumeric(info.getUserName())){
                holder.mInfoType.setText(getuserType(info.getUserType().toString()));
            }else{
                holder.mInfoType.setText(info.getUserName().toString());
            }
            if (type.equals("1")){
                holder.mInfoPrice.setVisibility(View.VISIBLE);
                holder.mTvLookPrice.setVisibility(View.VISIBLE);
                holder.mTvLookPrice.setText("点击查看详情");

            }else{
                holder.mInfoPrice.setVisibility(View.GONE);
                holder.mTvLookPrice.setVisibility(View.VISIBLE);
                holder.mTvLookPrice.setText("点击查看价格");

            }
        }
        holder.mInfoTime.setText(TextUtils.isEmpty(info.getMarketingTime())?info.getCreateTime().substring(5, 10):info.getMarketingTime().substring(5, 10));
        // 设置每个子布局的事件监听器

            holder.mRlLookPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
            Intent intent = new Intent(ctx,QuotedPriceDetailActivity.class);
                    intent.putExtra("id",info.getId());
                    ctx.startActivity(intent);
                }
            });


        return convertView;
    }

    private  String getuserType(String type){
        String str = "";
        if(type.equals("1")){
            str = "猪场";
        }else if(type.equals("2")){
            str = "屠宰场";
        }else if(type.equals("3")){
            str = "经纪人";
        }else{
            str = "网络";
        }
        return str;
    }
    public void addInfoItem(QuotedPrice info) {
        childJson.add(info);
    }

    private class ViewHolder {
        TextView mInfoTitle;//生猪类型
        TextView mInfoAddress;//地址
        TextView mInfoTime;//时间
        TextView mInfoType;//角色
        TextView mInfoPrice;//价格
        TextView mInfoCount;//数量
        TextView mInfoisZd;
        LinearLayout mLlItem;
        TextView mTvLookPrice;//查看价格
        RelativeLayout mRlLookPrice;
    }
}
