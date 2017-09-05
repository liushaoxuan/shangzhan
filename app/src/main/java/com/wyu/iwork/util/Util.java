package com.wyu.iwork.util;

import android.content.Context;

/**
 * Created by jhj_Plus on 2016/10/26.
 */
public class Util {
    private static final String TAG = "Util";

    public static float dp2px(Context context, int value) {
        return context.getResources().getDisplayMetrics().density * value;
    }

}
