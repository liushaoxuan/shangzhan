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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpParams;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.PersonListAdapter;
import com.wyu.iwork.adapter.spaceitemdecoration.SpaceItemDecoration;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.Person;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.view.dialog.PickerViewDialog;
import com.wyu.iwork.wheeldate.SelectMeetingTime;
import com.wyu.iwork.widget.MyCustomDialogDialog;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.wyu.iwork.util.Constant.F;
import static com.wyu.iwork.util.Constant.V;


public class MeetingMessageActivity extends BaseActivity implements View.OnTouchListener{

    private static final String TAG = MeetingMessageActivity.class.getSimpleName();

    @BindView(R.id.et_content)
    EditText et_content;

    @BindView(R.id.rl_time)
    AutoRelativeLayout rl_time;


    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.ll_address)
    AutoLinearLayout ll_address;

    @BindView(R.id.et_address)
    EditText et_address;

    @BindView(R.id.rv_add_person)
    RecyclerView rv_add_person;

    @BindView(R.id.add_image)
    AutoLinearLayout add_image;

    @BindView(R.id.tv_commit)
    TextView tv_commit;

    @BindView(R.id.ll_layout)
    LinearLayout ll_layout;

    @BindView(R.id.rl_remind)
    AutoRelativeLayout rl_remind;

    @BindView(R.id.remind_tv)
    TextView remind_tv;

    @BindView(R.id.remind_close)
    ImageView remind_close;

    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.meeting_scrollview)
    ScrollView meeting_scrollview;

    private SelectMeetingTime mSelectMeetingTime;
    private Date meetingTime ;
    private String date = "";

    private static final int CODE_REQUEST = 109;
    private static final int CODE_RESULT = 108;
    private LinearLayoutManager linearLayoutManager;
    private SpaceItemDecoration spaceItemDecoration;
    private int spacingInPixels;
    private PersonListAdapter personListAdapter;

    private ArrayList<Person.PersonMessage> headerList = new ArrayList<>();
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_message);
        ButterKnife.bind(this);
        hideToolbar();
        initView();
    }

    private void initView(){
        meeting_scrollview.setOnTouchListener(this);
        tv_title.setText("会议通知");
    }


    @OnClick({R.id.rl_time,R.id.add_image,R.id.remind_close,R.id.tv_commit,R.id.ll_back})
    void Click(View v){
        switch (v.getId()){
            case R.id.rl_time:
                Hideinputwindown(rl_time);
                showDatePickerDialog();
                break;
            case R.id.add_image:
                addPerson();
                break;
            case R.id.remind_close:
                rl_remind.setVisibility(View.GONE);
                break;
            case R.id.tv_commit:
                commitMeetingData();
                break;
            case R.id.ll_back:
                onBackPressed();
                break;
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

    private String getAllId(){
        String idStr= "|";
        if(headerList.size() <= 0){
            MsgUtil.shortToastInCenter(this,"请选择发送的人!");
        }else{
            for(int i = 0;i<headerList.size();i++){
                idStr += headerList.get(i).getId()+"|";
            }
            Logger.i(TAG,idStr);
            return idStr;
        }
        return "";
    }

    private void commitMeetingData(){
        String meetingContent = et_content.getText().toString();
        String address = et_address.getText().toString();
        if(TextUtils.isEmpty(meetingContent)){
            remind_tv.setText("请输入会议内容和需要准备的材料");
            rl_remind.setVisibility(View.VISIBLE);
        }else if(TextUtils.isEmpty(address)){
            remind_tv.setText("请输入会议地点");
            rl_remind.setVisibility(View.VISIBLE);
        }else if(meetingTime == null){
            remind_tv.setText("请选择会议开始的时间");
            rl_remind.setVisibility(View.VISIBLE);
        }else if(headerList.size()<=0){
            remind_tv.setText("请选择参与会议的人员");
            rl_remind.setVisibility(View.VISIBLE);
        }else{
            /**
             * user_id	是	int[11]会议通知发布者用户ID
             send_users	是	string[150]被通知者用户ID( 多个用户用 | 进行连接,如|232|445| )
             content	是	string[200]会议概要的内容和需要准备的资料
             address	是	string[150]会议地址
             start_time	是	string[18]会议开始的时间, 格式如：2017-03-03 9:00:51
             F	是	string[18]请求来源：IOS/ANDROID/WEB
             V	是	string[20]版本号如：1.0.1
             RandStr	是	string[50]请求加密随机数 time().|.rand()
             Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id.send_users)
             */
            showSendDialog();

        }
    }

    private void commitMeeting(){
        HttpParams data = new HttpParams();
        data.put("user_id",AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("send_users",getAllId());
        data.put("content",et_content.getText().toString());
        data.put("address",et_address.getText().toString());
        data.put("start_time",formatDate(meetingTime));
        data.put("F",F);
        data.put("V",V);
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        //String murl = RequestUtils.getRequestUrl(Constant.URL_SEND_METTING,data);
        Logger.i(TAG,"data: "+data.toString());
        //Logger.i(TAG,murl);
        OkGo.post(Constant.URL_SEND_METTING).tag(this).cacheMode(CacheMode.DEFAULT).params(data)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s,call,response);
                        Logger.i(TAG,s);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(s);
                            if("0".equals(object.getString("code"))){
                                onBackPressed();
                            }
                            else{
                                MsgUtil.shortToastInCenter(MeetingMessageActivity.this,object.getString("msg"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });
    }

    private void showSendDialog(){
        new MyCustomDialogDialog(10, this,R.style.MyDialog, AppManager.getInstance(this).getUserInfo().getReal_name()+"发起了一个会议",
                "会议将于"+tv_time.getText().toString()+"在"+et_address.getText().toString()+"开始！", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {
                commitMeeting();
                dialog.dismiss();
            }
        }).show();
    }

    //显示时间弹窗选择日期
    private void showDatePickerDialog(){
        new PickerViewDialog(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                meetingTime = date;
                tv_time.setText(getTime(date));
            }
        }," ").show_timepicker_h_m();
    }

    private String getTime(Date date){//2017-01-01 12:12
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
        return format.format(date);
    }

    private String formatDate(Date date){//2017-01-01 12:12
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Hideinputwindown(ll_layout);
        return false;
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
                    rv_add_person.setLayoutManager(linearLayoutManager);
                    rv_add_person.addItemDecoration(spaceItemDecoration);
                    personListAdapter = new PersonListAdapter(this,headerList);
                    rv_add_person.setAdapter(personListAdapter);
                    personListAdapter.notifyDataSetChanged();
                }
        }
    }
}
