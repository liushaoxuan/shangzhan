package com.wyu.iwork.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.UniversalCallback;
import com.wyu.iwork.util.Constant;

/**
 * @author HuaJian Jiang.
 *         Date 2017/1/11.
 */
public class MsgDialog extends DialogFragment
        implements DialogInterface.OnShowListener, DialogInterface.OnClickListener
{
    private static final String TAG = MsgDialog.class.getSimpleName();
    private UniversalCallback mCallback;
    private Bundle mArgs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgs = getArguments();
    }

    public void setCallback(UniversalCallback mCallback) {
        this.mCallback = mCallback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new AlertDialog.Builder(getActivity()).setPositiveButton(R.string.ok, this)
                .setNegativeButton(R.string.no, this).setMessage(mArgs.getInt(Constant.KEY_MSG))
                .create();
        dialog.setOnShowListener(this);
        return dialog;
    }

    @Override
    public void onShow(DialogInterface dialog) {

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (mCallback == null) return;
        if (which == DialogInterface.BUTTON_POSITIVE) {
            mCallback.onFinished(DialogInterface.BUTTON_POSITIVE);
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            mCallback.onFinished(DialogInterface.BUTTON_NEGATIVE);
        }
    }

}
