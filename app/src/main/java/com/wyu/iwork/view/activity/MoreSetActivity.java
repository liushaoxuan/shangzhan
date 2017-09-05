package com.wyu.iwork.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.presenter.MoveBindingPresenter;
import com.wyu.iwork.stor.Prefs;
import com.wyu.iwork.util.ActivityManager;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.widget.MyDialog;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

public class MoreSetActivity extends BaseNetActivity implements View.OnClickListener {
    private static final String TAG=MoreSetActivity.class.getSimpleName();
    private TextView tv_NavigationSet,tv_ModifyPassword,tv_RemoveBinding,tv_ClearCache,tv_About,tv_MSPosition;
    private TextView[]textViews={tv_NavigationSet,tv_ModifyPassword,tv_RemoveBinding,tv_ClearCache,tv_About,tv_MSPosition};
    private Integer[]tvID={R.id.tv_NavigationSet,R.id.tv_ModifyPassword,R.id.tv_RemoveBinding,R.id.tv_ClearCache,R.id.tv_About,R.id.tv_MSPosition};
    private Button btn_Cancellation;
    private String tv_Content;
    private MoveBindingPresenter moveBindingPresenter;
    private Dialog mDialog;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_set);
        setBackNaviAction();
        ((TextView) setCustomViewForToolbar(R.layout.title_toolbar)).setText("更多设置");

        if(AppManager.getInstance(this).getUserInfo()!=null&&!"".equals(AppManager.getInstance(this).getUserInfo())){
            userID= AppManager.getInstance(this).getUserInfo().getUser_id();
            Log.i("===userID===",userID);
        }else {
            startActivity(new Intent(this,SigninActivity.class));
        }
        init();
    }
    private void init() {
        for (int i=0;i<tvID.length;i++){
            textViews[i]=(TextView)findViewById(tvID[i]);
            textViews[i].setOnClickListener(this);
        }
        btn_Cancellation=(Button)findViewById(R.id.btn_MSCancellation);
        btn_Cancellation.setOnClickListener(this);
        initData();
    }

    private void initData() {
        tv_MSPosition=(TextView)findViewById(R.id.tv_MSPosition);
        tv_ClearCache=(TextView)findViewById(R.id.tv_ClearCache);
        String str=getExtar();

        if("1".equals(str)){
            tv_MSPosition.setText("管理者");
        }else{
            tv_MSPosition.setText("职员");
        }

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tv_NavigationSet:
                break;
            case R.id.tv_ModifyPassword:
                startActivity(new Intent(this,ModifyPasswordActivity.class));
                break;
            case R.id.tv_RemoveBinding:
                tv_Content=getString(R.string.ReminderText);
                new MyDialog(3, this, R.style.MyDialog, "确定", "取消", tv_Content, new MyDialog.DialogClickListener() {
                    @Override
                    public void oneClick(Dialog dialog) {
                        if(!userID.isEmpty()) {
                            RemoveBinding();
                        }
                        mDialog=dialog;
                    }

                    @Override
                    public void twoClick(Dialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void threeClick(Dialog dialog) {

                    }
                }).show();
                break;
            case R.id.tv_ClearCache:
                try {
                    String size= CustomUtils.getTotalCacheSize(this);
                    new MyDialog(3, this, R.style.MyDialog, "确定", "取消", "当前缓存大小为："+size+",清除缓存不会造成主要数据丢失，您确定要清除缓存吗？", new MyDialog.DialogClickListener() {
                        @Override
                        public void oneClick(Dialog dialog) {
                            clearCache();
                            dialog.dismiss();
                        }

                        @Override
                        public void twoClick(Dialog dialog) {
                            dialog.dismiss();
                        }

                        @Override
                        public void threeClick(Dialog dialog) {

                        }
                    }).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_About:
                startActivity(new Intent(this,AboutActivity.class));
                break;
            case R.id.btn_MSCancellation:
                Prefs.delUserInfo(this);
                AppManager.getInstance(this).delUser();
                signOut();
                startActivity(new Intent(this,SigninActivity.class));
                ActivityManager.getInstance().finishAllActivity();
                finish();
                break;
        }
    }

    public void RemoveBinding(){
        setNetPresenter(moveBindingPresenter=new MoveBindingPresenter(this),TAG);
        moveBindingPresenter=new MoveBindingPresenter(this);
        moveBindingPresenter.attachView(this);
        moveBindingPresenter.onLoadData();
        moveBindingPresenter.doSignIn(userID);
    }

    @Override
    public void onSuccess(Object data, JSONObject origin) {
        super.onSuccess(data, origin);
        MsgUtil.shortToast(MoreSetActivity.this,"修改成功");
    }

    @Override
    public void onFailure(int errorCode, String errorMsg) {
        super.onFailure(errorCode, errorMsg);
        MsgUtil.shortToast(MoreSetActivity.this,errorMsg);
    }

    @Override
    public void onRequestAfter() {
        super.onRequestAfter();
        mDialog.dismiss();
    }

    public String getExtar() {
        Intent it=getIntent();
        String Is_admin=it.getStringExtra("Is_admin");
        return Is_admin;
    }

    /**退出登录*/
    private void signOut() {
                    AppManager.getInstance(this).setUserInfo(null);
        DataSupport.deleteAll(UserInfo.class);
                    this.setResult(-1,null);
                    this.finish();
    }

    private void clearCache(){
    CustomUtils.clearAllCache(this);
    }
}
