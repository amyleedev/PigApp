package com.szmy.pigapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.BitmapUtils;
import com.szmy.pigapp.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 显示图片
 * 
 * @author Amy
 * 
 */
public class ShowImgActivity extends BaseActivity {

	private ArrayList<View> listViews = null;
	private ViewPager pager;
	private MyPageAdapter adapter;
	private int count;

	public ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
	public ArrayList<String> drr = new ArrayList<String>();
	public List<String> del = new ArrayList<String>();
	public int max;

	RelativeLayout photo_relativeLayout;

	public ArrayList<String> biglist = new ArrayList<String>();
	private BitmapUtils bitmapUtils;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_photo);

		photo_relativeLayout = (RelativeLayout) findViewById(R.id.photo_relativeLayout);
		photo_relativeLayout.setBackgroundColor(0x70000000);
		pager = (ViewPager) findViewById(R.id.viewpager);
		pager.setOnPageChangeListener(pageChangeListener);
		Button photo_bt_exit = (Button) findViewById(R.id.photo_bt_exit);
		photo_bt_exit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				finish();
			}
		});
		Button photo_bt_del = (Button) findViewById(R.id.photo_bt_del);
		Button photo_bt_enter = (Button) findViewById(R.id.photo_bt_enter);
		Intent intent = getIntent();
		int id = intent.getIntExtra("ID", 0);
		String flag = intent.getStringExtra("flag");
		if (flag.equals("false")) { // ֻ�鿴�������κβ���
			bitmapUtils = new BitmapUtils(this);
			photo_bt_del.setVisibility(View.GONE);
			photo_bt_enter.setVisibility(View.VISIBLE);
			biglist = intent.getStringArrayListExtra("urllist");
			for (int i = 0; i < biglist.size(); i++) {
				initImageView(biglist.get(i));
			}

		} else {

		}
		photo_bt_enter.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
		adapter = new MyPageAdapter(listViews);// ����adapter
		pager.setAdapter(adapter);// ����������

		pager.setCurrentItem(id);
	}

	@SuppressWarnings("deprecation")
	private void initListViews(Bitmap bm) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		ImageView img = new ImageView(this);// ����textView����
		img.setBackgroundColor(0xff000000);
		img.setImageBitmap(bm);
		img.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		listViews.add(img);// ���view
	}

	@SuppressWarnings("deprecation")
	private void initImageView(String url) {

		if (listViews == null)
			listViews = new ArrayList<View>();
		ImageView img = new ImageView(this);// ����textView����
		img.setBackgroundColor(0xff000000);
//		Matrix matrix = new Matrix();
//	    matrix.setSkew(0.5f, 0);
//		img.setImageMatrix(matrix); 
		img.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		bitmapUtils.display(img, url);
		listViews.add(img);
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {// ҳ��ѡ����Ӧ����
			count = arg0;
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {// �����С�����

		}

		public void onPageScrollStateChanged(int arg0) {// ����״̬�ı�

		}
	};

	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;// content

		private int size;// ҳ��

		public MyPageAdapter(ArrayList<View> listViews) {// ���캯��
															// ��ʼ��viewpager��ʱ����һ��ҳ��
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {// �Լ�д��һ����������������
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {// ��������
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {// ���view����
			((ViewPager) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {// ����view����
			try {
				((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
}
