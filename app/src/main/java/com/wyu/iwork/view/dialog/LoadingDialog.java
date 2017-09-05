package com.wyu.iwork.view.dialog;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.OnBackPressedCallback;
import com.wyu.iwork.util.Logger;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/27.
 */
public class LoadingDialog extends DialogFragment implements DialogInterface.OnKeyListener {
    private static final String TAG = LoadingDialog.class.getSimpleName();
    private OnBackPressedCallback mBackPressedCallback;

    public void setBackPressedCallback(OnBackPressedCallback backPressedCallback)
    {
        this.mBackPressedCallback = backPressedCallback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new AlertDialog.Builder(getActivity(), R.style.AppTheme_Dialog_Loading).setView(R.layout.part_loading).create();
        setCancelable(false);
        dialog.setOnKeyListener(this);
        return dialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        Logger.e(TAG,"LoadingDialog_onCancel");
        super.onCancel(dialog);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Logger.e(TAG, "LoadingDialog_onDismiss");
        super.onDismiss(dialog);
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mBackPressedCallback != null) {
            if (mBackPressedCallback.onBackPressed()) {
                dismiss();
                return true;
            }
        }else if (keyCode==KeyEvent.KEYCODE_BACK){
            dismiss();
            return true;
        }
        return false;
    }
}
