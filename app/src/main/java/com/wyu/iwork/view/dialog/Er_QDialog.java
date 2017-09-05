package com.wyu.iwork.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wyu.iwork.R;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.view.activity.MineCardActivity;

import io.rong.imkit.RongIM;

/**
 * Created by lx on 2017/4/15.
 */

public class Er_QDialog extends AlertDialog{

    private ImageView face;
    private ImageView er_q;
    private ImageView del;
    private TextView userName;
    private TextView company;
    private LinearLayout del_layout;

    private Activity context;
    private String companyString;
    private String User_name;
    private String face_img;
    private Bitmap face_bit;

    public Er_QDialog(Activity context, String company, String user_face,Bitmap bit, String User_name) {
        super(context,R.style.dialog);
        this.context = context;
        this.companyString = company;
        this.User_name = User_name;
        face_bit = bit;
        face_img = user_face;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_er_q);
        face = (ImageView) findViewById(R.id.er_q_user_face);
        er_q = (ImageView) findViewById(R.id.er_q);
        del = (ImageView) findViewById(R.id.del_er_q);

        userName = (TextView) findViewById(R.id.er_q_userName);
        company = (TextView) findViewById(R.id.er_q_company);
        del_layout = (LinearLayout) findViewById(R.id.del_layout);
//        WindowManager m = context.getWindowManager();
//        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
//        WindowManager.LayoutParams p = this.getWindow().getAttributes();  //获取对话框当前的参数值
//        p.width = (int) (d.getWidth() * 1.0);    //宽度设置为屏幕的0.90
//        this.getWindow().setAttributes(p);     //设置生效

        userName.setText(User_name);
        company.setText(companyString);
        Glide.with(context).load(face_img).transform(new CenterCrop(context), new GlideRoundTransform(context, 5)).into(face);
        er_q.setImageBitmap(face_bit);

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
