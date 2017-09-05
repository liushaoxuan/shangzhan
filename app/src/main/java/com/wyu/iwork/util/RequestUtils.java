package com.wyu.iwork.util;
import android.text.TextUtils;

import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by lx on 2016/10/24.
 */
public class RequestUtils {


    /**
     * get方式URL拼接
     * @param url
     * @param map
     * @return
     */
    public static String getRequestUrl(String url, Map<String, String> map) {
        if (map == null || map.size() == 0) {
            return url;
        } else {
            StringBuilder newUrl = new StringBuilder(url);
            if (url.indexOf("?") == -1) {
                newUrl.append("?");
            }

            int i = 0;
            for (Map.Entry<String, String> item : map.entrySet()) {
                if (false == TextUtils.isEmpty(item.getKey().trim())) {
                    try {
                        if (i==0){
                            newUrl.append( item.getKey().trim() + "=" + URLEncoder.encode(String.valueOf(item.getValue().trim()), "UTF-8"));
                        }else {
                            newUrl.append("&" + item.getKey().trim() + "=" + URLEncoder.encode(String.valueOf(item.getValue().trim()), "UTF-8"));
                        }
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return newUrl.toString();
        }
    }
}
