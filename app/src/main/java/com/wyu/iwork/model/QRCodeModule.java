package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by lx on 2017/4/17.
 */

public class QRCodeModule implements Serializable{

    /**
     * {"code":0,"msg":"\u8bf7\u6c42\u6210\u529f\uff01","data":{"type":2,"text":"23"}}
     */

    private int code;
    private String msg;

    private Data data;
    
    public class Data implements Serializable{

        private String type,text;

        public String getType() {
            return type;
        }

        public String getText() {
            return text;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "type='" + type + '\'' +
                    ", text='" + text + '\'' +
                    '}';
        }
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Data getData() {
        return data;
    }

    @Override
    public String toString() {
        return "QRCodeModule{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
