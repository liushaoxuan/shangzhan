package com.wyu.iwork.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lx on 2017/6/14.
 */

public class Marketing implements Serializable{

    private String code;
    private String msg;
    private ArrayList<MarketingDetail> data;

    public class MarketingDetail implements Serializable{

        private String id ; //  活动ID
        private String title ; //  活动名称
        private String type ; //  活动类型 1:其他 2:促销活动 3:品牌活动 4:会议销售 5:搜索引擎 6:广告 7:营销
        private String start_time ; //  开始时间 格式：2017-01-02
        private String end_time ; //  结束时间 格式：2017-01-02
        private String add_time ; //  添加时间 格式：2017-01-12 12:12:12
        private boolean isSelect;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public String getType() {
            return type;
        }

        public String getStart_time() {
            return start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public String getAdd_time() {
            return add_time;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        @Override
        public String toString() {
            return "MarketingDetail{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    ", start_time='" + start_time + '\'' +
                    ", end_time='" + end_time + '\'' +
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

    public ArrayList<MarketingDetail> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Marketing{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
