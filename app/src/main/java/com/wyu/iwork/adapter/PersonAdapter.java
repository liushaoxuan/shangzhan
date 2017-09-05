package com.wyu.iwork.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wyu.iwork.R;
import com.wyu.iwork.model.PersonModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lx on 2016/12/19.
 */

public class PersonAdapter extends android.widget.BaseAdapter {
    private static final String TAG="ScheduleAdapter";
    private List<PersonModel> list=new ArrayList<>();
    private ViewHolder vh=null;
    private Context context;
    public PersonAdapter(){}
    public PersonAdapter(Context context) {
        this.context = context;
    }

    public PersonAdapter(List<PersonModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PersonModel model=list.get(i);
//        if(view==null){
            vh=new ViewHolder();
            view= LayoutInflater.from(context).inflate(R.layout.item_people_layout,viewGroup,false);
            vh.image=(CircleImageView)view.findViewById(R.id.item_headimage);
            vh.tv_name=(TextView)view.findViewById(R.id.item_username);
            vh.tv_position=(TextView)view.findViewById(R.id.item_position) ;
            vh.tv_selected=(ImageView) view.findViewById(R.id.item_selected);
            view.setTag(vh);
//        }else{
//            vh = (ViewHolder) view.getTag();
//        }
        Glide.with(context).load(model.getFace_img()).dontAnimate().placeholder(R.mipmap.ic_noimage).into(vh.image);
        vh.tv_name.setText(model.getReal_name());
        vh.tv_position.setText("("+model.getDepartment()+")");
        if(model.getSelected()!=null) {
            vh.tv_selected.setSelected(model.getSelected());
        }
        view.setBackgroundResource(R.drawable.listitemcolor2);
        return view;
    }

    class ViewHolder{
        CircleImageView image;
        ImageView tv_selected;
        TextView tv_name,tv_position;
    }
}
