package com.wyu.iwork.view.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 作者： sxliu on 2017/7/13.14:38
 * 邮箱：2587294424@qq.com
 */

public class DelPopWindown extends PopupWindow {

    private Context mcontext;

    public DelPopWindown(Context context) {
        super(context);
        this.mcontext = context;
        LinearLayout linearLayout = new LinearLayout(context);
        TextView textView = new TextView(context);
        linearLayout.addView(textView);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.width = 200;
        lp.height = 60;
        linearLayout.setLayoutParams(lp);
        setContentView(linearLayout);
        textView.setText("删除");
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext,"删除成功",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 显示在view下方
     * @param v
     */
    public void showAsViewDown(View v){

    }

    /**
     * 显示在view上方
     * @param v
     */
    public void showAsViewTop(View v){
        this.showAsDropDown(v);
//         int x = v.getMeasuredWidth()/2;
//         int y = v.getMeasuredHeight()/2;
//        this.showAtLocation(v, Gravity.NO_GRAVITY,x,y);

    }
}
