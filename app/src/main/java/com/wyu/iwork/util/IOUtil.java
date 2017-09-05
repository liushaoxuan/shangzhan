package com.wyu.iwork.util;

import android.database.Cursor;

import java.io.Closeable;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/26.
 */
public class IOUtil {
    private static final String TAG = IOUtil.class.getSimpleName();

    private IOUtil() {
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable ignored) {
                Logger.e(TAG,ignored.getMessage());
            }
        }
    }

    public static void closeQuietly(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Throwable ignored) {
                Logger.e(TAG, ignored.getMessage());
            }
        }
    }
}
