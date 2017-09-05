package com.wyu.iwork.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.util.ShareUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author sxliu
 * 邀请同事
 */
public class InviteColleaguesActivity extends BaseActivity {

    @BindView(R.id.action_title)
    TextView title;

    @BindView(R.id.action_edit)
    TextView edit;

    private Dialog dialog;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_invitation);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        title.setText("邀请同事");
        edit.setVisibility(View.GONE);
        dialog = new Dialog(this);
    }

    @OnClick({R.id.action_back,R.id.activity_mine_invitation_wechat,R.id.activity_mine_invitation_constacts})
    void Click(View v){
        switch (v.getId()){
            case R.id.action_back:
                onBackPressed();
                break;

            case R.id.activity_mine_invitation_wechat://邀请微信同事
                ShareUtil.shareAppWechat(dialog,this);
                break;

            case R.id.activity_mine_invitation_constacts://邀请通讯录同事
                startActivity(new Intent(this,InvitaFriendsActivity.class));
                break;
        }
    }
}
