package com.wyu.iwork.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.net.GsonManager;
import com.wyu.iwork.stor.Prefs;
import com.wyu.iwork.takephoto.TakePhoto;
import com.wyu.iwork.takephoto.TakePhotoImpl;
import com.wyu.iwork.takephoto.model.CropOptions;
import com.wyu.iwork.takephoto.model.InvokeParam;
import com.wyu.iwork.takephoto.model.TContextWrap;
import com.wyu.iwork.takephoto.model.TResult;
import com.wyu.iwork.takephoto.permission.InvokeListener;
import com.wyu.iwork.takephoto.permission.PermissionManager;
import com.wyu.iwork.takephoto.permission.TakePhotoInvocationHandler;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.dialog.CemaraDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author  sxliu
 * 编辑个人资料
 */

public class EditPersonnalInfomationActivity extends BaseActivity implements TakePhoto.TakeResultListener,InvokeListener,TextWatcher {

    private static String TAG = EditPersonnalInfomationActivity.class.getSimpleName();
    /**
     * 更换头像
     */
    @BindView(R.id.activity_edit_personnal_infomation_change_head)
    RelativeLayout change_icon;

    /**
     * 头像
     */
    @BindView(R.id.activity_edit_personnal_infomation_head)
    CircleImageView head_icon;

    /**
     * 姓名
     */
    @BindView(R.id.activity_edit_personnal_infomation_name)
    EditText name;

    /**
     * 手机
     */
    @BindView(R.id.activity_edit_personnal_infomation_phone)
    TextView phone;

    /**
     * 邮箱
     */
    @BindView(R.id.activity_edit_personnal_infomation_email)
    EditText email;

    /**
     * 微信
     */
    @BindView(R.id.activity_edit_personnal_infomation_wechat)
    EditText wechat;

    /**
     * 职位
     */
    @BindView(R.id.activity_edit_personnal_infomation_position)
    TextView job;

    /**
     * 公司
     */
    @BindView(R.id.activity_edit_personnal_infomation_company)
    TextView company;

    /**
     * 部门
     */
    @BindView(R.id.activity_edit_personnal_infomation_department)
    TextView department;

    /**
     * 地址
     */
    @BindView(R.id.activity_edit_personnal_infomation_addr)
    EditText address;

    private List<EditText> editTextList = null;
    private UserInfo userInfo;

    /**
     * 可修改的用户信息
     */
    private String sName = "";
    private String sEmail = "";
    private String sWechart = "";
    private String sAddr = "";
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private String path = "";

    @Nullable
    @Override
    public Fragment getFragment() {
        return   null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personnal_infomation);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        userInfo = MyApplication.userInfo;
        if (userInfo==null){
            userInfo =  AppManager.getInstance(this).getUserInfo();
        }

