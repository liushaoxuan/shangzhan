package com.wyu.iwork.wheeldate;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyu.iwork.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 日期选择对话框
 * 
 * @author ywl
 *
 */
public class ChangeDateDialog extends Dialog implements View.OnClickListener {

	private Context context;
	private WheelView wvYear;
	private WheelView wvMonth;
	private WheelView wvDay;
	private WheelView wvHour;
	private WheelView wvMin;

	private View vChangeBirth;
	private View vChangeBirthChild;
	private TextView btnSure;
	private TextView btnCancel;
	private TextView Title;

	private ArrayList<String> arry_years = new ArrayList<String>();
	private ArrayList<String> arry_months = new ArrayList<String>();
	private ArrayList<String> arry_days = new ArrayList<String>();
	private ArrayList<String> arry_hour = new ArrayList<String>();
	private ArrayList<String> arry_min = new ArrayList<String>();
	private CalendarTextAdapter mYearAdapter;
	private CalendarTextAdapter mMonthAdapter;
	private CalendarTextAdapter mDaydapter;
	private CalendarTextAdapter mHourdapter;
	private CalendarTextAdapter mMindapter;

	private int month=12;
	private int day;
	private int hour=23;
	private int min=59;

	private int currentYear = getYear();
	private int currentMonth = 1;
	private int currentDay = 1;
	private int currentHour=0;
	private int currentMin=0;

	private int maxTextSize = 16;
	private int minTextSize = 10;

	private boolean issetdata = false;

	private String selectYear;
	private String selectMonth;
	private String selectDay;
	private String selectHour;
	private String selectMin;

	private OnBirthListener onBirthListener;

