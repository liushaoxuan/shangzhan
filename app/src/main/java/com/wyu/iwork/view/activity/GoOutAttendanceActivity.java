package com.wyu.iwork.view.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.UnixStamp;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.widget.MyCustomDialogDialog;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.wyu.iwork.util.Constant.F;
import static com.wyu.iwork.util.Constant.V;

/**
 * 外出考勤
 */
public class GoOutAttendanceActivity extends BaseActivity {

    private static final String TAG = GoOutAttendanceActivity.class.getSimpleName();

    //外出时间
    @BindView(R.id.go_out_time)
    TextView go_out_time;

    //外出地点
    @BindView(R.id.go_out_address)
    TextView go_out_address;

    @BindView(R.id.go_address)
    AutoLinearLayout go_address;

    //外出事由
    @BindView(R.id.go_out_for)
    EditText go_out_for;

    //拍照
    @BindView(R.id.takephoto)
    ImageView takephoto;

    @BindView(R.id.ll_back)
    AutoLinearLayout back;

    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.tv_edit)
    TextView edit;

    private String filepath;

    private static final int REQUEST_CAPTURE = 1001;//拍照 请求码
    private String ImageUrl;//上传后的图片地址

    private File file;

    private boolean iscommit = false;

    //定位的经纬度值 以及地址
    private String longitude;
    private String latitude;
    private String address;
    private String time;
    private static Bitmap resizedBitmap;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_out_attendance);
        hideToolbar();
        ButterKnife.bind(this);
        initView();
        getExtras();
    }

    private void getExtras(){
        Intent intent = getIntent();
        longitude = intent.getStringExtra("longitude");
        latitude = intent.getStringExtra("latitude");
        address = intent.getStringExtra("address");
        //获取并设置当前时间
        getCurrentTime();
        go_out_address.setText(address);

    }

    //获取并设置当前时间
    private void getCurrentTime(){
        /**
         * URL_NOW_UNIX_TIME
         * user_id	是	int[180]用户ID
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id());
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_NOW_UNIX_TIME,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s, call, response);
                        Gson gson = null;
                        try {
                            gson = new Gson();
                            UnixStamp stamp = gson.fromJson(s,UnixStamp.class);
                            if("0".equals(stamp.getCode())){
                                time = new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date(stamp.getData().getUnix_time() * 1000));
                                go_out_time.setText(time);
                            }else {
                                MsgUtil.shortToastInCenter(GoOutAttendanceActivity.this,stamp.getMsg());
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

    private void initView(){
        title.setText("外出考勤");
        edit.setText("确定");
        edit.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.takephoto,R.id.ll_back,R.id.tv_edit})
    void Click(View v){
        switch (v.getId()){
            case R.id.takephoto:
                takePhoto();
                break;
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.tv_edit:
                //提交数据
                iscommit = true;
                if(!TextUtils.isEmpty(filepath) && file != null){
                    uploadImage();
                }else if(!TextUtils.isEmpty(filepath)){
                    MsgUtil.shortToastInCenter(this,"正在压缩图片中，请稍候!");
                }else{
                    checkData();
                }
                break;
        }
    }

    private void checkData(){
        if(TextUtils.isEmpty(go_out_for.getText().toString())){
           //
            showRedmineDialog("您的外出事由未填写完整\n请完成填写并提交");
        }else if(TextUtils.isEmpty(filepath)){
            //
            showRedmineDialog("请选择图片并上传");
        }else{
            commitSignData();
        }
    }

    //提醒弹窗//"您的任务信息未填写完整\n请完成填写并提交"
    private void showRedmineDialog(String str){
        new MyCustomDialogDialog(5, this, R.style.MyDialog, str, new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {

            }
        }).show();
    }

    private void commitSignData(){
        /**
         * user_id	    是   	int[11]             用户id
         status	          是   	int[2]              签到状态，0：外勤签到，1：正常签到
         longitude	    是   	decimal(10,6)       所在地坐标经度,百度坐标bd09ll，格式：22.123456 ，注意必须小于等于6位小数,定位失败传361
         latitude	          是   	decimal(10,6)       所在地坐标纬度,百度坐标，格式：33.123456 ，同上
         building	          否   	string[50]          建筑物名称
         address	          否   	string[50]          具体地点,外勤时填写外勤地址，签到时记录当前坐标地址
         visit	          否   	string[50]          外出事由
         file	          否   	string[140]         外出考勤图片
         F	                是   	string[18]          请求来源：IOS/ANDROID/WEB
         V	                是   	string[20]          版本号如：1.0.1
         RandStr	          是   	string[50]          请求加密随机数 time().|.rand()
         Sign	          是   	string[400]         请求加密值 F_moffice_encode(F.V.RandStr.user_id.status.longitude.latitude)
         */
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("status","0");
        data.put("longitude",longitude);
        data.put("latitude",latitude);
        data.put("address",address);
        data.put("visit",go_out_for.getText().toString());
        data.put("file",ImageUrl);
        data.put("F", F);
        data.put("V", V);
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id()
                    +"0"+longitude+latitude);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_SING,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s, call, response);
                        Logger.i(TAG,s);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(s);
                            if("0".equals(object.getString("code"))){
                                jumpToSuccess();
                            }else{
                                MsgUtil.shortToastInCenter(GoOutAttendanceActivity.this,object.getString("msg"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        iscommit = false;
                    }
                });
        if(data != null){
            data.clear();
            data = null;
        }
    }

    private void jumpToSuccess(){
        Intent intent = new Intent(this,OutSignSuccessActivity.class);
        intent.putExtra("address",address);
        startActivity(intent);
        onBackPressed();
    }

    //上传图片
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
                                commitSignData();
                            }else{
                                iscommit = false;
                                MsgUtil.shortToastInCenter(GoOutAttendanceActivity.this,object.getString("msg"));
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

    //拍照
    private void takePhoto(){
        try {
        Intent intentPhote = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentPhote.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        filepath = getPhotopath();
        File out = new File(filepath);
        //Uri uri = Uri.fromFile(out);
        // 获取拍照后未压缩的原图片，并保存在uri路径中
        Uri uri ;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, out.getAbsolutePath());
            uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
        }else{
            uri = Uri.fromFile(out);
        }
        intentPhote.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intentPhote, REQUEST_CAPTURE);


     }catch (Exception e){
            MsgUtil.shortToastInCenter(this,"请前往手机设置检查拍照权限是否打开!");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK){
            try {
                    //Bitmap bitmap = BitmapFactory.decodeFile(filepath);
                    //takephoto.setImageBitmap(bitmap);
                    optionPicture(filepath);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 上传图片  先压缩图片到100k，再上传
     */

    private void compressBitmap(final Bitmap bitmap){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Logger.i("compress",BitmapFactory.decodeFile(filepath).getByteCount()+"");
                Logger.i("compress","filepath=="+filepath);
                //file = CustomUtils.ratio(BitmapFactory.decodeFile(filepath),GoOutAttendanceActivity.this,200);
                file = CustomUtils.ratio(bitmap,GoOutAttendanceActivity.this,200);
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
    }

    private void optionPicture(String filepath){
        /**
         * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
         */
        int degree = readPictureDegree(filepath);

        BitmapFactory.Options opts=new BitmapFactory.Options();//获取缩略图显示到屏幕上
        opts.inSampleSize=2;
        Bitmap cbitmap=BitmapFactory.decodeFile(filepath);

        /**
         * 把图片旋转为正的方向
         */
        Bitmap newbitmap = rotaingImageView(degree, cbitmap);
        takephoto.setImageBitmap(newbitmap);
    }

    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public int readPictureDegree(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    /*
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public Bitmap rotaingImageView(int angle , Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();;
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        compressBitmap(resizedBitmap);
        return resizedBitmap;
    }
}
