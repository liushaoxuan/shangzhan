package com.wyu.iwork.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpParams;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.PersonListAdapter;
import com.wyu.iwork.adapter.spaceitemdecoration.SpaceItemDecoration;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.Person;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.widget.MyCustomDialogDialog;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.wyu.iwork.R.id.rv_add_person;

/**
 * 添加日报
 */
public class AddDailyActivity extends BaseActivity implements View.OnTouchListener{

    private static final String TAG = AddDailyActivity.class.getSimpleName();

    @BindView(R.id.et_finish)
    EditText finish;

    @BindView(R.id.et_undo)
    EditText undo;

    @BindView(R.id.et_coordinate)
    EditText coordinate;

    @BindView(R.id.et_remark)
    EditText remark;


    @BindView(rv_add_person)
    RecyclerView recycleView;

    @BindView(R.id.add_image)
    AutoLinearLayout addPerson;

    @BindView(R.id.activity_add_daily_scroolview)
    ScrollView activity_add_daily_scroolview;

    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.tv_edit)
    TextView edit;

    //private ArrayList sendPersonIdList = new ArrayList();
    //private ArrayList<PersonModel> sendPersonList = new ArrayList();
    private PersonListAdapter personListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private int spacingInPixels;
    private SpaceItemDecoration spaceItemDecoration;
    private ArrayList<Person.PersonMessage> headerList = new ArrayList<>();

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily);
        ButterKnife.bind(this);
        hideToolbar();
        initData();
    }

    public void initData(){
        activity_add_daily_scroolview.setOnTouchListener(this);
        edit.setVisibility(View.VISIBLE);
        edit.setText("发布");
        title.setText("新建日报");
    }

    @OnClick({R.id.civ_add_person,R.id.tv_add_person,R.id.tv_edit,R.id.ll_back})
    void Click(View v){
        switch (v.getId()){
            case R.id.civ_add_person:
            case R.id.tv_add_person:
                //添加发送人员
                addPerson();
                break;
            case R.id.tv_edit:
                //提交
                addDailyReport();
                break;
            case R.id.ll_back:
                onBackPressed();
                break;
        }
    }

    //提交日报
    private void addDailyReport(){
        UserInfo user = AppManager.getInstance(this).getUserInfo();
        String send_user = getAllId();
        //修改必填项  :  今日完成工作 和 未完成工作 和 发送给谁是必填项
        if(TextUtils.isEmpty(finish.getText().toString())){
            MsgUtil.shortToastInCenter(this,"请填写该日报的已完成工作!");
        }else if(TextUtils.isEmpty(send_user)){
            MsgUtil.shortToastInCenter(this,"请选择该日报的发送人员!");
        }else{
            String F = Constant.F;
            String V = Constant.V;
            String RandStr = CustomUtils.getRandStr();
            String Sign = Md5Util.getSign(F+V+RandStr+user.getUser_id());

            HttpParams data = new HttpParams();
            data.put("user_id",user.getUser_id());
            data.put("send_users",send_user);
            data.put("finish_work",finish.getText().toString());
            if(!TextUtils.isEmpty(undo.getText().toString())){
                data.put("undone_work",undo.getText().toString());
            }
            if(!TextUtils.isEmpty(coordinate.getText().toString())){
                data.put("coordinate_work",coordinate.getText().toString());
            }
            if(!TextUtils.isEmpty(remark.getText().toString())){
                data.put("remark",remark.getText().toString());
            }
            data.put("F",F);
            data.put("V",V);
            data.put("RandStr",RandStr);
            data.put("Sign",Sign);

            OkGo.post(Constant.URL_ADD_DAILY).tag(this).cacheMode(CacheMode.DEFAULT).params(data)
                    .execute(new DialogCallback(this) {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            super.onSuccess(s,call,response);
                            Logger.i(TAG,s);
                            parseCommitData(s);
                        }
                    });
        }

    }

    //提醒弹窗
    private void showRedmineDialog(){
        new MyCustomDialogDialog(5, this, R.style.MyDialog, "您的日报信息未填写完整\n请完成填写之后再提交", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {

            }
        }).show();
    }

    private String getAllId(){
        String idStr= "|";
        if(headerList.size() <= 0){
            return "";
        }else{
            for(int i = 0;i<headerList.size();i++){
                idStr += headerList.get(i).getId()+"|";
            }
            Logger.i(TAG,idStr);
            return idStr;
        }
    }

    private void parseCommitData(String data){
        try {
            JSONObject object = new JSONObject(data);
            if("0".equals(object.getString("code"))){
                finish();
            }
            MsgUtil.shortToastInCenter(AddDailyActivity.this,object.getString("msg"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //添加发送日报的人员
    public void addPerson(){
        Intent headerIntent = new Intent(this, PersonListActivity.class);
        headerIntent.putExtra("type","header");
        Bundle bundle = new Bundle();
        bundle.putSerializable("HEADER",headerList);
        headerIntent.putExtras(bundle);
        startActivityForResult(headerIntent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 101:
                Bundle bundle = data.getExtras();
                if(requestCode == 0){
                    headerList = (ArrayList<Person.PersonMessage>) data.getExtras().getSerializable("HEADER");
                    Logger.i(TAG,headerList.toString());
                    if(linearLayoutManager == null){
                        linearLayoutManager = new LinearLayoutManager(this);
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    }

                    if(spaceItemDecoration == null){
                        spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
                        spaceItemDecoration = new SpaceItemDecoration(spacingInPixels);
                    }
                    recycleView.setLayoutManager(linearLayoutManager);
                    recycleView.addItemDecoration(spaceItemDecoration);
                    personListAdapter = new PersonListAdapter(this,headerList);
                    recycleView.setAdapter(personListAdapter);
                    personListAdapter.notifyDataSetChanged();
                }
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Hideinputwindown(activity_add_daily_scroolview);
        return false;
    }
}
