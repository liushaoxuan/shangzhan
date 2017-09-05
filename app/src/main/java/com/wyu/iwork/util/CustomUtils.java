package com.wyu.iwork.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.view.activity.BusinessAttestationActivity;
import com.wyu.iwork.view.activity.TaskAssistantActivity;
import com.wyu.iwork.widget.MyCustomDialogDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lx on 2016/12/26.
 */

public class CustomUtils {
    private static final String TAG="CustomUtils";

        /*图片翻转90度*/
    public static Bitmap toturn(Bitmap bitmap){
        Matrix matrix = new Matrix();
        matrix.postRotate(90); /*翻转90度*/
            int width = bitmap.getWidth();
            int height =bitmap.getHeight();
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            return bitmap;
        }

    //Bitmap转换成byte[ ]
    public static byte[] getBytes(Bitmap bitmap){
        //实例化字节数组输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);//压缩位图
        return baos.toByteArray();//创建分配字节数组
    }

    //byte[ ]转换回来Bitmap
    public static Bitmap getBitmap(byte[] data){
        return BitmapFactory.decodeByteArray(data, 0, data.length);//从字节数组解码位图
    }

    //bitmap保存到本地相册
    public static File saveBitmap(Bitmap bitmap){
        String pat = "yyyyMMddHHmmssSSS";
        SimpleDateFormat sdf = new SimpleDateFormat(pat);
        String name = sdf.format(new Date()) + ".png";
        File file = new File("/sdcard/", name);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream out;
        try {
             out = new FileOutputStream(file);
             if(bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
             out.flush();
             out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File compressBitmap(Bitmap bitmap,Context context,int size){
        String pat = "yyyyMMddHHmmssSSS";
        SimpleDateFormat sdf = new SimpleDateFormat(pat);
        String name = sdf.format(new Date()) + ".png";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        int options = 100;
        while (baos.toByteArray().length / 1024 > size) {    // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 5;// 每次都减少5
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            Log.i("===length===",(baos.toByteArray().length / 1024)+"");
        }
        File file = new File(context.getExternalCacheDir().getAbsoluteFile()+name+".png");
        try{
            FileOutputStream fos=new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return file;
    }

    /**按高宽压缩*/
    public static Bitmap zoomImage(Bitmap bgimage) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) 300) / width;
        float scaleHeight = ((float)300) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    public static String changList(ArrayList list){
        String str="|";
        for(int i=0;i<list.size();i++){
            Map m= (Map) list.get(i);
            str+=m.get("id")+"|";
        }
        return str;
    }
    /**获取缓存*/
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    /**
     * 清除缓存
     * @param context
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    //获取随机数
    public static String getRandStr(){
        return System.currentTimeMillis()+"|"+ Md5Util.getRandom();
    }

    //获取短信验证码
    public static void getPhoneSign(String phoneNumber,Context context){
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+phoneNumber);
        HashMap<String,String> data = new HashMap<>();
        data.put("phone",phoneNumber);
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_GET_PHONE_SIGN,data);
        OkGo.get(murl).tag(context).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(context) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i("getphonesign",s);
                    }
                });


    }    //找回密码 获取短信验证码
    public static void getPhoneSign_SMS(String phoneNumber, final Context context,final TimeCount timeCount){
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+phoneNumber);
        HashMap<String,String> data = new HashMap<>();
        data.put("phone",phoneNumber);
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_FINDPASS_SMS,data);
        Log.e("找回密码",murl);
        OkGo.get(murl).tag(context).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(context) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i("getphonesign",s);
                        try {
                            JSONObject object = new JSONObject(s);
                            String code = object.optString("code");
                            String msg = object.optString("msg");
                            if ("0".equals(code)){
                                timeCount.start();
                            }
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


    }

    //尺寸压缩图片

    /**
     *
     * @param bitmap 将要压缩的图片
     *               默认将图片尺寸压缩到当前图片的一半
     * @return
     */
    public static File ratio(Bitmap bitmap,Context context,int size){
        Logger.i("compress","bitmapSize=="+bitmap.getByteCount());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,os);
        /**
        if(os.toByteArray().length/1024 > 100){//若图片大小大于100k，则对图片进行尺寸压缩，否则不进行压缩
            os.reset();//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,os);//只进行
        }*/
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片   此时把option.inJustDecodeBounds设置为true
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap image = BitmapFactory.decodeStream(is, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //float hh = w/2;// 设置高度为原图片的一半时，可以明显看到图片缩小了
        //float ww = w/2;// 设置宽度为原图片的一半，可以明显看到图片缩小了
        //宽>高  宽压缩到500 高按比例压缩
        float hh = 0;
        float ww = 0;
        if(w>h){
            ww = 500;
            hh = newOpts.outHeight*ww/newOpts.outWidth;
        }else{
            hh = 500;
            ww = newOpts.outWidth*hh/newOpts.outHeight;
        }
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        Logger.i("compress","size:ww"+ww+"hh:"+hh);
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        is = new ByteArrayInputStream(os.toByteArray());
        image = BitmapFactory.decodeStream(is, null, newOpts);
        //压缩好比例大小后再进行质量压缩
        //      return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        Logger.i("compress","imageSize=="+image.getByteCount());
        File file = compressBitmap(image,context,size);
        return file;
    }

    //时间判断
    public static boolean matchTime(String beginTime,String endTime){
        //切割时间
        try {
            String[] begin = beginTime.split(" ");
            String[] beginyear = begin[0].split("-");
            String[] beginDay = begin[1].split(":");
            String[] end = endTime.split(" ");
            String[] endYear = end[0].split("-");
            String[] endDay = end[1].split(":");
            //先判断年  再月。。。
            if(Integer.parseInt(endYear[0])>Integer.parseInt(beginyear[0])){
                //
                return true;
            }else{
                if(Integer.parseInt(endYear[0]) == Integer.parseInt(beginyear[0])){
                    //判断月
                    if(Integer.parseInt(endYear[1])>Integer.parseInt(beginyear[1])){
                        return true;
                    }else{
                        if(Integer.parseInt(endYear[1]) == Integer.parseInt(beginyear[1])){
                            //判断天
                            if(Integer.parseInt(endYear[2])>Integer.parseInt(beginyear[2])){
                                return true;
                            }else{
                                if(Integer.parseInt(endYear[2]) == Integer.parseInt(beginyear[2])){
                                    //判断时
                                    if(Integer.parseInt(endDay[0])>Integer.parseInt(beginDay[0])){
                                        return true;
                                    }else{
                                        if(Integer.parseInt(endDay[1])>Integer.parseInt(beginDay[1])){
                                            return true;
                                        }else{
                                            if(Integer.parseInt(endDay[1]) == Integer.parseInt(beginDay[1])){
                                                return false;
                                            }else{
                                                return false;
                                            }
                                        }
                                    }
                                }else{
                                    return false;
                                }
                            }
                        }else{
                            return false;
                        }
                    }
                }else{
                    return false;
                }
            }
        }catch (Exception e){
            return true;
        }
    }


    //企业认证提示框
    public static void showDialog(final Activity activity){
        new MyCustomDialogDialog(7, activity, R.style.MyDialog, "您的账号尚未完成企业认证\n完成认证可享受跟多特权\n是否认证？", "去认证", "残忍拒绝", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                //拒绝
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {
                //认证
                activity.startActivity(new Intent(activity, BusinessAttestationActivity.class));
                dialog.dismiss();
            }
        }).show();
    }

    public static void showAuthingDialog(Activity activity){
        new MyCustomDialogDialog(8, activity, R.style.MyDialog, "您的企业正在认证中,预计需要\n1-3个工作日,请耐心等候!", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {

            }
        }).show();
    }

    //判断是否已经绑定企业0:认证失败 1：认证成功 2：认证中 3：未认证
    public static boolean showDialogForBusiness(Activity activity){
        String type = MyApplication.userInfo.getCompany_auth();
        Logger.i(TAG,type);
        boolean isAuth = true;
        if("0".equals(type) || "3".equals(type)){
            isAuth = false;
            showDialog(activity);
        }else if("2".equals(type)){
            isAuth = false;
            showAuthingDialog(activity);
        }
        return isAuth;
    }

    //把格式是2012-12-02的日期串转为Date类型
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    //获取当前版本号
    public static String getVersionName(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }

}
