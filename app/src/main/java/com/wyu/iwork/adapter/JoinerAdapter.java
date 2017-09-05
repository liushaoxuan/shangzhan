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
import com.wyu.iwork.model.PersonListModel;
import com.wyu.iwork.model.PersonModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lx on 2016/12/26.
 */

public class JoinerAdapter extends BaseAdapter {
    private static final String TAG="ChargeAdapter";
    private Context context;
    private List<PersonListModel> list=new ArrayList<>();
    private ViewHolder vh;

    public JoinerAdapter(Context context, List<PersonListModel> list) {
        this.context = context;
        this.list = list;
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
        vh.delected.setVisibility(View.GONE);

        view.setBackgroundResource(R.drawable.listitemcolor2);
        return view;
    }
    class ViewHolder{
        CircleImageView image;
        ImageView delected;
        TextView tv_name;
    }
}
