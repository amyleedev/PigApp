package com.szmy.pigapp.utils;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.szmy.pigapp.R;

import java.util.Calendar;

public class DateTimePickerDialog extends AlertDialog implements OnClickListener,
        OnDateChangedListener, OnTimeChangedListener{

	private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAY = "day";
    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";
    private static final String IS_24_HOUR = "is24hour";

    private final DatePicker mDatePicker;
    private final TimePicker mTimePicker;
    
    private final OnDateSetListener mCallBack;
    private final Calendar mCalendar;
    private int mInitialHourOfDay;
    private int mInitialMinute;
    boolean mIs24HourView;
    private Context mContext;
    private boolean mTitleNeedsUpdate = true;
	
    public interface OnDateSetListener {
        void onDateSet(DatePicker dataview, int year, int monthOfYear, int dayOfMonth,
                       TimePicker timeview, int hourOfDay, int minute);
    }
    
	public DateTimePickerDialog(Context context,
            OnDateSetListener callBack,int year,int monthOfYear,int dayOfMonth,
            int hourOfDay, int minute, boolean is24HourView) {
        this(context, 0, callBack, year, monthOfYear, dayOfMonth, hourOfDay, minute, is24HourView);
    }
	
	public DateTimePickerDialog(Context context,int theme,OnDateSetListener callBack,int year,
            int monthOfYear,int dayOfMonth,
            int hourOfDay, int minute, boolean is24HourView) {
        super(context, theme);
        mInitialHourOfDay = hourOfDay;
        mInitialMinute = minute;
        mIs24HourView = is24HourView;
        mCallBack = callBack;
        mContext = context;
        mCalendar = Calendar.getInstance();

//        Context themeContext = getContext();
        setButton(BUTTON_POSITIVE, mContext.getText(R.string.confirm), this);
        setIcon(0);
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_time_data_select, null);
        setView(view);
        mDatePicker = (DatePicker) view.findViewById(R.id.dialog_data_picker);
        mDatePicker.init(year, monthOfYear, dayOfMonth, this);
        
        mTimePicker = (TimePicker) view.findViewById(R.id.dialog_time_picker);

        // initialize state
        mTimePicker.setIs24HourView(mIs24HourView);
        mTimePicker.setCurrentHour(mInitialHourOfDay);
        mTimePicker.setCurrentMinute(mInitialMinute);
        mTimePicker.setOnTimeChangedListener(this);
        updateTitle(year, monthOfYear, dayOfMonth);
    }
	
	private void updateTitle(int year, int month, int day) {
        if (!mDatePicker.getCalendarViewShown()) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, month);
            mCalendar.set(Calendar.DAY_OF_MONTH, day);
            String title = DateUtils.formatDateTime(mContext,
                    mCalendar.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_DATE
                    | DateUtils.FORMAT_SHOW_WEEKDAY
                    | DateUtils.FORMAT_SHOW_YEAR
                    | DateUtils.FORMAT_ABBREV_MONTH
                    | DateUtils.FORMAT_ABBREV_WEEKDAY);
            setTitle(title);
            mTitleNeedsUpdate = true;
        } else {
            if (mTitleNeedsUpdate) {
                mTitleNeedsUpdate = false;
                setTitle(R.string.confirm);
            }
        }
    }
	
	 public void updateDate(int year, int monthOfYear, int dayOfMonth,int hourOfDay, int minutOfHour) {
	        mDatePicker.updateDate(year, monthOfYear, dayOfMonth);
	        mTimePicker.setCurrentHour(hourOfDay);
	        mTimePicker.setCurrentMinute(minutOfHour);
	    }

	private void tryNotifyDateSet() {
		if (mCallBack != null) {
			mDatePicker.clearFocus();
			mTimePicker.clearFocus();
			mCallBack.onDateSet(mDatePicker, mDatePicker.getYear(),
					mDatePicker.getMonth(), mDatePicker.getDayOfMonth(),
					mTimePicker, mTimePicker.getCurrentHour(),
                    mTimePicker.getCurrentMinute());
		}
	}
	
	@Override
	protected void onStop() {
		tryNotifyDateSet();
		super.onStop();
	}
	
	@Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(YEAR, mDatePicker.getYear());
        state.putInt(MONTH, mDatePicker.getMonth());
        state.putInt(DAY, mDatePicker.getDayOfMonth());
        state.putInt(HOUR, mTimePicker.getCurrentHour());
        state.putInt(MINUTE, mTimePicker.getCurrentMinute());
        state.putBoolean(IS_24_HOUR, mTimePicker.is24HourView());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int year = savedInstanceState.getInt(YEAR);
        int month = savedInstanceState.getInt(MONTH);
        int day = savedInstanceState.getInt(DAY);
//        mDatePicker.init(year, month, day, this);
        
        int hour = savedInstanceState.getInt(HOUR);
        int minute = savedInstanceState.getInt(MINUTE);
        mTimePicker.setIs24HourView(savedInstanceState.getBoolean(IS_24_HOUR));
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);
    }

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		tryNotifyDateSet();
	}

	@Override
	public void onDateChanged(DatePicker view, int year,
            int month, int day) {
		mDatePicker.init(year, month, day, this);
		updateTitle(year, month, day);
	}

	@Override
	public void onTimeChanged(TimePicker arg0, int arg1, int arg2) {
	}
}