package com.szmy.pigapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;

import com.szmy.pigapp.R;

import java.util.ArrayList;
import java.util.List;
/**
 * 标签容器布局
 *
 */
public class SingleTagView extends SingleLayout implements OnClickListener{

	private boolean mIsDeleteMode;
	private OnTagCheckedChangedListener mOnTagCheckedChangedListener;
	private OnTagClickListener mOnTagClickListener;
	private int mTagViewBackgroundResId;
	private int mTagViewTextColorResId;
	private final List<Tag> mTags = new ArrayList<Tag>();
	private Boolean isSingle;

	/**
	 * @param context
	 */
	public SingleTagView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	/**
	 * @param context
	 * @param attributeSet
	 */
	public SingleTagView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		// TODO Auto-generated constructor stub
		init();
	}

	/**
	 * @param context
	 * @param attributeSet
	 * @param defStyle
	 */
	public SingleTagView(Context context, AttributeSet attributeSet, int defStyle) {
		super(context, attributeSet, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public Boolean getIsSingle() {
		return isSingle;
	}

	public void setIsSingle(Boolean isSingle) {
		this.isSingle = isSingle;
	}

	@Override
	public void onClick(View v) {
		if ((v instanceof TagView)) {
			Tag localTag = (Tag) v.getTag();
			if (this.mOnTagClickListener != null) {
				if(getIsSingle()){
					changeAll();
				}
				this.mOnTagClickListener.onTagClick((TagView) v, localTag, getIsSingle());
			}
		}
	}

	private void init() {

	}

	private void inflateTagView(final Tag t, boolean b) {

		TagView localTagView = (TagView) View.inflate(getContext(), R.layout.item_select_time_tag, null);
		localTagView.setText(t.getTitle());
		localTagView.setTag(t);

		if (mTagViewTextColorResId <= 0) {
			int c = getResources().getColor(R.color.black);
			localTagView.setTextColor(c);

		}

		if (mTagViewBackgroundResId <= 0) {
			mTagViewBackgroundResId = R.drawable.btn_tag;
			localTagView.setBackgroundResource(mTagViewBackgroundResId);
		}

		localTagView.setChecked(t.isChecked());
		localTagView.setCheckEnable(b);
		if (mIsDeleteMode) {
			int k = (int) TypedValue.applyDimension(1, 5.0F, getContext().getResources().getDisplayMetrics());
			localTagView.setPadding(localTagView.getPaddingLeft(), localTagView.getPaddingTop(), k, localTagView.getPaddingBottom());
			localTagView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.cancel_new, 0);
		}
		if (t.getBackgroundResId() > 0) {
			localTagView.setBackgroundResource(t.getBackgroundResId());
		}
		if ((t.getLeftDrawableResId() > 0) || (t.getRightDrawableResId() > 0)) {
			localTagView.setCompoundDrawablesWithIntrinsicBounds(t.getLeftDrawableResId(), 0, t.getRightDrawableResId(), 0);
		}
		localTagView.setOnClickListener(this);
		localTagView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean) {
				
				
				changeAll();
				t.setChecked(paramAnonymousBoolean);
				if (SingleTagView.this.mOnTagCheckedChangedListener != null) {
					SingleTagView.this.mOnTagCheckedChangedListener.onTagCheckedChanged((TagView) paramAnonymousCompoundButton, t);
				}
			}
		});
		addView(localTagView);
	}
	
	public void changeAll(){
		int len =getChildCount();
		View child;
		for(int i = 0;i<len;i++){
			child = getChildAt(i);
			if(child instanceof TagView){
				child.setBackgroundResource(R.drawable.my_border);
			}
		}
	}

	public void addTag(int i, String s,String storc,String price) {
		addTag(i, s,storc,price, false);
	}

	public void addTag(int i, String s,String storc,String price, boolean b) {
		addTag(new Tag(i, s,storc,price), b);
	}

	public void addTag(Tag tag) {
		addTag(tag, false);
	}

	public void addTag(Tag tag, boolean b) {
		mTags.add(tag);
		inflateTagView(tag, b);
	}

	public void addTags(List<Tag> lists) {
		addTags(lists, false);
	}

	public void addTags(List<Tag> lists, boolean b) {
		for (int i = 0; i < lists.size(); i++) {
			addTag((Tag) lists.get(i), b);
		}
	}

	public List<Tag> getTags() {
		return mTags;
	}

	public View getViewByTag(Tag tag) {
		return findViewWithTag(tag);
	}

	public void removeTag(Tag tag) {
		mTags.remove(tag);
		removeView(getViewByTag(tag));
	}

	public void setDeleteMode(boolean b) {
		mIsDeleteMode = b;
	}

	public void setOnTagCheckedChangedListener(OnTagCheckedChangedListener onTagCheckedChangedListener) {
		mOnTagCheckedChangedListener = onTagCheckedChangedListener;
	}

	public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
		mOnTagClickListener = onTagClickListener;
	}

	public void setTagViewBackgroundRes(int res) {
		mTagViewBackgroundResId = res;
	}

	public void setTagViewTextColorRes(int res) {
		mTagViewTextColorResId = res;
	}

	public void setTags(List<? extends Tag> lists) {
		setTags(lists, false);
	}

	public void setTags(List<? extends Tag> lists, boolean b) {
		removeAllViews();
		mTags.clear();
		for (int i = 0; i < lists.size(); i++) {
			addTag((Tag) lists.get(i), b);
		}
	}

	public static abstract interface OnTagCheckedChangedListener {
		public abstract void onTagCheckedChanged(TagView tagView, Tag tag);
	}

	public static abstract interface OnTagClickListener {
		public abstract void onTagClick(TagView tagView, Tag tag, boolean isSinggle);
	}

}
