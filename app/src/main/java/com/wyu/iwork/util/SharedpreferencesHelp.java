package com.wyu.iwork.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by dingxueru on 2016/7/4 0004.
 * 保存数据到本地
 */
public class SharedpreferencesHelp {
    /**
     * 保存文件到本地
     * @param context
     * @param fileName
     *  key
     * value
     */
    public static void save(Context context,String fileName,Map<String, String> map){
        SharedPreferences preferences = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            editor.putString(key, value);
        }
        editor.commit();
    }


    /**
     * 删除本地文件，根据文件名
     * @param context
     * @param fileName
     */
    public static void delete(Context context,String fileName){
        SharedPreferences preferences = context.getSharedPreferences(fileName,Activity.MODE_PRIVATE);
        preferences.edit().clear().commit();
    }

    /**
     * 根据传过来的key值查找对应的数据
     * @param context
     * @param fileName
     * @param key
     * @return
     */
    public static List<String> read(Context context,String fileName,String... key){
        SharedPreferences preferences = context.getSharedPreferences(fileName,Activity.MODE_PRIVATE);
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < key.length; i++) {
            String	value = preferences.getString(key[i], "");
            list.add(value);
        }
        return list;
    }
}
