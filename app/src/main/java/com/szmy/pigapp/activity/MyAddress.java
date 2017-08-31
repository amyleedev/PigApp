package com.szmy.pigapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.widgets.Dialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.szmy.pigapp.R;
import com.szmy.pigapp.entity.AddressInfo;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.HttpUtil;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.MySwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MyAddress extends BaseActivity {
	private LayoutInflater mInflater;
	private List<AddressInfo> childJson = new ArrayList<AddressInfo>(); // 子列表
	private MyAdapter infoAdapter;
	private Button add_btn;
	private static final int PAGE_SIZE = 20;
	private int mPage;
	private Context mContext;
	private MySwipeRefreshLayout mSrlData;
	private ListView mLv;
	private View mFooterView;
	private LinearLayout mLlFooterLoading;
	private TextView mTvFooterResult;
	private boolean mIsLoadAll = false;
	private LinearLayout mLlLoading;
	private LinearLayout mLlLoadFailure;
	private boolean mIsLoading;

	private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {

		@Override
		public void onRefresh() {
			reload();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myset_address);
		((TextView) findViewById(R.id.def_head_tv)).setText("管理收货地址");
		add_btn = (Button) findViewById(R.id.add_address_btn);
		initView();
	}

	public void initView() {

		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		infoAdapter = new MyAdapter(getBaseContext());
		mContext = MyAddress.this;
		mFooterView = LayoutInflater.from(mContext).inflate(
				R.layout.footer_view, null);
		mLlFooterLoading = (LinearLayout) mFooterView
				.findViewById(R.id.ll_footer_loading);
		mTvFooterResult = (TextView) mFooterView
				.findViewById(R.id.tv_footer_result);
		mSrlData = (MySwipeRefreshLayout) findViewById(R.id.srl_address);
		mSrlData.setOnRefreshListener(mRefreshListener);
		mSrlData.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		mLv = (ListView) findViewById(R.id.info_framgment_xlistview);
		mLv.setOnScrollListener(mOnScrollListener);
		mLv.setOnItemClickListener(mItemClickListener);
		mLlLoading = (LinearLayout) findViewById(R.id.ll_loading);
		mLlLoadFailure = (LinearLayout) findViewById(R.id.ll_load_failure);
		mLlLoadFailure.setOnClickListener(mClickListener);

		add_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyAddress.this, NewAddress.class);
				startActivityForResult(intent, 99);
			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		loadData();
	}

	private void loadData() {

		mIsLoading = true;
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		params.addBodyParameter("pager.offset", String.valueOf(mPage));
		params.addBodyParameter("e.pageSize", String.valueOf(PAGE_SIZE));
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_MYADDRESS_URL,
				params, new RequestCallBack<String>() {
					@Override
					public void onStart() {
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						mIsLoading = false;
						getData(responseInfo.result);
						Log.i("insert", " insert responseInfo.result = "
								+ responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						mIsLoading = false;
						if (mLlLoading.getVisibility() == View.VISIBLE) {
							mLlLoading.setVisibility(View.GONE);
							mLlLoadFailure.setVisibility(View.VISIBLE);
						} else {
							mSrlData.setRefreshing(false);
						}
						if (mPage > 1)
							mPage--;
					}
				});

	}

	private void getData(String result) {
		JSONObject jsonresult;
		try {
			jsonresult = new JSONObject(result);
			if (jsonresult.get("success").toString().equals("1")) {
				// Toast.makeText(mContext, "查询成功！", Toast.LENGTH_SHORT).show();
				List<AddressInfo> childJsonItem = new ArrayList<AddressInfo>();
				JSONArray jArrData = jsonresult.getJSONArray("list");
				for (int i = 0; i < jArrData.length(); i++) {
					JSONObject jobj2 = jArrData.optJSONObject(i);
					GsonBuilder gsonb = new GsonBuilder();
					Gson gson = gsonb.create();
					AddressInfo mInfoEntry = gson.fromJson(jobj2.toString(),
							AddressInfo.class);
					childJsonItem.add(mInfoEntry);
				}
				if (mLlLoading.getVisibility() == View.VISIBLE) {
					mLlLoading.setVisibility(View.GONE);
					mSrlData.setVisibility(View.VISIBLE);
				} else {
					mSrlData.setRefreshing(false);
				}
				if (mPage == 1) {
					if (childJson.size() >= 0) {
						childJson.clear();
						mLv.removeFooterView(mFooterView);
					}
					mLv.addFooterView(mFooterView);
					childJson.addAll(childJsonItem);
					infoAdapter = new MyAdapter(getBaseContext());
					mLv.setAdapter(infoAdapter);
					if (childJsonItem.size() == 0) {
						mLlFooterLoading.setVisibility(View.GONE);
						mTvFooterResult.setVisibility(View.VISIBLE);
						mTvFooterResult.setText("查询结果为空");
						mIsLoadAll = true;
					} else if (childJsonItem.size() < 20) {
						mLlFooterLoading.setVisibility(View.GONE);
						mTvFooterResult.setVisibility(View.VISIBLE);
						mTvFooterResult.setText("已加载全部");
						mIsLoadAll = true;
					} else {
						mLlFooterLoading.setVisibility(View.VISIBLE);
						mTvFooterResult.setVisibility(View.GONE);
					}
				} else {
					childJson.addAll(childJsonItem);
					infoAdapter = new MyAdapter(getBaseContext());
					mLv.requestLayout();
					infoAdapter.notifyDataSetChanged();
					if (childJsonItem.size() < 20) {
						mLlFooterLoading.setVisibility(View.GONE);
						mTvFooterResult.setVisibility(View.VISIBLE);
						mTvFooterResult.setText("已加载全部");
						mIsLoadAll = true;
					} else {
						mLlFooterLoading.setVisibility(View.VISIBLE);
						mTvFooterResult.setVisibility(View.GONE);
					}
				}
				infoAdapter.notifyDataSetChanged();
			} else {
				mIsLoading = false;
				if (mLlLoading.getVisibility() == View.VISIBLE) {
					mLlLoading.setVisibility(View.GONE);
					mLlLoadFailure.setVisibility(View.VISIBLE);
				} else {
					mSrlData.setRefreshing(false);
				}
				if (mPage > 1)
					mPage--;
				successType(jsonresult.get("success").toString(), "查询失败！");
			}
		} catch (JSONException e) {
		}
	}

	private void load() {
		reload();
		mLlLoading.setVisibility(View.VISIBLE);
		mSrlData.setVisibility(View.GONE);
		mLlLoadFailure.setVisibility(View.GONE);
	}

	private void reload() {
		mPage = 1;
		mIsLoadAll = false;
		loadData();
	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_load_failure:
				load();
				break;
			}
		}
	};
	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int wit,
				long arg3) {

		}
	};

	private OnScrollListener mOnScrollListener = new OnScrollListener() {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (firstVisibleItem + visibleItemCount == totalItemCount) {
				if (mIsLoading)
					return;
				if (mIsLoadAll)
					return;
				mPage += 1;
				loadData();
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}
	};



	class MyAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;

		public MyAdapter(Context context) {
			this.context = context;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return childJson != null ? childJson.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.myset_adress_list_item,
						null);
				viewHolder = new ViewHolder();
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.myset_address_item_name);

				viewHolder.address = (TextView) convertView
						.findViewById(R.id.myset_address_item_provinces);
				viewHolder.mBtnUpp = (Button) convertView
						.findViewById(R.id.btn_upp);
				viewHolder.mBtnDel = (Button) convertView
						.findViewById(R.id.btn_del);
				viewHolder.phone = (TextView) convertView
						.findViewById(R.id.myset_address_phone);
				viewHolder.moren = (CheckBox) convertView
						.findViewById(R.id.loading_checkbox);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.moren.setClickable(false);
			viewHolder.name.setText(" " + childJson.get(position).getName());
			viewHolder.address.setText(" "
					+ childJson.get(position).getPcadetail()
					+ childJson.get(position).getAddress());
			viewHolder.phone.setText(childJson.get(position).getMobile());
			viewHolder.mBtnUpp.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 修改
					Intent i = new Intent(MyAddress.this,NewAddress.class);
					Bundle b = new Bundle();
					b.putSerializable("address", childJson.get(position));
					i.putExtra("address_id", b);
					startActivityForResult(i, 99);
				}
			});
			viewHolder.mBtnDel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 删除
					final Dialog dialog = new Dialog(MyAddress.this, "删除收货地址", "确定删除这个收货地址吗?");
					dialog.addCancelButton("取消");
					dialog.setOnAcceptButtonClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.cancel();
							showDialog();
							MyRunnable run = new MyRunnable(childJson.get(position).getId());
							new Thread(run).start();
						}
					});
					dialog.setOnCancelButtonClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.cancel();
						}
					});
					dialog.show();
