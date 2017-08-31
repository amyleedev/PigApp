package com.szmy.pigapp.activity;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.adapter.ColumnAdapter;
import com.szmy.pigapp.fragment.NewsFragment;
import com.szmy.pigapp.fragment.NewsPriceFragment;
import com.szmy.pigapp.widget.TabPageIndicator;

public class NewsActivity extends BaseActivity {
	private ImageView mIvType;
	/** DrawerLayout */
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mToggle;// 菜单是否打开状态开关
	/** 右边栏 */
	private GridView mGvRight;;
	private boolean mIsMenuOpen = false;
	private TabPageIndicator indicator;
	private ColumnAdapter mAapter;
	/**
	 * Tab标题
	 */
	// private static final String[] TITLE = new String[] { "今日要闻", "市场分析",
	// "行业点评", "养猪政策", "养猪会讯", "生猪价格", "仔猪价格", "猪肉价格", "杂粕价格", "添加剂价",
	// "饲料", "玉米价格", "豆粕价格", "鱼粉价格", "大豆价格", "种猪", "兽药", "设备", "疫苗" };
//	private static final String[] TITLE = new String[] { "今日要闻", "价格行情",
//			"笑话大全", "市场分析", "行业点评", "养猪政策", "养猪会讯", "饲料", "种猪", "兽药", "设备" };
	private static final String[] TITLE = new String[] { "今日要闻", "价格行情",
			 "市场分析", "行业点评", "养猪政策", "养猪会讯", "饲料", "种猪", "兽药", "设备" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		mIvType = (ImageView) findViewById(R.id.iv_type);
		mIvType.setOnClickListener(mClickListener);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.down, R.string.app_name, R.string.app_name) {
			public void onDrawerClosed(View view) {
				mIsMenuOpen = false;
			}
			public void onDrawerOpened(View drawerView) {
				mIsMenuOpen = true;
				if (mAapter == null) {
					mAapter = new ColumnAdapter(NewsActivity.this, TITLE,
							indicator.getCurrentItem());
					mGvRight.setAdapter(mAapter);
				} else {
					mAapter.setIndex(indicator.getCurrentItem());
					mAapter.notifyDataSetChanged();
				}
			}
		};
		mDrawerLayout.setDrawerListener(mToggle);
		mGvRight = (GridView) findViewById(R.id.gv_right);
		mGvRight.setOnItemClickListener(mItemClickListener);
		// ViewPager的adapter
		FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(
				getSupportFragmentManager());
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		// 实例化TabPageIndicator然后设置ViewPager与之关联
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		// 如果我们要对ViewPager设置监听，用indicator设置就行了
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// Toast.makeText(getApplicationContext(), TITLE[arg0],
				// Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
			if (indicator.getCurrentItem() == position)
				return;
			indicator.setCurrentItem(position);
			mDrawerLayout.closeDrawers();
		}
	};

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.iv_type:
					if (mIsMenuOpen) {
						mDrawerLayout.closeDrawers();
					} else {
						mDrawerLayout.openDrawer(mGvRight);
					}
					break;
			}
		}
	};

	/**
	 * ViewPager适配器
	 *
	 * @author len
	 *
	 */
	class TabPageIndicatorAdapter extends FragmentPagerAdapter {
		public TabPageIndicatorAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// 新建一个Fragment来展示ViewPager item的内容，并传递参数
			Fragment fragment = null;
			if (position == 1)
				fragment = new NewsPriceFragment();
//			else if (position == 2)
//				fragment = new NewsJestFragment();
			else
				fragment = new NewsFragment();
			Bundle args = new Bundle();
			args.putString("arg", TITLE[position]);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLE[position % TITLE.length];
		}

		@Override
		public int getCount() {
			return TITLE.length;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (mIsMenuOpen) {
				mDrawerLayout.closeDrawers();
				return true;
			}
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
