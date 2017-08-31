package com.szmy.pigapp.image;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.szmy.pigapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 图片选择
 * 
 */
public class ImageChooseActivity extends Activity
{
	private List<ImageItem> mDataList = new ArrayList<ImageItem>();
	private String mBucketName;
	private int availableSize;
	private GridView mGridView;
	private TextView mBucketNameTv;
	private TextView cancelTv;
	private ImageGridAdapter mAdapter;
	private Button mFinishBtn;
	private HashMap<String, ImageItem> selectedImgs = new HashMap<String, ImageItem>();
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_image_grid);

		mDataList = (List<ImageItem>) getIntent().getSerializableExtra(
				CustomConstants.EXTRA_IMAGE_LIST);
		if (mDataList == null) mDataList = new ArrayList<ImageItem>();
		mBucketName = getIntent().getStringExtra(
				CustomConstants.EXTRA_BUCKET_NAME);

		if (TextUtils.isEmpty(mBucketName))
		{
			mBucketName = "请选择";
		}
		availableSize = getIntent().getIntExtra(
				CustomConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
				CustomConstants.MAX_IMAGE_SIZE);

		initView();
		initListener();
		
	}

	private void initView()
	{
		mBucketNameTv = (TextView) findViewById(R.id.title);
		mBucketNameTv.setText(mBucketName);

		mGridView = (GridView) findViewById(R.id.gridview);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mAdapter = new ImageGridAdapter(ImageChooseActivity.this, mDataList);
		mGridView.setAdapter(mAdapter);
		mFinishBtn = (Button) findViewById(R.id.finish_btn);
		cancelTv = (TextView) findViewById(R.id.action);
		
		
		mFinishBtn.setText("完成" + "(" + selectedImgs.size() + "/"
				+ availableSize + ")");
		mAdapter.notifyDataSetChanged();
	}

	private void initListener()
	{
		mFinishBtn.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{
//				
//				Intent intent = new Intent(ImageChooseActivity.this,
//						PublishActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putSerializable(IntentConstants.EXTRA_IMAGE_LIST,
//						(Serializable) new ArrayList<ImageItem>(selectedImgs
//								.values()));
//				bundle.putString("type", getIntent().getStringExtra("type"));
//
//				intent.putExtras(bundle);
//				startActivity(intent);
				mDataList.clear();
				mDataList.addAll(new ArrayList<ImageItem>(selectedImgs.values()));
				SharedPreferences sp = getSharedPreferences(
						CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
				
				int type = getIntent().getIntExtra("type", 0);
				if(type == CustomConstants.TAKE_PHOTO){
					String prefStr1 = JSON.toJSONString(mDataList);
				sp.edit().putString(CustomConstants.PREF_TEMP_IMAGES1, prefStr1)
						.commit();
				}else if(type == CustomConstants.TAKE_PHOTO2){
					String prefStr1 = JSON.toJSONString(mDataList);
				sp.edit().putString(CustomConstants.PREF_TEMP_IMAGES2, prefStr1)
						.commit();
				}else {
					 List<ImageItem> mOldDataList = new ArrayList<ImageItem>();
					 String prefStr = sp.getString(CustomConstants.PREF_TEMP_IMAGES, null);
						if (!TextUtils.isEmpty(prefStr))
						{
							List<ImageItem> tempImages = JSON.parseArray(prefStr,
									ImageItem.class);
							mOldDataList = tempImages;
							
						}
						mOldDataList.addAll(mDataList);
						String prefStr1 = JSON.toJSONString(mOldDataList);
						sp.edit().putString(CustomConstants.PREF_TEMP_IMAGES, prefStr1)
						.commit();	
				}

				finish();
			}

		});

		mGridView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{

				ImageItem item = mDataList.get(position);
				if (item.isSelected)
				{
					item.isSelected = false;
					selectedImgs.remove(item.imageId);
				}
				else
				{
					if (selectedImgs.size() >= availableSize)
					{
						Toast.makeText(ImageChooseActivity.this,
								"最多选择" + availableSize + "张图片",
								Toast.LENGTH_SHORT).show();
						return;
					}
					item.isSelected = true;
					selectedImgs.put(item.imageId, item);
				}

				mFinishBtn.setText("完成" + "(" + selectedImgs.size() + "/"
						+ availableSize + ")");
				mAdapter.notifyDataSetChanged();
			}

		});

		cancelTv.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
//				Intent intent = new Intent(ImageChooseActivity.this,
//						PublishActivity.class);
//				startActivity(intent);
				finish();
			}
		});

	}
}