package com.wyu.iwork.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.wyu.iwork.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/6/7.
 */

public class ProcessSelectDialog extends Dialog {

    private Context context;
    private RecyclerView recyclerView;
    private String currentProcesss;
    private DialogListener listener;

    public ProcessSelectDialog(Context context,String currentProcesss,DialogListener listener) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.currentProcesss = currentProcesss;
        this.listener = listener;
    }

    public ProcessSelectDialog(Context context, int themeResId,String currentProcesss,DialogListener listener) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.currentProcesss = currentProcesss;
        this.listener = listener;
    }

    protected ProcessSelectDialog(Context context, boolean cancelable, OnCancelListener cancelListener,String currentProcesss,DialogListener listener) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.currentProcesss = currentProcesss;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_process_select);
        initView();
    }

    private void initView(){
        recyclerView = (RecyclerView) findViewById(R.id.dialog_process_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new ProcessAdapter(context.getResources().getStringArray(R.array.crm_process_change)));
        WindowManager windowManager = this.getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;//// 设置宽度
        lp.height= ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        this.getWindow().setAttributes(lp);
    }

    class ProcessAdapter extends RecyclerView.Adapter<ProcessAdapter.ViewHolder>{

        private LayoutInflater mInflater;
        private String[] arrs;


        public ProcessAdapter(String[] arrs){
            this.arrs = arrs;
            this.mInflater = LayoutInflater.from(context);

        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mInflater.inflate(R.layout.item_select_process,parent,false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.select_item.setText(arrs[position]);
            if(currentProcesss.equals(arrs[position])){
                holder.itemView.setSelected(true);
            }else{
                holder.itemView.setSelected(false);
            }
        }

        @Override
        public int getItemCount() {
            return arrs.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            @BindView(R.id.process_check)
            TextView process_check;

            @BindView(R.id.select_item)
            TextView select_item;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if("赢单".equals(arrs[getLayoutPosition()]) || "无效".equals(arrs[getLayoutPosition()])){
                            //赢单或无效
                            listener.itemClick(arrs[getLayoutPosition()]);
                            if("赢单".equals(arrs[getLayoutPosition()])){
                                showWarnDialog("确定商机赢单?");
                            }else{
                                showWarnDialog("确定商机无效?");
                            }
                            ProcessSelectDialog.this.dismiss();
                        }else if("输单".equals(arrs[getLayoutPosition()])){
                            //输单
                            recyclerView.setAdapter(new ProcessAdapter(context.getResources().getStringArray(R.array.crm_lost_opportunity)));

                        }else{
                            //其他
                            if (listener != null){
                                listener.itemClick(arrs[getLayoutPosition()]);
                                ProcessSelectDialog.this.dismiss();
                            }
                        }
                    }
                });
            }
        }
    }

    private void showWarnDialog(final String text){
        new MyCustomDialogDialog(9, context, R.style.MyDialog, text, new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
                ProcessSelectDialog.this.show();
            }

            @Override
            public void twoClick(Dialog dialog) {
                //确定
                listener.showClickMessage(text);
                dialog.dismiss();
            }
        }).show();
    }

    public interface DialogListener{

        void itemClick(String text);

        void showClickMessage(String text);

    }
}
