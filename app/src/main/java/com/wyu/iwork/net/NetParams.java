package com.wyu.iwork.net;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by jhj_Plus on 2016/11/11.
 */
public class NetParams {

    public static class Builder {
        Map<String, String> params = new LinkedHashMap<>();

        public Builder buildParams(String key, String value) {
            params.put(key, value);
            return this;
        }

        public Map<String, String> build() {
            return params;
        }
    }
}
