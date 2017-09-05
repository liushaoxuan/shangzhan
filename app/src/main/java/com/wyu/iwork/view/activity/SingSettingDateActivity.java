package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.widget.WeekItem;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SingSettingDateActivity extends BaseActivity {

    private static final String TAG = SingSettingDateActivity.class.getSimpleName();
    //
    @BindView(R.id.monday)
    WeekItem monday;

    @BindView(R.id.Tuesday)
    WeekItem Tuesday;

    @BindView(R.id.Wednesday)
    WeekItem Wednesday;

    @BindView(R.id.Thursday)
    WeekItem Thursday;

    @BindView(R.id.Friday)
    WeekItem Friday;

    @BindView(R.id.Saturday)
    WeekItem Saturday;

    @BindView(R.id.Sunday)
    WeekItem Sunday;

    @BindView(R.id.finish)
    TextView finish;

    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.ll_back)
    AutoLinearLayout back;

    private ArrayList<Integer> weekState = new ArrayList<>();

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_setting_date);
        hideToolbar();
        ButterKnife.bind(this);
        getExtras();
        initView();
    }

    private void getExtras(){
        weekState = getIntent().getIntegerArrayListExtra("week");
        for(int i = 0;i<weekState.size();i++){
            Logger.i(TAG,"week before ； "+weekState.get(i));
        }
        Logger.i(TAG,"getExtras ； "+weekState.size());
        if(weekState.size() == 7){
            setDefault();
        }
    }

    private void setDefault(){
        WeekItem[] week = {monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday};
        if(weekState.size()>0 && weekState.size() == 7){
            for(int i = 0;i<7;i++){
                Logger.i(TAG,weekState.get(i)+"");
                if(weekState.get(i) == 1){
                    week[i].setSelected(true);
                }else{
                    week[i].setSelected(false);
                }
            }
        }
    }

    private void initView(){

        title.setText("选择日期");

        WeekItem[] week = {monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday};
        monday.setValue("星期一");
        Tuesday.setValue("星期二");
        Wednesday.setValue("星期三");
        Thursday.setValue("星期四");
        Friday.setValue("星期五");
        Saturday.setValue("星期六");
        Sunday.setValue("星期日");

    }

    @OnClick({R.id.finish,R.id.ll_back})
    void Click(View view){
        switch (view.getId()){
            case R.id.finish:
                jumpToSetting();
                break;
            case R.id.ll_back:
                onBackPressed();
                break;
        }
    }

    private void jumpToSetting(){
        WeekItem[] week = {monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday};
        weekState.clear();
        for(int i = 0;i<7;i++){
            if(week[i].getSelected()){
                weekState.add(1);
            }else{
                weekState.add(0);
            }
        }
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("week",weekState);
        intent.putExtras(bundle);
        setResult(101,intent);
        finish();
    }
}
