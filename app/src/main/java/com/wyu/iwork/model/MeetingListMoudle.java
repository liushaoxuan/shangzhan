package com.wyu.iwork.model;

import java.util.ArrayList;

/**
 * Created by lx on 2017/8/12.
 */

public class MeetingListMoudle {

    private String code;
    private String msg;
    private ArrayList<Meet> data;

    public class Meet{
      private String id;        //会议id",
      private String text;      //会议内容",
      private String address;   //会议地址",
      private String user_id;   //发送者用户ID",
      private String user_name; //发送者用户姓名",
      private String face_img;  //发送者用户头像",
      private String status;    //通知的阅读状态，0：未读；1：已读",
      private String time;      //会议开始时间 格式2017-01-03 21:00:00",
      private String apply_type;//0:会议 1:请假 2:加班 3:出差 4:报销",
      private String type;      //0:我发起的 1:待我审批的 2:我已经审批的 3:抄送我的",
      private String detail_url;;//当apply_type为0时显示会议详情页面URL地址,否则显示关联id"


        public String getId() {
            return id;
        }

        public String getText() {
            return text;
        }

        public String getAddress() {
            return address;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getFace_img() {
            return face_img;
        }

        public String getStatus() {
            return status;
        }

        public String getTime() {
            return time;
        }

        public String getApply_type() {
            return apply_type;
        }

        public String getType() {
            return type;
        }

        public String getDetail_url() {
            return detail_url;
        }

        @Override
        public String toString() {
            return "Meet{" +
                    "id='" + id + '\'' +
                    ", text='" + text + '\'' +
                    ", address='" + address + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", user_name='" + user_name + '\'' +
                    ", face_img='" + face_img + '\'' +
                    ", status='" + status + '\'' +
                    ", time='" + time + '\'' +
                    ", apply_type='" + apply_type + '\'' +
                    ", type='" + type + '\'' +
                    ", detail_url='" + detail_url + '\'' +
                    '}';
        }
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<Meet> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "MeetingListMoudle{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
