package com.wyu.iwork.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.DepartmentModel;

import java.util.List;

/**
 * Created by lx on 2017/4/18.
 */

public class SpinnerAdapter extends BaseAdapter {
    private Context mcontext;
    private List<DepartmentModel> list;
    public static interface IOnItemSelectListener{
        public void onItemClick(int pos);
    };

    public SpinnerAdapter( Context mcontext,List<DepartmentModel> list) {
        this.list = list;
        this.mcontext = mcontext;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_spinner,parent,false);
        TextView textView = (TextView) view.findViewById(R.id.item_spinner_textView);
        textView.setText(list.get(position).getDepartment());
        return view;
    }
}
