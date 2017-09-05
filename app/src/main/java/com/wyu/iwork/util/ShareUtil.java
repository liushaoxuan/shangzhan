package com.wyu.iwork.util;

import android.app.Dialog;
import android.content.Context;

import com.wyu.iwork.AppManager;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.model.Application;
import com.wyu.iwork.model.UserInfo;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by lx on 2017/4/15.
 */

public class ShareUtil {

    //微信好友APP分享
    public static void shareAppWechat(final Dialog dialog, final Context context){
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        /**
         * shareType(Platform.SHARE_WEBPAGE)	title	text(朋友圈不显示此字段)	imagePath	 url
         */

        UserInfo  userInfo = MyApplication.userInfo;
            if (userInfo==null){
                userInfo = new UserInfo();
            }
        sp.setTitle(userInfo.getShare().getTitle());//标题
        sp.setText(userInfo.getShare().getIntro());//文字
        sp.setImageUrl(userInfo.getShare().getIcon());//跳转的链接
        sp.setUrl(userInfo.getShare().getUrl());
        Logger.i("sharesdk_wechat","title="+userInfo.getShare().getTitle()+"  text="+userInfo.getShare().getIntro()+"  url="+
                userInfo.getShare().getUrl()+" icon="+userInfo.getShare().getIcon());
        Platform sin = ShareSDK.getPlatform(Wechat.NAME);
        sin.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                if(dialog!=null){
                    dialog.dismiss();
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                if(dialog!=null){
                    dialog.dismiss();
                }
                MsgUtil.shortToastInCenter(context,"分享失败,请重试!");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
        });
        sin.share(sp);
    }

    //微信朋友圈APP分享
    public static void shareAppWechatFriend(final Dialog dialog, final Context context){
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        UserInfo  userInfo = MyApplication.userInfo;
        sp.setTitle(userInfo.getShare().getTitle());//标题
        sp.setText(userInfo.getShare().getIntro());//文字
        sp.setUrl(userInfo.getShare().getUrl());//跳转的链接
        sp.setImageUrl(userInfo.getShare().getIcon());//图片url
        Platform sin = ShareSDK.getPlatform(WechatMoments.NAME);
        Logger.i("sharesdk_wechatmoments","title="+userInfo.getShare().getTitle()+"  text="+userInfo.getShare().getIntro()+"  url="+
                userInfo.getShare().getUrl()+" icon="+userInfo.getShare().getIcon());
        sin.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                if(dialog!=null){
                    dialog.dismiss();
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                if(dialog!=null){
                    dialog.dismiss();
                }
                MsgUtil.shortToastInCenter(context,"分享失败,请重试!");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
        });
        sin.share(sp);
    }

    //QQ好友APP分享
    public static void shareAppQQ(final Dialog dialog, final Context context){
        Platform.ShareParams sp = new Platform.ShareParams();
        UserInfo  userInfo = MyApplication.userInfo;
        sp.setTitle(userInfo.getShare().getTitle());//标题
        sp.setText(userInfo.getShare().getIntro());//文字
        sp.setTitleUrl(userInfo.getShare().getUrl());//跳转的链接
        sp.setImageUrl(userInfo.getShare().getIcon());//图片url
        Platform sin = ShareSDK.getPlatform(QQ.NAME);
        Logger.i("sharesdk_qq","title="+userInfo.getShare().getTitle()+"  text="+userInfo.getShare().getIntro()+"  url="+
                userInfo.getShare().getUrl()+" icon="+userInfo.getShare().getIcon());
        sin.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                if(dialog!=null){
                    dialog.dismiss();
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                if(dialog!=null){
                    dialog.dismiss();
                }
                MsgUtil.shortToastInCenter(context,"分享失败,请重试!");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
        });
        sin.share(sp);
    }

    //QQ空间APP分享
    public static void shareAppQZone(final Dialog dialog, final Context context){
        Platform.ShareParams sp = new Platform.ShareParams();
        UserInfo  userInfo = MyApplication.userInfo;
        sp.setTitle(userInfo.getShare().getTitle());//标题
        sp.setTitleUrl(userInfo.getShare().getUrl());
        sp.setText(userInfo.getShare().getIntro());//文字
        sp.setImageUrl(userInfo.getShare().getIcon());//图片url
        sp.setSite("商栈");
        sp.setSiteUrl(userInfo.getShare().getUrl());
        Platform sin = ShareSDK.getPlatform(QZone.NAME);
        /**
         * sp.setTitle(title);
         sp.setTitleUrl(url);
         sp.setText(text);
         sp.setImagePath(ImageUrl);
         sp.setSite("软融");
         sp.setSiteUrl ;
         Platform sin = ShareSDK.getPlatform(QZone.NAME);
         */
        Logger.i("sharesdk_qzone","title="+userInfo.getShare().getTitle()+"  text="+userInfo.getShare().getIntro()+"  url="+
                userInfo.getShare().getUrl()+" icon="+userInfo.getShare().getIcon());
        sin.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                if(dialog!=null){
                    dialog.dismiss();
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                if(dialog!=null){
                    dialog.dismiss();
                }
                MsgUtil.shortToastInCenter(context,"分享失败,请重试!");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
        });
        sin.share(sp);
    }