//					AlertDialog dialog = new AlertDialog.Builder(MyAddress.this)
//					.setTitle("删除收货地址")
//					.setMessage("确定删除这个收货地址吗?")
//					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//
//							showDialog();
//							    MyRunnable run = new MyRunnable(childJson.get(position).getId());
//							    new Thread(run).start();
//						}
//					})
//					.setNegativeButton("取消", new AlertDialog.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//						}
//					})
//					.create();// 创建
//					// 显示对话框
//					dialog.show();
				}
			});
			

			if (childJson.get(position).getIsdefault().equals("y")) {
				viewHolder.moren.setChecked(true);
			} else {
				viewHolder.moren.setVisibility(View.GONE);
				viewHolder.moren.setChecked(false);

			}

			return convertView;
		}

		class ViewHolder {
			
			TextView name, address, phone;
			CheckBox moren;
			Button mBtnUpp;
			Button mBtnDel;

		}
	}
	
	public class MyRunnable implements Runnable {
		String id = "";
		public MyRunnable(String addressid){
			this.id=addressid;
		}
		@Override
			public void run() {
				Message msg = new Message();
				Bundle data = new Bundle();
				String result = "";
				MultipartEntity mpEntity = new MultipartEntity();

				try {
					
					mpEntity.addPart("ids", new StringBody(id));
					mpEntity.addPart("uuid",
							new StringBody(App.uuid));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				HttpUtil http = new HttpUtil();
				result = http.postDataMethod(UrlEntry.DELETE_MYADDRESS_URL,
						mpEntity);

				data.putString("value", result);
				msg.setData(data);
				msg.what = 1;

				handler.sendMessage(msg);
			}

		}
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				disDialog();
				Bundle data1 = msg.getData();
				String result = data1.getString("value");
				JSONObject jsonresult;
				switch (msg.what) {
			
			
			
				case 1://删除
					if (!result.equals("")) {
						try {
							jsonresult = new JSONObject(result);
							if (jsonresult.get("success").toString().equals("1")) {
								showToast("删除成功！");
								
								reload();
								
								
							} else if(jsonresult.get("success").toString().equals("3")) {
								showToast("删除失败！");
							} else if(jsonresult.get("success").toString().equals("0")){
								showToast("用户信息错误，请重新登录！");
								editor.putString("userinfo", "");
								editor.commit();
								
								
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					break;
				}
				super.handleMessage(msg);
			}
		};
}
