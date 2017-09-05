package com.wyu.iwork.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyu.iwork.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by lx on 2016/12/21.
 */

public class TaskTagAdapter extends BaseAdapter {
    private ArrayList list=new ArrayList();
    private String []item={"A","B","C","D"};
    private Context context;
    private ViewHodel vh;

    public TaskTagAdapter(ArrayList<Map<String,String>> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public int getCount() {
        return list.size();
//        return item.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        vh=new ViewHodel();
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.item_tasktag,viewGroup,false);
            vh.tag=(TextView)view.findViewById(R.id.item_taskTag);
            view.setTag(vh);
        }else {
            vh=(ViewHodel)view.getTag();
        }
        vh.tag.setText(list.get(i).toString());
//        vh.tag.setText(item[i]);
        return view;
    }
    class ViewHodel{
        TextView tag;
        ImageView delete;
    }

}
