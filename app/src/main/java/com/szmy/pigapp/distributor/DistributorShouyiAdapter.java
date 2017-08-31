package com.szmy.pigapp.distributor;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.szmy.pigapp.R;
import com.szmy.pigapp.entity.ProFit;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.UrlEntry;

import java.util.List;


public class DistributorShouyiAdapter extends BaseAdapter {
    private List<ProFit> list;
    private LayoutInflater mInflater;
    private Context ctx;
    private BitmapUtils bitmapUtils;
    private ProFit info;
    private String type;

    public DistributorShouyiAdapter(Context ctx, List<ProFit> list,String sctctype) {
        this.ctx = ctx;
        mInflater = LayoutInflater.from(ctx);
        this.list = list;
        this.type = sctctype;
        bitmapUtils = new BitmapUtils(ctx);

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
            convertView = mInflater.inflate(R.layout.item_fxssy, null);
            new ViewHolder(convertView);
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        info = getItem(arg0);

        holder.tvProName.setText(info.getProductName());
        holder.tvComName.setText("店铺名称：" + info.getCompName());
        if (type.equals("2")){
            holder.tvXiaDanName.setText("市场用户：" + info.getAccount());
        }else
        holder.tvXiaDanName.setText("下单人：" + info.getPresentee());
        if (!TextUtils.isEmpty(info.getNumber())) {
            holder.tvPrice.setText(info.getPrice());
        }

        holder.tvJifen.setText(info.getFinalProfit());
        if (TextUtils.isEmpty(info.getRatio())||Double.parseDouble(info.getRatio())==0){}else
        holder.tvBili.setText(info.getNumber());
        holder.tvStatus.setText(getType(info.getOrderStatus()));
        if (TextUtils.isEmpty(info.getCity())){
            holder.tvOrderId.setText("订单号："+info.getOrderID());
        }else{
            holder.tvOrderId.setText("订单号："+info.getOrderID()+"  "+ Html.fromHtml("<font color = '#ff0000'>   ["+info.getCity()+"]      </font>"));
        }

        if (info.getStatus().equals("y")){
            holder.tvStatus.setBackgroundResource(R.drawable.ic_yuanjiao);
            if(info.getCreateTime().length()<16){

            }else {
                    holder.tvDate.setText("结算" + info.getCreateTime().substring(0, 16));
            }
        }else{
            holder.tvStatus.setBackgroundResource(R.drawable.ic_blueyuanjiao);
        }
        if (info.getXdrq().length()<16){

        }else
        holder.tvCreateTime.setText(info.getXdrq().substring(0,16)+"创建");

        if (!TextUtils.isEmpty(info.getProductImg())) {
            com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                    .displayImage(UrlEntry.ip + info.getProductImg(),
                            holder.mIvHead, AppStaticUtil.getOptions());
        } else {
            holder.mIvHead.setImageResource(R.drawable.default_title);
        }

        return convertView;
    }
private String getType(String type){
    String str = "";
    if(type.equals("init")){
        str = "已下单";
    }else if(type.equals("pass")){
        str = "已付款";
    }else if(type.equals("sign")){
        str = "已收益";
    }else if(type.equals("cancel")){
        str = "已取消";
    }else if(type.equals("send")){
        str = "已发货";
    }
    return str;
}

    private class ViewHolder {
        ImageView mIvHead;
        TextView tvProName;
        TextView tvComName;
        TextView tvXiaDanName;
        TextView tvPrice;
        TextView tvJifen;
        TextView tvBili;
        TextView tvDate;
        TextView tvStatus;
        TextView tvOrderId;
        TextView tvCreateTime;

        public ViewHolder(View view) {
            tvOrderId = (TextView) view.findViewById(R.id.tv_orderid);
            tvStatus = (TextView) view.findViewById(R.id.tv_status);
            mIvHead = (ImageView) view.findViewById(R.id.imageview_farm);
            tvProName = (TextView) view.findViewById(R.id.pro_name);
            tvComName = (TextView) view.findViewById(R.id.com_name);
            tvXiaDanName = (TextView) view.findViewById(R.id.xiadan_name);
            tvPrice = (TextView) view.findViewById(R.id.proprice);
            tvJifen = (TextView) view.findViewById(R.id.tv_jifen);
            tvBili = (TextView) view.findViewById(R.id.tv_bili);
            tvDate = (TextView) view.findViewById(R.id.tv_date);
            tvCreateTime = (TextView) view.findViewById(R.id.tv_cjdate);
            view.setTag(this);
        }
    }
}
