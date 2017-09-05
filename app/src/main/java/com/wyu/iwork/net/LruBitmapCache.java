package com.wyu.iwork.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by jhj_Plus on 2016/10/25.
 */
public class LruBitmapCache extends LruCache<String,Bitmap> implements ImageLoader.ImageCache {
    private static final String TAG = "LruBitmapCache";

    public LruBitmapCache(Context context) {
        this(getCacheSize(context));
    }

    public LruBitmapCache(int maxSize) {
        super(maxSize);
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        if (get(url) == null) {
            put(url, bitmap);
        }
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    public static int getCacheSize(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        final int screenWidth = displayMetrics.widthPixels;
        final int screenHeight = displayMetrics.heightPixels;
        //手机屏幕占用的字节数
        final int screenBytes = screenWidth * screenHeight * 4;
        //返回3个屏幕字节数大小缓存
        return screenBytes * 3;
    }
}
