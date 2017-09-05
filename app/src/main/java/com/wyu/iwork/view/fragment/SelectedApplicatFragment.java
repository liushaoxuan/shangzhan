package com.wyu.iwork.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.ApplicationAdapter;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.Application;
import com.wyu.iwork.model.QRCodeModule;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.QRCodeContentActivity;
import com.wyu.iwork.view.activity.ScanCardResultActivity;
import com.wyu.iwork.view.activity.WebActivity;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by lx on 2017/1/17.
 * 应用——精选
 */
@SuppressLint("ValidFragment")
public class SelectedApplicatFragment extends Fragment {

    private static final String TAG = SelectedApplicatFragment.class.getSimpleName();

    private ApplicationAdapter adapter;

    @BindView(R.id.select_zanwu)
    AutoLinearLayout select_zanwu;

    @BindView(R.id.reload)
    TextView reload;

    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    
    private Gson gson;
    private Application application;

    private static int REQUEST_CODE = 1001;
    private static final int CODE_CAMERA = 1;

    public SelectedApplicatFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selected_application,null);
        ButterKnife.bind(this,view);
        showContent(false);
        getApplicationlist();
        return view;
    }


    @OnClick({R.id.reload,R.id.zxing_icon})
    void Click(View v){
        switch (v.getId()){
            case R.id.reload:
                getApplicationlist();
                break;

            case R.id.zxing_icon:

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        int isPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
                        Logger.i(TAG,isPermission+"");
                        if(isPermission == PackageManager.PERMISSION_DENIED){
                            Logger.i(TAG,"请求权限");
                            SelectedApplicatFragment.this.requestPermissions(new String[]{Manifest.permission.CAMERA},CODE_CAMERA);
                        }else{
                            Logger.i(TAG,"已有权限");
                            scanCode();
                        }
                    }else{
                        scanCode();
                    }

                }catch (Exception e){
                    MsgUtil.shortToastInCenter(getActivity(),"请前往手机设置检查拍照权限是否打开!");
                }
                break;
        }
    }

    private void scanCode(){
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * 获取应用列表
     */
    private void getApplicationlist(){
        //loadingDialog.show(this.getFragmentManager(), "ApplicationFragment");
        String RandStr = CustomUtils.getRandStr();
        String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr);
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", MyApplication.userInfo.getUser_id());
        data.put("F",Constant.F);
        data.put("V",Constant.V);
        data.put("RandStr",RandStr);
        data.put("Sign",sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_GET_APP_LIST_V2,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(getActivity()) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        parseData(s);
                    }

                    @Override
                    public void onAfter(@Nullable String s, @Nullable Exception e) {
                        super.onAfter(s, e);
                        //loadingDialog.dismiss();
                    }
                });
    }

    private void parseData(String s){
        if(gson == null){
            gson = new Gson();
        }
        try {
            application = gson.fromJson(s,Application.class);
            Logger.i(TAG,application.toString());
            if("0".equals(application.getCode())){
                if(application.getData() != null){
                    showContent(true);
                    setAdapter(application);
                }else{
                    showContent(false);
                }
            }else{
                showContent(false);
            }
        }catch (Exception e){
            e.printStackTrace();
            showContent(false);
        }
    }

    private void setAdapter(Application application){
        adapter = new ApplicationAdapter(getActivity(),application);
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recycleview.setAdapter(adapter);
    }

    private void showContent(boolean flag){
        select_zanwu.setVisibility(flag?View.GONE:View.VISIBLE);
        recycleview.setVisibility(flag?View.VISIBLE:View.GONE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter = null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE){
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Logger.i(TAG,result);
                    postQrCodeMessage(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(), "解析二维码失败，请重试!", Toast.LENGTH_LONG).show();

                }
            }
        }
    }

    private void postQrCodeMessage(String code){
        /**
         * text	是	int[180]扫码后数据
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr
         */
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr);
        HashMap<String,String> data = new HashMap<>();
        data.put("text",code);
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_GET_QRCODE_INFO, data);
        OkGo.post(murl).tag(this).cacheMode(CacheMode.DEFAULT).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                Logger.i(TAG,s);
                Gson gson = null;
                try {
                    gson = new Gson();
                    QRCodeModule qrCodeModule = gson.fromJson(s,QRCodeModule.class);
                    if(qrCodeModule.getCode() == 0){
                        /**
                         * 0 -> 原文输出 text:内容
                         1 -> 跳转链接 text:url地址
                         2 -> 跳转用户信息 text:用户id
                         3 -> 跳转名片信息 text:名片id
                         */
                        Intent intent = null;
                        if(qrCodeModule.getData().getType().equals("0")){
                            //0 -> 原文输出 text:内容
                            if(!TextUtils.isEmpty(qrCodeModule.getData().getText())){
                                intent = new Intent(getActivity(), QRCodeContentActivity.class);
                                intent.putExtra("msg",qrCodeModule.getData().getText());
                                startActivity(intent);
                            }
                        }else if(qrCodeModule.getData().getType().equals("1")){
                            //1 -> 跳转链接 text:url地址
                            if(!TextUtils.isEmpty(qrCodeModule.getData().getText())){
                                intent = new Intent(getActivity(), WebActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("url",qrCodeModule.getData().getText());
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        }else if(qrCodeModule.getData().getType().equals("2")){
                            //2 -> 跳转用户信息 text:用户id
                            if(!TextUtils.isEmpty(qrCodeModule.getData().getText())){
                                intent = new Intent(getActivity(), ScanCardResultActivity.class);
                                intent.putExtra("type","6");
                                intent.putExtra("id",qrCodeModule.getData().getText());
                                startActivity(intent);
                            }
                        }else if(qrCodeModule.getData().getType().equals("3")){
                            //3 -> 跳转名片信息 text:名片id
                            if(!TextUtils.isEmpty(qrCodeModule.getData().getText())){
                                intent = new Intent(getActivity(), ScanCardResultActivity.class);
                                intent.putExtra("type","5");
                                intent.putExtra("id",qrCodeModule.getData().getText());
                                startActivity(intent);
                            }
                        }
                    }else{
                        MsgUtil.shortToastInCenter(getActivity(),qrCodeModule.getMsg());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CODE_CAMERA){
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                Logger.i(TAG,"权限请求通过");
                scanCode();
            } else {
                // Permission Denied
                Logger.i(TAG,"权限请求未通过");
                MsgUtil.shortToastInCenter(getActivity(),"请前往手机设置检查拍照权限是否打开!");

            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
