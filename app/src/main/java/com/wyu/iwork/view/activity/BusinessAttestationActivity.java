package com.wyu.iwork.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.widget.CrmCreateCustomDialog;
import com.wyu.iwork.widget.CustomCrmItem;
import com.wyu.iwork.widget.MyCustomDialogDialog;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author juxinhua
 *  企业认证
 */
public class BusinessAttestationActivity extends BaseActivity implements View.OnTouchListener{

    private static final String TAG = BusinessAttestationActivity.class.getSimpleName();
    //企业名称
    @BindView(R.id.business_attestation_company)
    CustomCrmItem business_attestation_company;

    //对公账户
    @BindView(R.id.business_attestation_account)
    CustomCrmItem business_attestation_account;

    //开户支行
    @BindView(R.id.business_attestation_bank)
    CustomCrmItem business_attestation_bank;

    //银行执照注册号
    @BindView(R.id.business_attestation_passport_number)
    CustomCrmItem business_attestation_passport_number;

    //请上传营业执照
    @BindView(R.id.business_attestation_post_title)
    CustomCrmItem business_attestation_post_title;

    //联系人姓名
    @BindView(R.id.business_attestation_name)
    CustomCrmItem business_attestation_name;

    //联系方式
    @BindView(R.id.business_attestation_phone)
    CustomCrmItem business_attestation_phone;

    //添加图片按钮
    @BindView(R.id.business_attestation_add_picture)
    ImageView business_attestation_add_picture;

    //添加的图片
    @BindView(R.id.business_attestation_picture)
    ImageView business_attestation_picture;

