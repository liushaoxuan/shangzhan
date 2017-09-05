package com.wyu.iwork.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.UniversalCallback;
import com.wyu.iwork.util.Constant;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/29.
 */
public class InputDialog extends DialogFragment
        implements DialogInterface.OnShowListener, DialogInterface.OnClickListener
{
    private static final String TAG = InputDialog.class.getSimpleName();
    private UniversalCallback mCallback;
    private EditText mInput;
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
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setView(R.layout.dialog_input)
                .setPositiveButton(R.string.ok,this)
                .setNegativeButton(R.string.no,this)
                .create();
        dialog.setOnShowListener(this);
        return dialog;
    }

    @Override
    public void onShow(DialogInterface dialog) {
        mInput = (EditText) getDialog().findViewById(R.id.input);
        mInput.setHint(mArgs != null ? mArgs.getString(Constant.KEY_PARAM, "") : "");
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (mCallback==null) return;
        if (which == DialogInterface.BUTTON_POSITIVE) {
            final int id = mArgs != null ? mArgs.getInt(Constant.KEY_ID, -1) : -1;
            mCallback.onFinished(DialogInterface.BUTTON_POSITIVE, id, mInput.getText().toString());
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            mCallback.onFinished(DialogInterface.BUTTON_NEGATIVE);
        }
    }
}
