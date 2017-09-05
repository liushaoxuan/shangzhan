package com.wyu.iwork.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.ThumbnailUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者： sxliu on 2017/6/15.13:43
 * 邮箱：2587294424@qq.com
 * 生成二维码和添加logo
 */

public class ZxingCodeUtils {

    /**
     * 生成二维码
     *
     * @param content
     * @param width
     * @param height
     * @return
     */
    public static Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 在二维码中间添加Logo图案
     */
    public static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }
        if (logo == null) {
            return src;
        }
        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();
        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }
        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }   //logo大小为二维码整体大小的1/8
        float scaleFactor = srcWidth * 1.0f / 8 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }


    /**
     * 得到本地或者网络上的bitmap url - 网络或者本地图片的绝对路径,比如:    *    * A.网络路径: url="http://blog.foreverlove.us/girl2.png" ;    *    * B.本地路径:url="file://mnt/sdcard/photo/image.png";    *    * C.支持的图片格式 ,png, jpg,bmp,gif等等    *    * @param url    * @return
     */
    public static Bitmap getBitmap(String url) {
        URL imageURL = null;
        Bitmap bitmap = null;
        Logger.e("inuni","URL = "+url);
        try {
            imageURL = new URL(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) imageURL
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    //给logo加上边框边框
    private static Bitmap setSide(Bitmap logoBitmap){
        int bgWidth = logoBitmap.getWidth()*4/3;
        int bgHeigh = logoBitmap.getHeight()*4/3;
        Bitmap bgBitmap =Bitmap.createBitmap(bgWidth, bgHeigh, Bitmap.Config.ARGB_8888);
        //通过ThumbnailUtils压缩原图片，并指定宽高为背景图的3/4
        logoBitmap = ThumbnailUtils.extractThumbnail(logoBitmap,bgWidth*3/4, bgHeigh*3/4, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        Bitmap cvBitmap = Bitmap.createBitmap(bgWidth, bgHeigh, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(cvBitmap);
        // 开始合成图片
        canvas.drawBitmap(bgBitmap, 0, 0, null);
        canvas.drawBitmap(logoBitmap,(bgWidth - logoBitmap.getWidth()) /2,(bgHeigh - logoBitmap.getHeight()) / 2, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);// 保存
        canvas.restore();
        if(cvBitmap.isRecycled()){
            cvBitmap.recycle();
        }
        return cvBitmap;
    }
}
