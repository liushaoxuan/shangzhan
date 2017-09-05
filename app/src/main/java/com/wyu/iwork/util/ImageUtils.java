package com.wyu.iwork.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * 作者： sxliu on 2017/8/17.09:43
 * 邮箱：2587294424@qq.com
 */

public class ImageUtils {

    public static void LoadImageFitWH(Context mContext, String imgUrl, final ImageView imageView){
        if (imgUrl.toUpperCase().endsWith(".GIF")) {
            Glide.with(mContext)
                    .load(imgUrl)
                    .asGif()
//                    .dontAnimate() //去掉显示动画
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE) //DiskCacheStrategy.NONE
                    .into(imageView);
        } else {
            Glide.with(mContext)
                    .load(imgUrl)
                    .crossFade()
                    .into(imageView);
        }
    }


}
