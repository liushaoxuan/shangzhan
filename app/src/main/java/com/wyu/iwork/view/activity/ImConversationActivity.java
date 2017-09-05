package com.wyu.iwork.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wyu.iwork.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.UserInfo;

/**
 * @author HuaJian Jiang.
 *         Date 2017/1/16.
 */
public class ImConversationActivity extends BaseActivity {
    private static final String TAG = ImConversationActivity.class.getSimpleName();

    @BindView(R.id.oa_title)
    TextView title;

    @BindView(R.id.oa_aplly_title_bar)
    RelativeLayout titlebar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imconversation);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        getmTitle();
        try {
            RongIM.getInstance().refreshUserInfoCache(new UserInfo(userInfo.getUser_id(),userInfo.getReal_name(),Uri.parse(userInfo.getUser_face_img())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setFragment();
    }

    private void getmTitle(){
        Uri uri =  getIntent().getData();
        if (uri!=null){
            String mtitle = uri.getQueryParameter("title").toString();
            title.setText(mtitle);
        }else {
            title.setText("私信");
        }

    }

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @OnClick(R.id.oa_back)
    void Click(){
        onBackPressed();
    }

    private void setFragment(){
      FragmentManager fm =  getSupportFragmentManager();
        FragmentTransaction  ft= fm.beginTransaction();
        ft.replace(R.id.activity_imconversation_fragment,new ConversationFragment());
        ft.commit();
    }
}
