package com.wyu.iwork.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wyu.iwork.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 作者： sxliu on 2017/8/25.13:57
 * 邮箱：2587294424@qq.com
 */

public class UpLoadProgressDialog extends Dialog implements View.OnClickListener{
    private Activity context;
    //取消按钮
    private TextView cancel;
    private DialogInterface minterface;
    private Thread downLoadThread;
    //返回的安装包url
    private String apkUrl = "";
    /* 下载包安装路径 */
    private static final String savePath = "/sdcard/iwork/";

    private static final String saveFileName = savePath + "Iwork.apk";
    private int progress;

    private static final int DOWN_UPDATE = 1;

    private static final int DOWN_OVER = 2;
    /**
     * 进度条
     */
    public ProgressBar progressBar;

    private boolean interceptFlag = false;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE://下载中
                    progressBar.setProgress(progress);
                    break;
                case DOWN_OVER: //下载完成
                    dismiss();//下载完成关闭进度条对话框
                    installApk();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public UpLoadProgressDialog(Activity context,String apkUrl) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.apkUrl = apkUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_upload_progress);
        cancel = (TextView) findViewById(R.id.dialog_upload_progress_cancel);
        progressBar = (ProgressBar) findViewById(R.id.dialog_upload_progress_progressbar);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        minterface.cancelDownload(this);
    }


    public interface DialogInterface{
        void cancelDownload(Dialog dialog);
    }

    public void showproDialog(){
        show();
        downloadApk();
    }

    /**
     * 下载apk
     */

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }


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

    /**
     * 安装apk
     */
    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        context.startActivity(i);

    }
}
