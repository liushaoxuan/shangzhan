package com.wyu.iwork.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.view.activity.BaseActivity;
import com.wyu.iwork.view.dialog.NewVersionDialog;
import com.wyu.iwork.view.dialog.UpLoadProgressDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class UpdateManager {

    private BaseActivity mContext;


    //返回的安装包url
    private String apkUrl = "";


    private NewVersionDialog dialog;
    private UpLoadProgressDialog upLoadDialog;

    private Dialog downloadDialog;
    /* 下载包安装路径 */
    private static final String savePath = "/sdcard/iwork/";

    private static final String saveFileName = savePath + "Iwork.apk";

    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;


    private static final int DOWN_UPDATE = 1;

    private static final int DOWN_OVER = 2;

    private int progress;

    private Thread downLoadThread;

    private boolean interceptFlag = false;

/*    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE://下载中
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER: //下载完成
                    downloadDialog.dismiss();//下载完成关闭进度条对话框
                    installApk();
                    break;
                default:
                    break;
            }
        }

        ;
    };*/

    public UpdateManager(BaseActivity context, String url) {
        this.mContext = context;
        this.apkUrl = url;
    }

    //外部接口让主Activity调用
    public void checkUpdateInfo() {
        showNoticeDialog();
    }


    private void showNoticeDialog() {

        dialog = new NewVersionDialog(mContext, new NewVersionDialog.DialogInterface() {
            @Override
            public void onDownLoad(Dialog dialog) {
                dialog.dismiss();
                showDownloadDialog();
            }
        });
        dialog.show();
    }

    private void showDownloadDialog() {
        if (upLoadDialog==null) {
            upLoadDialog = new UpLoadProgressDialog(mContext, apkUrl);
        }
        upLoadDialog.showproDialog();
    }
/*
    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkUrl);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    //更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        //下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);//点击取消就停止下载.

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    *//**
     * 下载apk
     *//*

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    *//**
     * 安装apk
     *//*
    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);

    }*/
}
