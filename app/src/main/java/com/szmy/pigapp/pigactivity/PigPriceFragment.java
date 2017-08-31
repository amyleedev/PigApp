package com.szmy.pigapp.pigactivity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.widget.MyViewPager;
import com.szmy.pigapp.widget.PagerSlidingTabStrip;
import com.szmy.pigapp.widget.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

public class PigPriceFragment extends BaseActivity {

	private MyViewPager mViewPager;
	private PagerSlidingTabStrip mPagerTabStrip;
	private MyPagerAdapter mAdapter;
	private PigPriceWsyData pd1;
	PigPriceNsyData pd3;
	PigPriceTzzData pd4;
	PigPriceYmData pd5;
	PigPriceDpData pd6;
	private ArrayList<View> mlistViews;
	public static final int TYPE_ID = 22;
	// Fields
	private int mCurPos = 0;
	private String type ="";

	// Types
	private final int[] mFragmentTitles = new int[] { R.string.wsy,
			R.string.nsy, R.string.tzz, R.string.ym, R.string.dp, };

	public static String pro = "",city ="";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pigprice_fragment);
		TextView title = (TextView) findViewById(R.id.def_head_tv);
		title.setText("附近报价");
		mViewPager = (MyViewPager) findViewById(R.id.viewPager);
		mPagerTabStrip = (PagerSlidingTabStrip) findViewById(R.id.viewpager_strip);
		mlistViews = new ArrayList<View>();
		 pro = getIntent().getStringExtra("nearPro");
		 city = getIntent().getStringExtra("nearCity");
		
		pd1 = new PigPriceWsyData(PigPriceFragment.this);
		pd3 = new PigPriceNsyData(PigPriceFragment.this);
		pd4 = new PigPriceTzzData(PigPriceFragment.this);
		pd5 = new PigPriceYmData(PigPriceFragment.this);
		pd6 = new PigPriceDpData(PigPriceFragment.this);
		mlistViews.add(pd1);
		mlistViews.add(pd3);
		mlistViews.add(pd4);
		mlistViews.add(pd5);
		mlistViews.add(pd6);

		mAdapter = new MyPagerAdapter(mlistViews);
		
		mViewPager.setAdapter(mAdapter);
		
		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(mViewPager);
		 if(getIntent().hasExtra("type")){
			 type = getIntent().getStringExtra("type");
			 if(type.equals("wsy")){
				 mCurPos = 0;
				 pd1.loadData();
			 }else if(type.equals("nsy")){
				 mCurPos = 1;
				 pd3.loadData();
			 }else if(type.equals("tzz")){
				 mCurPos = 2;
				 pd4.loadData();
			 }else if(type.equals("ym")){
				 mCurPos = 3;
				 pd5.loadData();
			 }else if(type.equals("dp")){
				 mCurPos = 4;
				 pd6.loadData();
			 }
		 }else{
			 pd1.loadData();
		 }
		mViewPager.setCurrentItem(mCurPos);
		
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		


	}

	// ============================= PagerAdapter
	// ==================================//

	public class MyPagerAdapter extends PagerAdapter implements
			ViewPager.OnPageChangeListener {
		public List<View> mListViews;
		private int mChildCount = 0;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public CharSequence getPageTitle(int position) {
			String str = null;
			if (position == 0) {
				str = getString(mFragmentTitles[position]);

			} else {
				str = getString(mFragmentTitles[position]);
			}
			return str;
		}

		
		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			Log.i("position", position+"");
			
			switch (position) {
			case 0:
				pd1.loadData();

				break;
			case 1:
				pd3.loadData();
				break;
			case 2:
				pd4.loadData();
				break;
			case 3:
				pd5.loadData();

				break;
			case 4:
				pd6.loadData();
				break;

			default:

				break;
			}
		}

		@Override
		public void notifyDataSetChanged() {
			mChildCount = getCount();
			super.notifyDataSetChanged();
		}

		@Override
		public int getItemPosition(Object object) {
			if (mChildCount > 0) {
				mChildCount--;
				return POSITION_NONE;
			}
			return super.getItemPosition(object);
		}
	}

}
