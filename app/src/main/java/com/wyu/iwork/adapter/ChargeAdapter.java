package com.wyu.iwork.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wyu.iwork.R;
import com.wyu.iwork.model.PersonModel;
import com.wyu.iwork.util.CustomUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lx on 2016/12/26.
 */

public class ChargeAdapter extends BaseAdapter {
    private static final String TAG="ChargeAdapter";
    private Context context;
    private ArrayList<PersonModel> list=new ArrayList<>();
    private ViewHolder vh;
    private int position;

    public ChargeAdapter(Context context, ArrayList<PersonModel> list,int position) {
        this.context = context;
        this.list = list;
        this.position=position;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            vh=new ViewHolder();
            view= LayoutInflater.from(context).inflate(R.layout.item_charge,viewGroup,false);
            vh.image=(CircleImageView)view.findViewById(R.id.item_Charge_head);
            vh.tv_name=(TextView)view.findViewById(R.id.item_Charge_name);
            vh.delected=(ImageView)view.findViewById(R.id.item_deleteCharge);
            view.setTag(vh);
        }else{
            vh = (ViewHolder) view.getTag();
        }
        Glide.with(context).load(list.get(i).getFace_img()).dontAnimate().placeholder(R.mipmap.ic_noimage).into(vh.image);
        if(position==1) {
            vh.tv_name.setText(list.get(i).getReal_name());
        }
        view.setBackgroundResource(R.drawable.listitemcolor2);
        return view;
    }
    class ViewHolder{
        CircleImageView image;
        ImageView delected;
        TextView tv_name;
    }
}
