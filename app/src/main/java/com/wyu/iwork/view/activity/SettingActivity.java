package com.wyu.iwork.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.model.ShareModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.stor.Prefs;
import com.wyu.iwork.util.ActivityManager;
import com.wyu.iwork.util.Exit_app;
import com.wyu.iwork.widget.MyCustomDialogDialog;

import org.litepal.crud.DataSupport;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;

/**
 * @author sxliu
 * 设置
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.action_title)
    TextView title;

    @BindView(R.id.action_edit)
    TextView edit;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().hide();
        ButterKnife.bind(this);

        title.setText("设置");
        edit.setVisibility(View.GONE);
    }

    @OnClick({R.id.action_back,R.id.activity_setting_about_us,R.id.activity_setting_exit,R.id.activity_setting_changePass})
    void Click(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.action_back:
                onBackPressed();
                break;
            case R.id.activity_setting_about_us://关于我们
                intent = new Intent(this,AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_setting_changePass://修改密码
                intent = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent);
                break;

            case R.id.activity_setting_exit://退出登录
                Prefs.delUserInfo(this);
                AppManager.getInstance(this).delUser();
                outSign();
                break;
        }
    }

    /**
     * 退出登录
     */
    private void outSign(){

        new MyCustomDialogDialog(7, this, R.style.MyDialog, "即将退出登录","确定","取消", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }
            @Override
            public void twoClick(Dialog dialog) {
                signOut();
                RongIM.getInstance().logout();
                startActivity(new Intent(SettingActivity.this, SigninActivity.class));
                ActivityManager.getInstance().finishAllActivity();
                finish();
                dialog.dismiss();
                Exit_app.Exit(userInfo);
            }

        }).show();

    }

    private void signOut() {
        AppManager.getInstance(this).setUserInfo(null);
        DataSupport.deleteAll(UserInfo.class);
        DataSupport.deleteAll(ShareModel.class);
        setResult(-1, null);
    }
}