/************************************************************************************************************************************************/
    public static void shareCardWechat(final Dialog dialog, final Context context,String title,String text,String url,String ImageUrl){
        Logger.i("SHARE","title="+title+"text="+text+"url="+url+"ImageUrl="+ImageUrl);
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(title);
        sp.setText(text);
        sp.setUrl(url);
        sp.setImageUrl(ImageUrl);
        Platform sin = ShareSDK.getPlatform(Wechat.NAME);
        sin.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                if(dialog!=null){
                    dialog.dismiss();
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                if(dialog!=null){
                    dialog.dismiss();
                }
                MsgUtil.shortToastInCenter(context,"分享失败,请重试!");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
        });
        sin.share(sp);
    }

    public static void shareCardWechatFriend(final Dialog dialog, final Context context,String title,String text,String url,String ImageUrl){
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(title);
        sp.setText(text);
        sp.setUrl(url);
        sp.setImageUrl(ImageUrl);
        Platform sin = ShareSDK.getPlatform(WechatMoments.NAME);
        sin.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                if(dialog!=null){
                    dialog.dismiss();
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                if(dialog!=null){
                    dialog.dismiss();
                }
                MsgUtil.shortToastInCenter(context,"分享失败,请重试!");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
        });
        sin.share(sp);
    }

    public static void shareCardQQ(final Context context,final Dialog dialog,String title,String text,String url,String ImageUrl){
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(title);
        sp.setTitleUrl(url);
        sp.setText(text);
        sp.setImageUrl(ImageUrl);
        Platform sin = ShareSDK.getPlatform(QQ.NAME);
        sin.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                if(dialog!=null){
                    dialog.dismiss();
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                if(dialog!=null){
                    dialog.dismiss();
                }
                MsgUtil.shortToastInCenter(context,"分享失败,请重试!");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
        });
        sin.share(sp);
    }
    //title	titleUrl	text	imagePath	site	siteUrl

    public static void shareCardQZone(final Context context,final Dialog dialog,String title,String text,String url,String ImageUrl){
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(title);
        sp.setTitleUrl(url);
        sp.setText(text);
        sp.setImagePath(ImageUrl);
        sp.setSite("商栈");
        sp.setSiteUrl(url);
        Platform sin = ShareSDK.getPlatform(QZone.NAME);
        sin.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                if(dialog!=null){
                    dialog.dismiss();
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                if(dialog!=null){
                    dialog.dismiss();
                }
                MsgUtil.shortToastInCenter(context,"分享失败,请重试!");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
        });
        sin.share(sp);
    }


}