    //提交
    @BindView(R.id.business_attestation_commit)
    TextView business_attestation_commit;

    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.activity_business_attestation_scrollview)
    ScrollView activity_business_attestation_scrollview;

    private CustomCrmItem[] crmItemArr;

    private static final int REQUEST_CAPTURE = 1001;//拍照 请求码
    private static final int REQUEST_GET_PICTURE = 2001;//从相册选取照片 请求码
    private String filepath;
    private String ImageUrl;//上传后的图片地址
    private File file;
    private boolean iscommit = false;
    //private LoadingDialog mLoadingDialog;
    private static final int CODE_CAMERA = 200;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_attestation);
        hideToolbar();
        ButterKnife.bind(this);
        crmItemArr = new CustomCrmItem[]{business_attestation_company,business_attestation_account,
                business_attestation_bank,business_attestation_passport_number,
                business_attestation_post_title,
                business_attestation_name,business_attestation_phone};
        initView();
        initDialog(this);
        //mLoadingDialog = new LoadingDialog();
    }

    //初始化界面
    private void initView(){
        tv_title.setText("企业认证");
        //当前为编辑界面  所以设置TextView隐藏
        setTextViewGone();
        //设置每个item的标题
        setTitle();
        setKeyDescMustGone();
        setBottomLineGone();
        activity_business_attestation_scrollview.setOnTouchListener(this);
    }

    private void setBottomLineGone(){
        business_attestation_company.setBottomLineVisible(false);
        business_attestation_post_title.setBottomLineVisible(false);
        business_attestation_phone.setBottomLineVisible(false);
    }

    private void setKeyDescMustGone(){
        business_attestation_account.setKeyDescVisible(false);
        business_attestation_bank.setKeyDescVisible(false);
        business_attestation_name.setKeyDescVisible(false);
        business_attestation_phone.setKeyDescVisible(false);
    }

    //设置标题
    private void setTitle(){
        ArrayList<String> titleList = new ArrayList<>();
        titleList.add("企业名称");
        titleList.add("对公账户");
        titleList.add("开户支行");
        titleList.add("银行执照注册号");
        titleList.add("请上传营业执照");
        titleList.add("联系人姓名");
        titleList.add("联系方式");
        for(int i = 0;i<crmItemArr.length;i++){
            crmItemArr[i].setTitle(titleList.get(i));
        }
        titleList.clear();
        titleList = null;
    }

    private void setTextViewGone(){
        for(int i = 0;i<crmItemArr.length;i++){
            crmItemArr[i].setValueVisible(false);
            crmItemArr[i].setEditVisible(true);
        }
        business_attestation_post_title.setEditVisible(false);
    }

    @OnClick({R.id.ll_back,R.id.business_attestation_commit,R.id.business_attestation_add_picture})
    void Click(View v){
        switch (v.getId()){
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.business_attestation_add_picture:
                //选取照片或拍摄
                showSelectPictureDialog();
                break;
            case R.id.business_attestation_commit:
                //提交
                //先上传图片
                iscommit = true;
                if(!TextUtils.isEmpty(filepath) && file != null){
                    uploadImage();
                }else if(!TextUtils.isEmpty(filepath)){
                    MsgUtil.shortToastInCenter(this,"正在压缩图片中，请稍候!");
                }else{
                    showRedmineDialog();
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Hideinputwindown(activity_business_attestation_scrollview);
        return false;
    }

    //消息未填完整提醒弹窗
    private void showRedmineDialog(){
        new MyCustomDialogDialog(5, this, R.style.MyDialog, null, new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {

            }
        }).show();
    }

    //选取照片弹窗 拍照或者从相册选取
    private void showSelectPictureDialog(){
        new CrmCreateCustomDialog(this, "拍摄", "手机相册", new CrmCreateCustomDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                //拍摄
                dialog.dismiss();
                //跳转到相机
                requestLocationPermission();
            }

            @Override
            public void twoClick(Dialog dialog) {
                //相册选取
                dialog.dismiss();
                //跳转到相册
                startAlbum();
            }

            @Override
            public void threeClick(Dialog dialog) {
                //取消
                dialog.dismiss();
            }
        }).show();
    }

    //申请权限
    private void requestLocationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int isPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if(isPermission != PackageManager.PERMISSION_GRANTED){
                Logger.i(TAG,"请求权限");
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CODE_CAMERA);
            }else{
                Logger.i(TAG,"已有权限");
                takePhote();
            }
        }else{
            takePhote();
        }
    }

    private void takePhote(){
        Intent intentPhote = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentPhote.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        filepath = getPhotopath();
        File out = new File(filepath);
        Uri uri ;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, out.getAbsolutePath());
            uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
        }else{
            uri = Uri.fromFile(out);
        }
        // 获取拍照后未压缩的原图片，并保存在uri路径中
        intentPhote.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intentPhote, REQUEST_CAPTURE);
    }

    //跳转到相册
    private void startAlbum(){
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, REQUEST_GET_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK){
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(filepath);
                business_attestation_picture.setImageBitmap(bitmap);
                compressBitmap();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(requestCode == REQUEST_GET_PICTURE){
            //相册
            Bitmap bitmap;
            ContentResolver cr = this.getContentResolver();
            if(data == null)
                return;
            Uri uri = data.getData();

            try {
                bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                business_attestation_picture.setImageBitmap(bitmap);
                filepath = getImagePath(uri);
                compressBitmap();
                Logger.i(TAG,filepath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取原图片存储路径
     * @return
     */
    private String getPhotopath() {
        // 照片全路径
        String fileName = "";
        // 文件夹路径
        String pathUrl = Environment.getExternalStorageDirectory()+"/shangzhan/image";
        new DateFormat();
        String imageName= DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA))+".jpg";
        File file = new File(pathUrl);
        file.mkdirs();// 创建文件夹
        fileName = pathUrl + imageName;
        return fileName;
    }

    //获取图片的资源路径
    private String getImagePath(Uri uri) {
        if (null == uri) {
            Logger.e("getImagePath", "uri return null");
            return null;
        }

        Logger.e("getImagePath", uri.toString());
        String path = null;
        final String scheme = uri.getScheme();
        if (null == scheme) {
            path = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            path = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(uri, proj, null, null,
                    null);
            int nPhotoColumn = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (null != cursor) {
                cursor.moveToFirst();
                path = cursor.getString(nPhotoColumn);
            }
            cursor.close();
        }

        return path;
    }

    /**
     * 上传图片  先压缩图片到100k，再上传
     */

    private void compressBitmap(){
       // mLoadingDialog.show(getSupportFragmentManager(),Constant.DIALOG_TAG_LOADING);
        /**new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                });*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                Logger.i("compress",BitmapFactory.decodeFile(filepath).getByteCount()+"");
                Logger.i("compress","filepath=="+filepath);
                file = CustomUtils.ratio(BitmapFactory.decodeFile(filepath),BusinessAttestationActivity.this,200);
                Logger.i("compress","fileSize=="+file.length());
                Logger.i("compress","file="+file.getAbsolutePath());
                if(iscommit){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            uploadImage();
                        }
                    });
                }
            }
        }).start();


                //runOnUiThread(new Runnable() {
                  //  @Override
                    //public void run() {
                        //uploadImage();
                    //}
                //});
            //}
      //  }).start();
    }
    private void uploadImage(){
        //现将图片进行压缩  再上传
        /**
         * file	是	obj[]需要上传图片 post上传
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().rand()
         Sign	是	string[24]请求加密值 F_moffice_encode(F.V.RandStr.Phone)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(Constant.F+Constant.V+RandStr+ AppManager.getInstance(this).getUserInfo().getUser_id());
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("F",Constant.F);
        data.put("V",Constant.V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_UPLOAD_PIC,data);
        OkGo.post(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .params("file",file)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(s);
                            if("0".equals(object.getString("code"))){
                                ImageUrl = object.getString("data");
                                commitAttestationData();
                            }else{
                                MsgUtil.shortToastInCenter(BusinessAttestationActivity.this,object.getString("msg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {

                        }
                    }
                });
        data.clear();
        data = null;
    }

    /**
     * 上传企业认证数据
     */
    private void commitAttestationData(){
        //判断必填项是否已经全部填写
        if(TextUtils.isEmpty(business_attestation_company.getEditTextValue()) ||
                TextUtils.isEmpty(business_attestation_passport_number.getEditTextValue()) || TextUtils.isEmpty(filepath)){

            showRedmineDialog();
            return;

        }

        //所有item的数组  方便后面读取数据
                                    //企业名称                    //对公账户                    //开户支行
        CustomCrmItem[] items = {business_attestation_company,business_attestation_account,business_attestation_bank,
                //银行执照注册号                   //联系人姓名                 //联系方式
                business_attestation_passport_number,business_attestation_name,business_attestation_phone};
        //依次取出每个item的数据
        ArrayList<String> keyList = new ArrayList<>();
        keyList.add("company_name");
        keyList.add("public_account");
        keyList.add("bank_branch");
        keyList.add("license_num");
        keyList.add("user_name");
        keyList.add("phone");
        /**
         * user_id	是	string[18]用户ID
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id)
         */
        ArrayList<String> valueList = new ArrayList<>();
        for(int i = 0;i<items.length;i++){
            if(!TextUtils.isEmpty(items[i].getEditTextValue())){
                valueList.add(items[i].getEditTextValue());
            }else{
                valueList.add("");
            }
        }
        HashMap<String,String> data = new HashMap<>();
        for(int i = 0;i<items.length;i++){
            if(!TextUtils.isEmpty(valueList.get(i))){
                data.put(keyList.get(i),valueList.get(i));
            }
        }
        /**
         * user_id	是	string[18]用户ID1
         company_name	是	string[18]公司名称1
         public_account	是	int[6]对公账户1
         bank_branch	是	int[6]开户支行1
         license_num	是	int[6]营业执照注册号1
         license_img	是	int[6]营业执照图片地址1
         user_name	是	int[6]联系人姓名1
         phone	是	int[6]联系方式1
         F	是	string[18]请求来源：IOS/ANDROID/WEB1
         V	是	string[20]版本号如：1.0.11
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id)
         */
        data.put("user_id",AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("F",Constant.F);
        data.put("V",Constant.V);
        data.put("license_img",ImageUrl);
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(Constant.F+Constant.V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_COMPANY_AUTH,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(s);
                            if("0".equals(object.getString("code"))){
                                AppManager.getInstance(BusinessAttestationActivity.this).getUserInfo().setCompany_auth("2");
                                showAuthingDialog(BusinessAttestationActivity.this);
                            }else{
                                iscommit = false;
                                MsgUtil.shortToastInCenter(BusinessAttestationActivity.this,object.getString("msg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        items = null;
        keyList.clear();
        keyList = null;
        valueList.clear();
        valueList = null;
        data.clear();
        data = null;
    }

    private Dialog dialog;
    private void initDialog(Context context) {

        dialog = new Dialog(context, R.style.progress_dialog);
        dialog.setContentView(R.layout.dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) dialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("加载中...");
    }

    public static void showAuthingDialog(final Activity activity){
        new MyCustomDialogDialog(8, activity, R.style.MyDialog, "提交认证成功,预计需要1-3个\n工作日,请耐心等候!", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
                activity.onBackPressed();
            }

            @Override
            public void twoClick(Dialog dialog) {

            }
        }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults != null && grantResults.length>0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                takePhote();
            }else{
                MsgUtil.shortToast(this,"请前往手机设置打开手机定位权限!");
            }
        }
    }
}

