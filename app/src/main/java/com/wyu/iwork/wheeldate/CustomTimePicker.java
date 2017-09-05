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
 * 日期选择对话框 -- 时分选择器
 * 
 * @author ywl
 *
 */
public class CustomTimePicker extends Dialog implements View.OnClickListener {

	private Context context;
	private WheelView wvHour;
	private WheelView wvMin;

	private View vChangeBirth;
	private View vChangeBirthChild;
	private TextView btnSure;
	private TextView btnCancel;
	private TextView Title;

	private ArrayList<String> arry_days = new ArrayList<String>();
	private ArrayList<String> arry_hour = new ArrayList<String>();
	private ArrayList<String> arry_min = new ArrayList<String>();
	private CalendarTextAdapter mDaydapter;
	private CalendarTextAdapter mHourdapter;
	private CalendarTextAdapter mMindapter;

	private int month=12;
	private int day;
	private int hour=23;
	private int min=59;

	private int currentYear = getYear();
	private int currentDay = 1;
	private int currentHour=0;
	private int currentMin=0;

	private int maxTextSize = 18;
	private int minTextSize = 14;

	private boolean issetdata = false;

	private String selectYear;
	private String selectMonth;
	private String selectDay;
	private String selectHour;
	private String selectMin;

	private OnBirthListener onBirthListener;

	public CustomTimePicker(Context context) {
		super(context, R.style.ShareDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_time);

		wvHour= (WheelView) findViewById(R.id.wv_date_hour);
		wvMin= (WheelView) findViewById(R.id.wv_date_min);

		vChangeBirth = findViewById(R.id.ly_myinfo_changedate);
		vChangeBirthChild = findViewById(R.id.ly_myinfo_changedate_child);
		btnSure = (TextView) findViewById(R.id.tv_sure);
		btnCancel = (TextView) findViewById(R.id.tv_cancel);

		vChangeBirth.setOnClickListener(this);
		vChangeBirthChild.setOnClickListener(this);
		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		if (!issetdata) {
			initData();
		}
		initHour(hour);
		mHourdapter=new CalendarTextAdapter(context,arry_hour,currentHour,maxTextSize,minTextSize);
		wvHour.setVisibleItems(3);
		wvHour.setViewAdapter(mHourdapter);
		wvHour.setCurrentItem(currentHour);

		initMin(min);
		mMindapter=new CalendarTextAdapter(context,arry_min,currentMin,maxTextSize,minTextSize);
		wvMin.setVisibleItems(3);
		wvMin.setViewAdapter(mMindapter);
		wvMin.setCurrentItem(currentMin);

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
				onBirthListener.onClick(selectYear, selectDay,selectHour,selectMin);
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
		public void onClick(String year, String day, String hour, String Mmin);
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
		//selectDay = day + "";
		selectDay = month+"月"+day+"日";
		selectHour=hour+"";
		selectMin=min+"";
		issetdata = true;
		this.currentYear = getYear();
		this.currentDay = day;
		this.currentHour=hour;
		this.currentMin=min;
		calDays(year, month);
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
		} else if(year % 400 == 0){
			leayyear = true;
		}else {
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
	}

	public void setTitle(String str){
		//Title=(TextView)findViewById(R.id.tv_date_title) ;
		//Title.setText(str);
	}
}