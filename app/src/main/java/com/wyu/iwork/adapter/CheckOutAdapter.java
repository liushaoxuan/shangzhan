package com.wyu.iwork.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.CheckOutSignModule;

import java.util.ArrayList;

/**
 * Created by lx on 2017/4/7.
 */

public class CheckOutAdapter extends BaseAdapter {

    private Context context;

    private static final int VIEW_TYPE_FIRST = 1;//有薯条
    private static final int VIEW_TYPE_SECOND = 2;//没薯条
    private ArrayList<CheckOutSignModule.CheckOut.CheckOutData> list;

    public CheckOutAdapter(Context context,ArrayList<CheckOutSignModule.CheckOut.CheckOutData> list){
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CheckOutSignModule.CheckOut.CheckOutData getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
/**
    @Override
    public int getItemViewType(int position) {
        if(position == getCount()-1){
            return VIEW_TYPE_SECOND;
        }else{
            return VIEW_TYPE_FIRST;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageViewHolder imageViewHolder;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_check_out_detail,null);
            imageViewHolder = new ImageViewHolder();
            imageViewHolder.check_out_time = (TextView) convertView.findViewById(R.id.check_out_time);
            imageViewHolder.check_out_image = (ImageView) convertView.findViewById(R.id.check_out_image);
            imageViewHolder.check_out_thing = (TextView) convertView.findViewById(R.id.check_out_thing);
            imageViewHolder.check_out_address = (TextView) convertView.findViewById(R.id.check_out_address);
            convertView.setTag(imageViewHolder);
        }else{
            imageViewHolder = (ImageViewHolder) convertView.getTag();
        }
        imageViewHolder.check_out_address.setText("打卡地点："+getItem(position).getAddress());
        imageViewHolder.check_out_thing.setText("外出事由："+getItem(position).getText());
        String[] str = getItem(position).getTime().split(" ")[1].split(":");
        imageViewHolder.check_out_time.setText(str[0]+":"+str[1]);
        return convertView;
    }

    class ImageViewHolder{
        TextView check_out_time;
        ImageView check_out_image;
        TextView check_out_thing;
        TextView check_out_address;
    }

}
