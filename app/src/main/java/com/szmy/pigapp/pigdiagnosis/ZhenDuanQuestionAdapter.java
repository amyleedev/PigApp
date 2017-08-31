package com.szmy.pigapp.pigdiagnosis;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szmy.pigapp.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by xushun on 16/7/5.
 */
public class ZhenDuanQuestionAdapter extends BaseAdapter {

    private Context context;

    private List<ZhenDuan> list;


    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;


    class ViewHolder {
        TextView tvName;
        CheckBox cb;
        LinearLayout LL;
    }

    public ZhenDuanQuestionAdapter(Context context, List<ZhenDuan> list, HashMap<Integer,Boolean> isSelected) {
        // TODO Auto-generated constructor stub
        this.list = list;
        this.context = context;
        this.isSelected = isSelected;
        // 初始化数据
        initDate();

    }

    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < list.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public ZhenDuan getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        // 页面
        Log.v("MyListViewBase", "getView " + position + " " + convertView);
        ViewHolder holder = null;

        LayoutInflater inflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.item_zbzd_list, null);
            holder = new ViewHolder();
            holder.cb = (CheckBox) convertView.findViewById(R.id.groupCheckBox);
            holder.tvName = (TextView) convertView.findViewById(R.id.groupText);
            holder.LL = (LinearLayout) convertView.findViewById(R.id.checklistitemlayout);
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }
        final ZhenDuan entry = getItem(position);
        System.out.println(isSelected.toString());
        holder.tvName.setText(entry.getName());
        // 监听checkBox并根据原来的状态来设置新的状态

        holder.LL.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                System.out.println("点击："+position);
                if (isSelected.get(position)) {
                    isSelected.put(position, false);
                    setIsSelected(isSelected);
                } else {
                    isSelected.put(position, true);
                    setIsSelected(isSelected);
                }
                notifyDataSetChanged();
            }
        });

        // 根据isSelected来设置checkbox的选中状况
        holder.cb.setChecked(getIsSelected().get(position));
        return convertView;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        ZhenDuanQuestionAdapter.isSelected = isSelected;
    }
}