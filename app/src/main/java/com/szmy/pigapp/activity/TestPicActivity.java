package com.szmy.pigapp.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.image.CustomConstants;
import com.szmy.pigapp.image.ImageBucket;
import com.szmy.pigapp.image.ImageBucketAdapter;
import com.szmy.pigapp.image.ImageChooseActivity;
import com.szmy.pigapp.image.ImageFetcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ���ѡȡͼƬ
 * @author Amy
 *
 */
public class TestPicActivity extends Activity {
	// ArrayList<Entity> dataList;//用来装载数据源的列表
	private List<ImageBucket> mDataList = new ArrayList<ImageBucket>();
	GridView gridView;
	ImageBucketAdapter mAdapter;// 自定义的适配器
	ImageFetcher helper;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;
	private int availableSize;
	private ListView mListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_image_bucket_choose);

		helper = ImageFetcher.getInstance(getApplicationContext());
		

		initData();
		initView();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// /**
		// * 这里，我们假设已经从网络或者本地解析好了数据，所以直接在这里模拟了10个实体类，直接装进列表中
		// */
		// dataList = new ArrayList<Entity>();
		// for(int i=-0;i<10;i++){
		// Entity entity = new Entity(R.drawable.picture, false);
		// dataList.add(entity);
		// }
		mDataList = helper.getImagesBucketList(false);	
		bimap=BitmapFactory.decodeResource(
				getResources(),
				R.drawable.icon_addpic_unfocused);
		availableSize = getIntent().getIntExtra(
				CustomConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
				CustomConstants.MAX_IMAGE_SIZE);
	}

	
	
	/**
	 * 初始化view视图
	 */
	private void initView() {
		TextView quxiaotx=(TextView) findViewById(R.id.action);
		quxiaotx.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			   finish();	
			}
		});
		mListView = (ListView) findViewById(R.id.listview);
		mAdapter = new ImageBucketAdapter(this, mDataList);
		mListView.setAdapter(mAdapter);
		TextView titleTv  = (TextView) findViewById(R.id.title);
		titleTv.setText("相册");
		mListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{

				selectOne(position);

				Intent intent = new Intent(TestPicActivity.this,
						ImageChooseActivity.class);
				intent.putExtra(CustomConstants.EXTRA_IMAGE_LIST,
						(Serializable) mDataList.get(position).imageList);
				intent.putExtra(CustomConstants.EXTRA_BUCKET_NAME,
						mDataList.get(position).bucketName);
				intent.putExtra(CustomConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
						availableSize);
				intent.putExtra("type", getIntent().getIntExtra("type", 0));
				startActivity(intent);//(intent, 11);
				finish();
				
			}
		});
		
//		gridView = (GridView) findViewById(R.id.gridview);
//		adapter = new ImageBucketAdapter(TestPicActivity.this, dataList);
//		gridView.setAdapter(adapter);
//
//		gridView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				/**
//				 * 根据position参数，可以获得跟GridView的子View相绑定的实体类，然后根据它的isSelected状态，
//				 * 来判断是否显示选中效果。 至于选中效果的规则，下面适配器的代码中会有说明
//				 */
//				// if(dataList.get(position).isSelected()){
//				// dataList.get(position).setSelected(false);
//				// }else{
//				// dataList.get(position).setSelected(true);
//				// }
//				/**
//				 * 通知适配器，绑定的数据发生了改变，应当刷新视图
//				 */
//				// adapter.notifyDataSetChanged();
//				Intent intent = new Intent(TestPicActivity.this,
//						ImageGridActivity.class);
//				intent.putExtra(TestPicActivity.EXTRA_IMAGE_LIST,
//						(Serializable) dataList.get(position).imageList);
//				startActivity(intent);
//				finish();
//			}
//
//		});
	}
	private void selectOne(int position)
	{
		int size = mDataList.size();
		for (int i = 0; i != size; i++)
		{
			if (i == position) mDataList.get(i).selected = true;
			else
			{
				mDataList.get(i).selected = false;
			}
		}
		mAdapter.notifyDataSetChanged();
	}
}
