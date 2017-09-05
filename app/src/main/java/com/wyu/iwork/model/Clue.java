package com.wyu.iwork.model;

import java.util.ArrayList;

/**
 * Created by lx on 2017/6/14.
 */

public class Clue {

    private String code;
    private String msg;
    private ArrayList<ClueMessage> data;

    public class ClueMessage{
        private String id; // 线索ID
        private String name; //  姓名
        private String source_type; // 线索来源 1：其他 2：搜索引擎 3：客户介绍 4：会议 5：广告 6：电话 7：网站
        private String add_time; //  添加时间 格式：2017-01-12 12:12:12
        private String phone;

        public String getPhone() {
            return phone;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getSource_type() {
            return source_type;
        }

        public String getAdd_time() {
            return add_time;
        }

        @Override
        public String toString() {
            return "ClueMessage{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", source_type='" + source_type + '\'' +
                    ", add_time='" + add_time + '\'' +
                    '}';
        }
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<ClueMessage> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Clue{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
