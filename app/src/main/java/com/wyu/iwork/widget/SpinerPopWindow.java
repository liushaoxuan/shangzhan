package com.wyu.iwork.widget;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.adapter.SpinnerAdapter;
import com.wyu.iwork.model.DepartmentModel;
import com.wyu.iwork.view.activity.BuildDepartmentActivity;

public class SpinerPopWindow extends PopupWindow implements OnItemClickListener{

	private Context mContext;
	private ListView listView;
	private SpinnerAdapter adapter;
	private List<DepartmentModel> list;
	private TextView textView;

	private int index = 0;
	
	public SpinerPopWindow(Context context,List<DepartmentModel> data,TextView textView)
	{
		super(context);
		mContext = context;
		list = data;
		this.textView = textView;
		if (list==null){
			list = new ArrayList<>();
		}
		init();
	}


	
	private void init()
	{
		View view = LayoutInflater.from(mContext).inflate(R.layout.pop_department_build_edite, null);
		setContentView(view);		
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
    	ColorDrawable dw = new ColorDrawable(0x00);
		setBackgroundDrawable(dw);
		listView = (ListView)view.findViewById(R.id.pop_department_build_edit_recyclerview);
		adapter = new SpinnerAdapter(mContext,list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
		if (textView!=null){
			textView.setText(list.get(pos).getDepartment());
			index = pos;
		}
		dismiss();
	}


	public int getIndex(){
		return index;
	}
	
}