        initviews();
    }
    //返回和保存事件
    @OnClick({R.id.activity_edit_personnal_infomation_back,R.id.activity_edit_personnal_infomation_save,R.id.activity_edit_personnal_infomation_change_head})
    void Click(View v){
        switch (v.getId()){
            case R.id.activity_edit_personnal_infomation_back:
                onBackPressed();
                break;
            case R.id.activity_edit_personnal_infomation_save://保存
                changeUserInfo();
                break;

            case R.id.activity_edit_personnal_infomation_change_head://修改头像
                showGetPicPop();
                break;
        }
    }

    //初始化个人信息参数
    private void initviews(){
        Glide.with(this).load(userInfo.getUser_face_img()).error(R.mipmap.head_icon_nodata).into(head_icon);
        name.setText(userInfo.getReal_name());
        phone.setText(userInfo.getUser_phone());
        email.setText(userInfo.getEmail());
        wechat.setText(userInfo.getWechat());
        job.setText(userInfo.getJob());
        job.setText(userInfo.getJob());
        company.setText(userInfo.getCompany());
        department.setText(userInfo.getDepartment());
        address.setText(userInfo.getAddress());
    }


    //修改用户信息
    private void changeUserInfo(){
        sName = name.getText().toString();
        sEmail = email.getText().toString();
        sWechart = wechat.getText().toString();
        sAddr = address.getText().toString();
        if (sName.isEmpty()){
            Toast.makeText(this,"用户名不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        String url = Constant.URL_MINE_CHANGE_USERINFO;
        String user_id = null;
        user_id = userInfo == null ? "" : userInfo.getUser_id();
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + user_id +sName );
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);
        map.put("real_name", sName );
        map.put("email", sEmail );
        map.put("wechat", sWechart );
        map.put("address", sAddr );
        map.put("F", F);
        map.put("V", V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);

        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(url, map);
        OkGo.get(murl)
                .tag("EditPersonnalInfomationFragment")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }

                    @Override
                    public void onSuccess(String s, Call call, okhttp3.Response response) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String code = object.optString("code");
                            if ("0".equals(code)) {
                                userInfo.setUser_name(sName);
                                if (!"".equals(sEmail)){
                                    userInfo.setEmail(sEmail);
                                }
                                if (!"".equals(sWechart)){
                                    userInfo.setWechat(sWechart);
                                }
                                if (!"".equals(sAddr)){
                                    userInfo.setAddress(sAddr);
                                }
                                saveUserInfo(userInfo);
                                MyApplication.userInfo = userInfo;
                                finish();
                            }
                            Toast.makeText(EditPersonnalInfomationActivity.this, object.optString("msg"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Logger.e("response------------>", s);
                    }
                });
    }

    public void saveUserInfo(UserInfo userInfo) {
        //保存用户信息对象,供当前在应用内其他功能使用
        userInfo.saveOrUpdate("user_id=?",userInfo.getUser_id());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    //修改头像
    private void changeHead(File file){
        String url = Constant.URL_UPDATE_FACE;
        String user_id = userInfo == null ? "" : userInfo.getUser_id();
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + user_id);
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);
        map.put("F", F);
        map.put("V", V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);

        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(url, map);

        OkGo.post(murl)
                .tag("DynamicFragment")
                .cacheMode(CacheMode.DEFAULT)
                .params("face",file)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, okhttp3.Response response) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String code = object.optString("code");
                            if ("0".equals(code)) {

                                Glide.with(EditPersonnalInfomationActivity.this).load(path).into(head_icon);
                                userInfo.setUser_face_img(path);
                                saveUserInfo(userInfo);

                            } else {
                                Toast.makeText(EditPersonnalInfomationActivity.this, object.optString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Logger.e("response------------>", s);
                    }
                });

    }


    //获取图片
    private void showGetPicPop() {

        new CemaraDialog(this, new CemaraDialog.DialogClickListener() {
            @Override
            public void cemaraClick(Dialog dialog) {
                getPermission();
            }

            @Override
            public void galaryClick(Dialog dialog) {
                startGallery();
            }
        }).show();
    }

    //打开相册
    private void startGallery() {
        File file=new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        takePhoto.onPickFromGalleryWithCrop(imageUri,getCropOptions());
    }
    //打开相机
    private void startCamera() {
        File file=new File(Environment.getExternalStorageDirectory(), +System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        takePhoto.onPickFromCaptureWithCrop(imageUri,getCropOptions());
    }
    /*************************/
    /**
     *  获取TakePhoto实例
     * @return
     */
    public TakePhoto getTakePhoto(){
        if (takePhoto==null){
            takePhoto= (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this,this));
        }
        return takePhoto;
    }

    @Override
    public void takeSuccess(TResult result) {
        if (result!=null){
        String filepath =  result.getImage().getOriginalPath();
        File file = new File(filepath);
        path = filepath;
        changeHead(file);
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {
//        AndPermission.defaultSettingDialog(EditPersonnalInfomationActivity.this, 400).show();
        Log.d(TAG,"takeFail,"+msg);
    }

    @Override
    public void takeCancel() {
        Log.d(TAG,"takeCancel");
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type=PermissionManager.checkPermission(TContextWrap.of(this),invokeParam.getMethod());
        if(PermissionManager.TPermissionType.WAIT.equals(type)){
            this.invokeParam=invokeParam;
        }
        return type;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type=PermissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
        PermissionManager.handlePermissionsResult(this,type,invokeParam,this);
    }

    private CropOptions getCropOptions(){
        CropOptions.Builder builder=new CropOptions.Builder();
        builder.setOutputX(500).setOutputY(500);
        builder.setAspectX(500).setAspectY(500);
        builder.setWithOwnCrop(false);
        return builder.create();
    }


    @Override
    protected void onStop() {
        super.onStop();
        updateUserInfo();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        name.setSelection(s.length());
        email.setSelection(s.length());
        wechat.setSelection(s.length());
        address.setSelection(s.length());
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 申请权限
     */
    private void getPermission(){
        AndPermission.with(this)
                .requestCode(100)
                .permission(Permission.CAMERA)
                .rationale(rationaleListener)
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        // 权限申请成功回调。
                        // 这里的requestCode就是申请时设置的requestCode。
                        // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
                        if(requestCode == 100) {
                            startCamera();
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        // 权限申请失败回调。
                        if(requestCode == 100) {
                            // TODO ...
                            AndPermission.defaultSettingDialog(EditPersonnalInfomationActivity.this, 400).show();
                        }
                    }
                })
                .start();
    }

    RationaleListener rationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
            AndPermission.rationaleDialog(EditPersonnalInfomationActivity.this, rationale).show();
        }
    };
}
