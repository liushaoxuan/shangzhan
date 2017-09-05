package com.wyu.iwork.interfaces;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.request.BaseRequest;
import com.wyu.iwork.R;
import com.wyu.iwork.util.MsgUtil;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lx on 2017/5/8.
 */

public abstract class DialogCallback  extends AbsCallback<String> {
//    private ProgressDialog dialog;

    private Context context;
    private Dialog dialog;

    private void initDialog(Context context) {
        this.context = context;

        dialog = new Dialog(context, R.style.progress_dialog);
        dialog.setContentView(R.layout.dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) dialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("加载中...");
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface mdialog, int keyCode, KeyEvent event) {
                if (keyCode==KeyEvent.KEYCODE_BACK&& dialog.isShowing()){
                    OkGo.getInstance().cancelAll();
                    dialog.dismiss();
                }
                return false;
            }
        });

    }

    public DialogCallback(Context activity) {
        super();
        initDialog(activity);
    }

    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        //网络请求前显示对话框
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void onAfter(@Nullable String t, @Nullable Exception e) {
        super.onAfter(t, e);
        //网络请求结束后关闭对话框
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public String convertSuccess(Response response) throws Exception {
        dialog.dismiss();
        String s = StringConvert.create().convertSuccess(response);
        response.close();
        return s;
    }

    @Override
    public void onSuccess(String s, Call call, Response response) {
        dialog.dismiss();
    }

    @Override
    public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
        dialog.dismiss();
        MsgUtil.shortToast(context,"网络错误,请稍后重试!");
    }

    @Override
    public void parseError(Call call, Exception e) {
        super.parseError(call, e);
        dialog.dismiss();
    }

}
