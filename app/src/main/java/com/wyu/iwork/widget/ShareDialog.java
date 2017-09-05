package com.wyu.iwork.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.util.ShareUtil;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * Created by lx on 2017/3/28.
 */

public class ShareDialog extends Dialog implements View.OnClickListener{

    private Context context;
    private TextView cancel;
    private AutoLinearLayout share_wechat;//微信好友分享
    private AutoLinearLayout share_friend;//微信朋友圈
    private AutoLinearLayout share_qq;//qq好友
    private AutoLinearLayout share_qzone;//qq空间
    private int type;
    private String title;
    private String text;
    private String url;
    private String imageUrl;

    public ShareDialog(Context context) {
        super(context, R.style.ShareDialog);
        this.context = context;
    }

    public ShareDialog(Context context,int type){
        super(context, R.style.ShareDialog);
        this.context = context;
        this.type = type;
    }

    public ShareDialog(Context context,int type,String title,String text,String url,String imageUrl) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.type = type;
        this.title = title;
        this.text = text;
        this.url = url;
        this.imageUrl = imageUrl;
    }
/**
    public ShareDialog(Context context, int themeResId) {
        super(context, R.style.ShareDialog);
        this.context = context;
    }

    protected ShareDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layout_card_share);
        init();
        initDialog(context);
    }

    private void init(){
        cancel = (TextView) findViewById(R.id.tv_cancel);
        share_wechat = (AutoLinearLayout) findViewById(R.id.ll_wechat);
        share_friend = (AutoLinearLayout) findViewById(R.id.ll_friend);
        share_qq = (AutoLinearLayout) findViewById(R.id.ll_qq);
        share_qzone = (AutoLinearLayout) findViewById(R.id.ll_qzone);
        share_wechat.setOnClickListener(this);
        share_friend.setOnClickListener(this);
        share_qq.setOnClickListener(this);
        share_qzone.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    private void initDialog(Context context) {
        try {
            //setCanceledOnTouchOutside(true);
            //setCancelable(true);
            /**
            setOnKeyListener(new OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface dialog, int keyCode,
                                     KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK
                            && event.getRepeatCount() == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });*/
            WindowManager windowManager = this.getWindow().getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = this.getWindow().getAttributes();
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;//// 设置宽度
            lp.height= ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            this.getWindow().setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()){
                case R.id.tv_cancel:
                    dismiss();
                    //取消
                    break;
                case R.id.ll_wechat:
                    //微信好友分享
                    if(type == 1){
                        ShareUtil.shareCardWechat(this,context,title,text,url,imageUrl);
                    }else if(type == 2){
                        ShareUtil.shareAppWechat(this,context);
                    }
                    dismiss();
                    break;
                case R.id.ll_friend:
                    //微信朋友圈分享
                    if(type == 1){
                        ShareUtil.shareCardWechatFriend(this,context,title,text,url,imageUrl);
                    }else if(type == 2){
                        ShareUtil.shareAppWechatFriend(this,context);
                    }
                    dismiss();
                    break;
                case R.id.ll_qq:
                    //qq好友分享
                    if(type == 1){
                        ShareUtil.shareCardQQ(context,this,title,text,url,imageUrl);
                    }else if(type == 2){
                        ShareUtil.shareAppQQ(this,context);
                    }
                    dismiss();
                    break;
                case R.id.ll_qzone:
                    //qq空间分享
                    if(type == 1){
                        ShareUtil.shareCardQZone(context,this,title,text,url,imageUrl);
                    }else if(type == 2){
                        ShareUtil.shareAppQZone(this,context);
                    }
                    dismiss();
                    break;
                default:
                    dismiss();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
