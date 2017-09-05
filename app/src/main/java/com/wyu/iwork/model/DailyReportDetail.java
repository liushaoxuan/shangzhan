package com.wyu.iwork.model;

/**
 * Created by lx on 2017/3/21.
 */

public class DailyReportDetail {

    private int code;
    private String msg;
    private DailyDetail data;

    public class DailyDetail{
        /**
         * [name] => 发送者用户名称
         [face_img] => 发送者用户头像
         [time] => 发送时间，格式如：2017/03/02 10:42
         [finish_work] => 今日完成工作
         [undone_work] => 未完成工作
         [coordinate_work] => 需协调工作
         [remark] => 备注
         */
        private String name,face_img,time,finish_work,undone_work,coordinate_work,remark,department;

        public String getName() {
            return name;
        }

        public String getFace_img() {
            return face_img;
        }

        public String getTime() {
            return time;
        }

        public String getFinish_work() {
            return finish_work;
        }

        public String getUndone_work() {
            return undone_work;
        }

        public String getCoordinate_work() {
            return coordinate_work;
        }

        public String getRemark() {
            return remark;
        }

        public String getDepartment() {
            return department;
        }

        @Override
        public String toString() {
            return "DailyDetail{" +
                    "name='" + name + '\'' +
                    ", face_img='" + face_img + '\'' +
                    ", time='" + time + '\'' +
                    ", finish_work='" + finish_work + '\'' +
                    ", undone_work='" + undone_work + '\'' +
                    ", coordinate_work='" + coordinate_work + '\'' +
                    ", remark='" + remark + '\'' +
                    ", department='" + department + '\'' +
                    '}';
        }
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public DailyDetail getData() {
        return data;
    }

    @Override
    public String toString() {
        return "DailyReportDetail{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
