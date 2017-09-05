package com.wyu.iwork.util;


import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jhj_Plus on 2016/11/6.
 */
public class DateUtil {
    private static final String TAG = "DateUtil";

    public static long getTimeMilliSecs() {
        return new Date().getTime();
    }

    public static long getTimeSecs() {
        return new Date().getTime() / 1000;
    }

    public static String formatDate2MD(long secs) {
        Date date = new Date(secs );
        return DateFormat.format("MM月dd日", date).toString();
    }

    public static String getCurrDate2MD(long secs) {
        Date date = new Date(secs  );
        return DateFormat.format("MM月dd日", date).toString();
    }

    public static String getCurrDate2YMD(long secs) {
        Date date = new Date(secs  );
        return DateFormat.format("yyyy年MM月dd日", date).toString();
    }

    public static String getCurrDate2YMDHM(long secs) {
        Date date = new Date(secs  );
        return DateFormat.format("yyyy年MM月dd日HH时mm分ss秒", date).toString();
    }

    public static String getCurrDate2HMS(long secs) {
        Date date = new Date(secs  );
        return DateFormat.format("HH:mm:ss", date).toString();
    }

    public static Date String2Date(String dateString){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String ChartTime(long secs) {
        Date date = new Date(secs  );
        return DateFormat.format("yyyy/MM/dd", date).toString();
    }
}
