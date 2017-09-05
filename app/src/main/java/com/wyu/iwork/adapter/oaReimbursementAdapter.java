package com.wyu.iwork.adapter;

import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.wyu.iwork.R;
import com.wyu.iwork.model.OptionsModel;
import com.wyu.iwork.model.oaPaymentModel;
import com.wyu.iwork.view.activity.oaReimbursementActivity;
import com.wyu.iwork.view.activity.oaWorkOverTimeActivity;
import com.wyu.iwork.view.dialog.PickerViewDialog;
import com.wyu.iwork.widget.CustomeViewGroup;
import com.zhy.autolayout.utils.AutoUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者： sxliu on 2017/6/12.14:21
 * 邮箱：2587294424@qq.com
 * OA——报销的adapter
 */

public class oaReimbursementAdapter extends RecyclerView.Adapter<oaReimbursementAdapter.ViewHolder>{

    private oaReimbursementActivity context;
    private List<oaPaymentModel> list;

    private List<OptionsModel> coast_type = new ArrayList<>();

    public oaReimbursementAdapter(oaReimbursementActivity context, List<oaPaymentModel> list) {
        this.context = context;
        this.list = list;
        if (coast_type.size()==0){
            inint();
        }
    }

    //初始化费用类型
    private void inint(){  //.费用类型：
        coast_type.add(new OptionsModel("飞机票"));
        coast_type.add(new OptionsModel("火车票"));
        coast_type.add(new OptionsModel("打的费"));
        coast_type.add(new OptionsModel("住宿费"));
        coast_type.add(new OptionsModel("餐饮费"));
        coast_type.add(new OptionsModel("礼品费"));
        coast_type.add(new OptionsModel("活动费"));
        coast_type.add(new OptionsModel("通讯费"));
        coast_type.add(new OptionsModel("补助"));
        coast_type.add(new OptionsModel("其他"));
    }

    @Override
    public oaReimbursementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.oa_item_reimbursement,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(oaReimbursementAdapter.ViewHolder holder, int position) {
            holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        /**
         * 明细项 list。size大于1才显示
         */
        @BindView(R.id.oa_item_reimbursement_position_layout)
        RelativeLayout layout;

        /**
         * 明细项(第几项) list。size大于1才显示
         */
        @BindView(R.id.oa_item_reimbursement_position)
        TextView mindex;

        /**
         * 明细项(删除) list。size大于1才显示
         */
        @BindView(R.id.oa_item_reimbursement_del)
        TextView del;

        /**
         *  费用类型
         */
        @BindView(R.id.oa_item_reimbursement_type)
        CustomeViewGroup type;

        /**
         *  发生时间
         */
        @BindView(R.id.oa_item_reimbursement_time)
        CustomeViewGroup time;

        /**
         *  费用金额
         */
        @BindView(R.id.oa_item_reimbursement_account)
        EditText account;

        /**
         *  费用说明
         */
        @BindView(R.id.oa_item_reimbursement_content)
        EditText inputText;

        /**
         *  费用说明字数
         */
        @BindView(R.id.oa_item_reimbursement_input_size)
        TextView inputText_accout;
        /**
         *  添加明细
         */
        @BindView(R.id.oa_item_reimbursement_add)
        LinearLayout add;


        //报销类型的Picker
        private PickerViewDialog Typepicker;

        //发生时间
        private PickerViewDialog timePicke;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            AutoUtils.autoSize(itemView);
        }

        private void setData(final int position){
            int size = list.size();
            if (position==size-1){
                add.setVisibility(View.VISIBLE);
            }else {
                add.setVisibility(View.GONE);
            }

            if (size==1){
                layout.setVisibility(View.GONE);
            }else {
                layout.setVisibility(View.VISIBLE);
            }

            oaPaymentModel item = list.get(position);
            type.setRightText(item.getCost_type().equals("")?"请选择":item.getCost_type());
            time.setRightText(item.getStart_time().equals("")?"请选择":item.getStart_time());
            account.setText(item.getMoney());
            mindex.setText("报销明细"+(position+1));
            inputText_accout.setText(item.getContent().length()+"/60");

            //删除
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.del(position);
                }
            });

            //报销类型
            Typepicker = new PickerViewDialog(context, coast_type, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    String text = coast_type.get(options1).getPickerViewText();
                    type.setRightText(text);
                    context.setType(position,text);
                    context.Hideinputwindown(account);
                }
            });

            //
            //时间选择器
            timePicke = new PickerViewDialog(context, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                 String mtime = getTime(date);
                    time.setRightText(mtime);
                    context.setTime(position,mtime);
                    context.Hideinputwindown(account);
                }
            }, "");

            //字数监听
            inputText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    context.setdescribe(position,s.toString());
                    int length = s.length();
                    inputText_accout.setText( length+"/60");
                    if (length==60){
                        Toast.makeText(context,"费用说明最多60字",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //费用输入监听
            account.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    context.setCoast(position,s.toString());
                    context.setTotal();
                }
            });

            //添加明细
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.add();
                }
            });

            //发生时间
            time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.Hideinputwindown(v);
                    if (timePicke!=null){
                        timePicke.show_timepicker_h_m();
                    }
                }
            });

            //费用类型
            type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.Hideinputwindown(v);
                    if (Typepicker!=null){
                        Typepicker.show_Options();
                    }
                }
            });

        }
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
}
