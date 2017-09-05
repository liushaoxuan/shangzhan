package com.wyu.iwork.model;

import java.util.ArrayList;

/**
 * Created by lx on 2017/4/8.
 */

public class CheckInStatisticsModule {

    private int code;
    private String msg;
    private Data data;

    public class Data{
        //       迟到次数,格式 2  早退次数,格式 2  请假时间，格式 4天3小时 出差时间，格式 4天3小时
        private String be_late_num,leave_early_num,leave_time,out_time,over_time;   //加班时间，格式 4天3小时
        private ArrayList<Late> late_msg;
        private ArrayList<Late> leave_early_msg;
        private ArrayList<DayState> month_sign;
        private String user_name,department,now_time;

        public class Late{
            private String diff_time,sign_time;

            public String getDiff_time() {
                return diff_time;
            }

            public String getSign_time() {
                return sign_time;
            }

            @Override
            public String toString() {
                return "Late{" +
                        "diff_time='" + diff_time + '\'' +
                        ", sign_time='" + sign_time + '\'' +
                        '}';
            }
        }

        public class DayState{
            private int status;
            private String day;


            public String getDay() {
                return day;
            }

            public int getStatus() {
                return status;
            }

            @Override
            public String toString() {
                return "DayState{" +
                        "day=" + day +
                        ", status=" + status +
                        '}';
            }
        }

        public String getUser_name() {
            return user_name;
        }

        public String getNow_time() {
            return now_time;
        }

        public String getDepartment() {
            return department;
        }

        public String getBe_late_num() {
            return be_late_num;
        }

        public String getLeave_early_num() {
            return leave_early_num;
        }

        public String getLeave_time() {
            return leave_time;
        }

        public String getOut_time() {
            return out_time;
        }

        public String getOver_time() {
            return over_time;
        }

        public ArrayList<Late> getLate_msg() {
            return late_msg;
        }

        public ArrayList<Late> getLeave_early_msg() {
            return leave_early_msg;
        }

        public ArrayList<DayState> getMonth_sign() {
            return month_sign;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "be_late_num='" + be_late_num + '\'' +
                    ", leave_early_num='" + leave_early_num + '\'' +
                    ", leave_time='" + leave_time + '\'' +
                    ", out_time='" + out_time + '\'' +
                    ", over_time='" + over_time + '\'' +
                    ", late_msg=" + late_msg +
                    ", leave_early_msg=" + leave_early_msg +
                    ", month_sign=" + month_sign +
                    ", user_name='" + user_name + '\'' +
                    ", department='" + department + '\'' +
                    ", now_time='" + now_time + '\'' +
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
        return "CheckInStatisticsModule{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
