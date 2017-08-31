package com.szmy.pigapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.entity.StatisticsAdmin;

import java.util.List;


public class StatisticsAdminAdapter extends BaseAdapter {
    private List<StatisticsAdmin> list;
    private LayoutInflater mInflater;
    private Context ctx;

    public StatisticsAdminAdapter(Context ctx, List<StatisticsAdmin> list) {
        this.ctx = ctx;
        mInflater = LayoutInflater.from(ctx);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public StatisticsAdmin getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_statistics_admin, null);
            new ViewHolder(convertView);
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        final StatisticsAdmin entry = getItem(arg0);
        holder.tvName.setText(entry.getCompName());
        holder.tvCjzj.setText(entry.getAllPrice());
        holder.tvCll.setText(entry.getAllNumber());
        holder.tvFbts.setText(entry.getAllCount());
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
        TextView tvCjzj;
        TextView tvCll;
        TextView tvFbts;

        public ViewHolder(View view) {
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvCjzj = (TextView) view.findViewById(R.id.tv_cjzj);
            tvCll = (TextView) view.findViewById(R.id.tv_cll);
            tvFbts = (TextView) view.findViewById(R.id.tv_fbts);
            view.setTag(this);
        }
    }
}