	public ChangeDateDialog(Context context) {
		super(context, R.style.ShareDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_myinfo_changedate);
		wvYear = (WheelView) findViewById(R.id.wv_date_year);
		wvMonth = (WheelView) findViewById(R.id.wv_date_month);
		wvDay = (WheelView) findViewById(R.id.wv_date_day);
		wvHour= (WheelView) findViewById(R.id.wv_date_hou);
		wvMin= (WheelView) findViewById(R.id.wv_date_min);

		vChangeBirth = findViewById(R.id.ly_myinfo_changedate);
		vChangeBirthChild = findViewById(R.id.ly_myinfo_changedate_child);
		btnSure = (TextView) findViewById(R.id.btn_date_sure);
		btnCancel = (TextView) findViewById(R.id.btn_date_cancel);

		vChangeBirth.setOnClickListener(this);
		vChangeBirthChild.setOnClickListener(this);
		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		if (!issetdata) {
			initData();
		}
		initYears();
		mYearAdapter = new CalendarTextAdapter(context, arry_years, setYear(currentYear), maxTextSize, minTextSize);
		wvYear.setVisibleItems(5);
		wvYear.setViewAdapter(mYearAdapter);
		wvYear.setCurrentItem(setYear(currentYear));

		initMonths(month);
		mMonthAdapter = new CalendarTextAdapter(context, arry_months, setMonth(currentMonth), maxTextSize, minTextSize);
		wvMonth.setVisibleItems(5);
		wvMonth.setViewAdapter(mMonthAdapter);
		wvMonth.setCurrentItem(setMonth(currentMonth));

		initDays(day);
		mDaydapter = new CalendarTextAdapter(context, arry_days, currentDay - 1, maxTextSize, minTextSize);
		wvDay.setVisibleItems(5);
		wvDay.setViewAdapter(mDaydapter);
		wvDay.setCurrentItem(currentDay - 1);

		initHour(hour);
		mHourdapter=new CalendarTextAdapter(context,arry_hour,currentHour,maxTextSize,minTextSize);
		wvHour.setVisibleItems(5);
		wvHour.setViewAdapter(mHourdapter);
		wvHour.setCurrentItem(currentHour);

		initMin(min);
		mMindapter=new CalendarTextAdapter(context,arry_min,currentMin,maxTextSize,minTextSize);
		wvMin.setVisibleItems(5);
		wvMin.setViewAdapter(mMindapter);
		wvMin.setCurrentItem(currentMin);

		wvYear.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
				selectYear = currentText;
				setTextviewSize(currentText, mYearAdapter);
				currentYear = Integer.parseInt(currentText);
				setYear(currentYear);
				initMonths(month);
				mMonthAdapter = new CalendarTextAdapter(context, arry_months, 0, maxTextSize, minTextSize);
				wvMonth.setVisibleItems(5);
				wvMonth.setViewAdapter(mMonthAdapter);
				wvMonth.setCurrentItem(0);
			}
		});

		wvYear.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mYearAdapter);
			}
		});

		wvMonth.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
				selectMonth = currentText;
				setTextviewSize(currentText, mMonthAdapter);
				setMonth(Integer.parseInt(currentText));
				initDays(day);
				mDaydapter = new CalendarTextAdapter(context, arry_days, 0, maxTextSize, minTextSize);
				wvDay.setVisibleItems(5);
				wvDay.setViewAdapter(mDaydapter);
				wvDay.setCurrentItem(0);
			}
		});

		wvMonth.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mMonthAdapter);
			}
		});

		wvDay.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mDaydapter);
				selectDay = currentText;
			}
		});

		wvDay.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mDaydapter);
			}
		});

		wvHour.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String currentText = (String) mHourdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mHourdapter);
				selectHour = currentText;
			}
		});
		wvHour.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String currentText = (String) mHourdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mHourdapter);

			}
		});
		wvMin.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String currentText = (String) mMindapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mMindapter);
				selectMin = currentText;
			}
		});
		wvMin.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String currentText = (String) mMindapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mMindapter);

			}
		});

	}


	public void initYears() {
		for (int i =2000; i < 2051; i++) {
			arry_years.add(i + "");
		}
	}

	public void initMonths(int months) {
		arry_months.clear();
		for (int i = 1; i <= months; i++) {
			arry_months.add(i + "");
		}
	}

	public void initDays(int days) {
		arry_days.clear();
		for (int i = 1; i <= days; i++) {
			arry_days.add(i + "");
		}
	}

	public void initHour(int huor){
		arry_hour.clear();
		for (int i=0;i<=huor;i++){
			arry_hour.add(i+"");
		}
	}

	public void initMin(int min){
		arry_min.clear();
		for (int i=0;i<=min;i++){
			arry_min.add(i+"");
		}
	}

	private class CalendarTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected CalendarTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
			this.list = list;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index) + "";
		}
	}

	public void setBirthdayListener(OnBirthListener onBirthListener) {
		this.onBirthListener = onBirthListener;
	}

	@Override
	public void onClick(View v) {

		if (v == btnSure) {
			if (onBirthListener != null) {
				onBirthListener.onClick(selectYear, selectMonth, selectDay,selectHour,selectMin);
			}
		} else if (v == btnSure) {

		} else if (v == vChangeBirthChild) {
			return;
		} else {
			dismiss();
		}
		dismiss();

	}

	public interface OnBirthListener {
		public void onClick(String year, String month, String day,String hour,String Mmin);
	}

	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(String curriteItemText, CalendarTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(maxTextSize);
			} else {
				textvew.setTextSize(minTextSize);
			}
		}
	}

	public int getYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}

	public int getMonth() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MONTH) + 1;
	}

	public int getDay() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DATE);
	}

	public int getHour() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.HOUR);
	}

	public int getMin() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MINUTE);
	}

	public void initData() {
		setDate(getYear(), getMonth(), getDay(),getHour(),getMin());
		this.currentDay = 1;
		this.currentMonth = 1;
		this.currentHour=0;
		this.currentMin=0;
	}

	/**
	 * 设置年月日
	 * 
	 * @param year
	 * @param month
	 * @param day
	 */
	public void setDate(int year, int month, int day,int hour,int min) {
		selectYear = year + "";
		selectMonth = month + "";
		selectDay = day + "";
		selectHour=hour+"";
		selectMin=min+"";
		issetdata = true;
		this.currentYear = getYear();
		this.currentMonth = month;
		this.currentDay = day;
		this.currentHour=hour;
		this.currentMin=min;
//		if (year == getYear()) {
//			this.month = getMonth();
//		} else {
//		this.month = 12;
//		}
		calDays(year, month);
	}

	/**
	 * 设置年份
	 * 
	 * @param year
	 */
	public int setYear(int year) {
		int yearIndex = 0;
//		if (year != getYear()) {
//			this.month = 12;
//		} else {
//			this.month = getMonth();
//		}
		for (int i = 2000; i <2051; i++) {
			if (i == year) {
				return yearIndex;
			}
			yearIndex++;
		}
		return yearIndex;
	}

	/**
	 * 设置月份

	 * @param month
	 * @return
	 */
	public int setMonth(int month) {
		int monthIndex = 0;
		calDays(currentYear, month);
		for (int i = 1; i < this.month; i++) {
			if (month == i) {
				return monthIndex;
			} else {
				monthIndex++;
			}
		}
		return monthIndex;
	}



	/**
	 * 计算每月多少天
	 * 
	 * @param month
	 */
	public void calDays(int year, int month) {
		boolean leayyear = false;
		if (year % 4 == 0 && year % 100 != 0) {
			leayyear = true;
		}else if(year % 400 == 0){
			leayyear = true;
		} else {
			leayyear = false;
		}
		for (int i = 1; i <= 12; i++) {
			switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				this.day = 31;
				break;
			case 2:
				if (leayyear) {
					this.day = 29;
				} else {
					this.day = 28;
				}
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				this.day = 30;
				break;
			}
		}
//		if (year == getYear() && month == getMonth()) {
//			this.day = getDay();
//		}
	}

//	/**
//	 * 设置小时
//	 * @param hour
//	 * @return
//	 */
//	public int setHour(int hour) {
//		int hourIndex = 0;
//		for (int i = 1; i < this.hour; i++) {
//			if (hour == i) {
//				return hourIndex;
//			} else {
//				hourIndex++;
//			}
//		}
//		return hourIndex;
//	}
//
//	/**
//	 * 设置分钟
//	 * @param min
//	 * @return
//	 */
//	public int setMin(int min) {
//		int MinIndex = 0;
//		for (int i = 1; i < this.min; i++) {
//			if (hour == i) {
//				return MinIndex;
//			}
//		}
//		return MinIndex;
//	}

	public void setTitle(String str){
		Title=(TextView)findViewById(R.id.tv_date_title) ;
		Title.setText(str);
	}
}