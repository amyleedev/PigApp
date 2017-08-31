package com.szmy.pigapp.liulanliang;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.entity.Ecaluate;
import com.szmy.pigapp.entity.NewsEntity;
import com.szmy.pigapp.entity.UserInfo;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.UrlEntry;

import java.util.List;

public class LiulanAdapter extends BaseAdapter {
    private List<LiuLan> list;
    private LayoutInflater mInflater;
    private Context ctx;

    public LiulanAdapter(Context ctx, List<LiuLan> list) {
        this.ctx = ctx;
        mInflater = LayoutInflater.from(ctx);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public LiuLan getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_liulan, null);
            new ViewHolder(convertView);
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        final LiuLan entry = getItem(arg0);
        holder.tvName.setText(entry.getUserName());
        holder.tvTime.setText("浏览时间："+entry.getCreateTime().substring(5, 16));
        holder.news_image.setImageResource(R.drawable.head_default);
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
        TextView tvTime;
        ImageView news_image;

        public ViewHolder(View view) {
            tvName = (TextView) view.findViewById(R.id.news_name_text);
            tvTime = (TextView) view.findViewById(R.id.news_time_text);
            news_image = (ImageView) view.findViewById(R.id.news_image);
            view.setTag(this);
        }
    }
}
