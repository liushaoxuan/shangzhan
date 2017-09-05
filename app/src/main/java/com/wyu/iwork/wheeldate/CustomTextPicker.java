package com.wyu.iwork.wheeldate;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyu.iwork.R;

import java.util.ArrayList;

/**
 * 日期选择对话框 -- 时分选择器
 * 
 * @author ywl
 *
 */
public class CustomTextPicker extends Dialog implements View.OnClickListener {

	private Context context;
	private WheelView wvMin;

	private View vChangeBirth;
	private View vChangeBirthChild;
	private TextView btnSure;

	private String[] arry ;
	private CalendarTextAdapter mMindapter;

	private int currentMin=1;

	private int maxTextSize = 18;
	private int minTextSize = 14;

	private boolean issetdata = false;
	private String selectMin;

	private OnBirthListener onBirthListener;

	public CustomTextPicker(Context context, String[] arry) {
		super(context, R.style.ShareDialog);
		this.context = context;
		this.arry = arry;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_crm_bottom_style);

		wvMin= (WheelView) findViewById(R.id.wv_date_min);

		vChangeBirth = findViewById(R.id.ly_myinfo_changedate);
		vChangeBirthChild = findViewById(R.id.ly_myinfo_changedate_child);
		btnSure = (TextView) findViewById(R.id.tv_sure);

		vChangeBirth.setOnClickListener(this);
		vChangeBirthChild.setOnClickListener(this);
		btnSure.setOnClickListener(this);


		if(arry.length>=3){
			currentMin = 1;
		}else{
			currentMin = 0;
		}
		if (!issetdata) {
			initData();
		}
		mMindapter=new CalendarTextAdapter(context,arry,currentMin,maxTextSize,minTextSize);
		wvMin.setVisibleItems(3);
		wvMin.setViewAdapter(mMindapter);
		wvMin.setCurrentItem(currentMin);

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


	private class CalendarTextAdapter extends AbstractWheelTextAdapter {
		String[] arr;

		protected CalendarTextAdapter(Context context, String[] arr, int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_crm_text, NO_RESOURCE, currentItem, maxsize, minsize);
			this.arr = arr;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return arr.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return arr[index] + "";
		}
	}

	public void setBirthdayListener(OnBirthListener onBirthListener) {
		this.onBirthListener = onBirthListener;
	}

	@Override
	public void onClick(View v) {

		if (v == btnSure) {
			if (onBirthListener != null) {
				onBirthListener.onClick(selectMin);
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
		public void onClick(String Mmin);
	}

	private void initData(){
		selectMin = arry[currentMin];
		issetdata = true;
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

	public void setTitle(String str){
		//Title=(TextView)findViewById(R.id.tv_date_title) ;
		//Title.setText(str);
	}
}