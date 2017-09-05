package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.MyItemClickListener;
import com.wyu.iwork.model.MyFragmentModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/4/6.
 */

public class MineFragmentAdapter extends RecyclerView.Adapter<MineFragmentAdapter.ViewHolder>{
    private Context context;
    private List<MyFragmentModel> list;
    private MyItemClickListener mitemClickListener;

    public MineFragmentAdapter(Context context, List<MyFragmentModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mine_fragment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_mine_1_layout)
        RelativeLayout layout1;//专门为第一项设置的layout
        @BindView(R.id.item_mine_namelayout)
        RelativeLayout layout;
        @BindView(R.id.item_mine_image_1)
        ImageView imageView1;

        @BindView(R.id.item_mine_name_1)
        TextView name1;

        @BindView(R.id.item_mine_content)
        TextView content;

        @BindView(R.id.item_mine_image)
        ImageView imageView;
        @BindView(R.id.item_mine_name)
        TextView name;
        @BindView(R.id.item_mine_bottom)
        View bottom;
        @BindView(R.id.item_mine_line)
        View line;
        private View view;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            view = itemView;
        }
        private void setData(final int position){
            MyFragmentModel item = list.get(position);
            if (position==0){
                layout1.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                name1.setText(item.getName());
                content.setText(item.getContent());
                imageView1.setImageResource(item.getImgsrc());

            }else {
                layout.setVisibility(View.VISIBLE);
                layout1.setVisibility(View.GONE);
                imageView.setImageResource(item.getImgsrc());
                name.setText(item.getName());
            }

            if (position==3||position==5||position==7){
                bottom.setVisibility(View.VISIBLE);
                line.setVisibility(View.GONE);
            }else {
                bottom.setVisibility(View.GONE);
                line.setVisibility(View.VISIBLE);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mitemClickListener!=null){
                        mitemClickListener.onImtemClick(v,position);
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(MyItemClickListener listener){
        mitemClickListener = listener;
    }
}
