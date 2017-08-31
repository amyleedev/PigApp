package com.szmy.pigapp.pds;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szmy.pigapp.R;

import java.util.List;

public class DeclarationAdapter extends BaseAdapter {
	private List<Declaration> list;
	private LayoutInflater mInflater;
	private Context ctx;

	public DeclarationAdapter(Context ctx, List<Declaration> list) {
		this.ctx = ctx;
		mInflater = LayoutInflater.from(ctx);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Declaration getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_declaration, null);
			new ViewHolder(convertView);
		}
		final ViewHolder holder = (ViewHolder) convertView.getTag();
		final Declaration qo = getItem(arg0);
		holder.tvAreaType.setText(getAreaType(Integer.parseInt(qo.getAREATYPE())));// 40 ,地点类别 0 规模化养殖场 1 散养 2 屠宰场
		holder.tvARRIVALLOCAL.setText(qo.getARRIVALLOCAL()) ;// 山西省,晋中市,太谷县,,,孙耀强 ,到达地点
		holder.tvARRIVALTIME.setText(qo.getARRIVALTIME()) ;//  ,到达时间
		holder.tvCODE.setText(qo.getCODE()) ;// A2016519734 , 编号
		holder.tvCONSIGNEE.setText(qo.getCONSIGNEE()) ;// 刘军广 ,货主
		holder.tvDECLDATE.setText(qo.getDECLDATE()) ;// 2016-01-19 09;//34;//58.0 , 受理时间
		holder.tvINSPDATETIME.setText(qo.getINSPDATETIME()) ;// 2016-01-19 09;//34;//58.0 , 申报时间
		holder.tvLEAVELTIME.setText(qo.getLEAVELTIME()) ;// 2016-01-19 00;//00;//00.0 , 启运时间
		holder.tvNUM.setText(qo.getNUM()+qo.getUNIT()) ;// 5000 ,
		holder.tvOPERATOR.setText(qo.getOPERATOR()) ;// 王春刚 , 经办人
		holder.tvQUARDATE.setText(qo.getQUARDATE()) ;// 2016-01-19 00;//00;//00.0 , 检疫时间
		holder.tvSIGN.setText(getSign(Integer.parseInt(qo.getSIGN()))) ;// 0 , 动物类型 0 是动物 1 是动物产品 
		holder.tvSORT.setText(qo.getSORT()) ;// 雏鸡 ,
		holder.tvSTATUS.setText(getType(Integer.parseInt(qo.getSTATUS()))) ;// 5 ,//状态(1;//未受理 2：已受理 3：不受理 4;//删除) 
		holder.tvTELEPHONE.setText(qo.getTELEPHONE()) ;// 13933000048 ,
		holder.tvTQUARLOCAL.setText(qo.getTQUARLOCAL()) ;// 河北省石家庄市高营镇西古城村 , 检疫地点
		holder.tvUNIT.setText(qo.getUNIT());// 只  单位
		holder.lLLayout1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				holder.lLLayout2.setVisibility(View.VISIBLE);
				holder.lLLayout3.setVisibility(View.GONE);
				
			}
		});
		holder.lLLayout2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				holder.lLLayout2.setVisibility(View.GONE);
				holder.lLLayout3.setVisibility(View.VISIBLE);
			}
		});
		return convertView;
	}

	private String getType(int type) {
		String t = "";
		switch (type) {
		case 1:
			t = "未受理";
			break;
		case 2:
			t = "已受理";
			break;
		case 3:
			t = "不受理";
			break;
		default:
			t = "其他";
			break;
		}
		return t;

	}

	private String getSign(int type) {
		String t = "";
		switch (type) {
		case 0:
			t = "动物";
			break;
		case 1:
			t = "动物产品";
			break;
		default:
			t = "其他";
			break;
		}
		return t;
	}

	private String getAreaType(int type) {
		String t = "";
		switch (type) {
		case 0:
			t = "规模化养殖场";
			break;
		case 1:
			t = "散养";
			break;
		case 2:
			t = "屠宰场";
			break;
		default:
			t = "其他";
			break;
		}
		return t;
	}

	private class ViewHolder {
		TextView  tvAreaType;// 40 ,地点类别 0 规模化养殖场 1 散养 2 屠宰场
		TextView  tvARRIVALLOCAL ;// 山西省,晋中市,太谷县,,,孙耀强 ,到达地点
		TextView  tvARRIVALTIME ;//  ,到达时间
		TextView  tvCODE ;// A2016519734 , 编号
		TextView  tvCONSIGNEE ;// 刘军广 ,货主
		TextView  tvDECLDATE ;// 2016-01-19 09;//34;//58.0 , 受理时间
		TextView  tvINSPDATETIME ;// 2016-01-19 09;//34;//58.0 , 申报时间
//		TextView  tvISOUT ;// 1 ,  区分省内证 省外证
		TextView  tvLEAVELOCAL ;// 河北省,石家庄市,长安区,,,高营镇西古城村 , 启运地点
		TextView  tvLEAVELTIME ;// 2016-01-19 00;//00;//00.0 , 启运时间
		TextView  tvNUM ;// 5000 ,
		TextView  tvOPERATOR ;// 王春刚 , 经办人
		TextView  tvQUARDATE ;// 2016-01-19 00;//00;//00.0 , 检疫时间
		TextView  tvSIGN ;// 0 , 动物类型 0 是动物 1 是动物产品 
		TextView  tvSORT ;// 雏鸡 ,
		TextView  tvSTATUS ;// 5 ,//状态(1;//未受理 2：已受理 3：不受理 4;//删除) 
		TextView  tvTELEPHONE ;// 13933000048 ,
		TextView  tvTQUARLOCAL ;// 河北省石家庄市高营镇西古城村 , 检疫地点
		TextView  tvUNIT ;// 只  单位
		LinearLayout lLLayout1;
		LinearLayout lLLayout2;
		LinearLayout lLLayout3;

		public ViewHolder(View view) {
			  tvAreaType = (TextView) view.findViewById(R.id.tv_type);
			  tvARRIVALLOCAL = (TextView) view.findViewById(R.id.tv_endadd);
			  tvARRIVALTIME = (TextView) view.findViewById(R.id.tv_endtime);
			  tvCODE = (TextView) view.findViewById(R.id.tv_name);
			  tvCONSIGNEE = (TextView) view.findViewById(R.id.tv_user);
			  tvDECLDATE = (TextView) view.findViewById(R.id.tv_decldate);
			  tvINSPDATETIME = (TextView) view.findViewById(R.id.tv_sbtime);
			  tvLEAVELOCAL = (TextView) view.findViewById(R.id.tv_startadd);
			  tvLEAVELTIME = (TextView) view.findViewById(R.id.tv_starttime);
			  tvNUM = (TextView) view.findViewById(R.id.tv_num);
			  tvOPERATOR = (TextView) view.findViewById(R.id.tv_opera);
			  tvQUARDATE = (TextView) view.findViewById(R.id.tv_quardate);
			  tvSIGN = (TextView) view.findViewById(R.id.tv_sign);
			  tvSORT = (TextView) view.findViewById(R.id.tv_sort);
			  tvSTATUS = (TextView) view.findViewById(R.id.tv_status);
			  tvTELEPHONE = (TextView) view.findViewById(R.id.tv_phone);
			  tvTQUARLOCAL = (TextView) view.findViewById(R.id.tv_jyadd);
			  tvUNIT = (TextView) view.findViewById(R.id.tv_unit);
			  lLLayout1 = (LinearLayout) view.findViewById(R.id.layout1);
			  lLLayout2 = (LinearLayout) view.findViewById(R.id.layout2);
			  lLLayout3 = (LinearLayout) view.findViewById(R.id.layoutqw);
			view.setTag(this);
		}
	}
}
