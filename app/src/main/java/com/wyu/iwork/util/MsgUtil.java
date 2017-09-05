package com.wyu.iwork.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import static android.widget.Toast.makeText;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/26.
 */
public class MsgUtil {
    private static final String TAG = MsgUtil.class.getSimpleName();

    public static void shortToast(Context ctxt, int txtResId) {
        shortToast(ctxt, ctxt.getString(txtResId));
    }

    public static void shortToast(Context ctxt, CharSequence txt) {
        if (ctxt == null) return;
        makeText(ctxt, txt, Toast.LENGTH_SHORT).show();
    }


    public static void longToast(Context ctxt, int txtResId) {
        longToast(ctxt, ctxt.getString(txtResId));
    }

    public static void longToast(Context ctxt, CharSequence txt) {
        if (ctxt == null) return;
        makeText(ctxt, txt, Toast.LENGTH_LONG).show();
    }

    /**
     * 吐司放在屏幕中间显示
     */
    public static void shortToastInCenter(Context ctxt, CharSequence txt) {
        if (ctxt == null) return;
        //Toast.makeText(ctxt, txt, Toast.LENGTH_SHORT).show();
        Toast toast = Toast.makeText(ctxt,txt,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public static void longToastInCenter(Context ctxt, CharSequence txt) {
        if (ctxt == null) return;
        //Toast.makeText(ctxt, txt, Toast.LENGTH_SHORT).show();
        Toast toast = Toast.makeText(ctxt,txt,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}
