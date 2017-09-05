package com.wyu.iwork.model;

import java.util.ArrayList;

/**
 * Created by lx on 2017/4/11.
 */

public class Task {

    private String code;
    private String msg;
    private ArrayList<TaskModel> data;

    public class TaskModel{
        private String task_id;       //任务ID
        private String task;          //任务名称
        private String header;        //负责人ID
        private String id;            //第一位负责人ID
        private String user_name;     //第一位负责人用户名
        private String real_name;     //第一位负责人真实姓名
        private String face_img;      //用户头像
        private String begin_time;    //开始时间
        private String end_time;      //结束时间
        private String intro;
        private String status;
        private String user_id;

        public String getIntro() {
            return intro;
        }

        public String getTask_id() {
            return task_id;
        }

        public String getTask() {
            return task;
        }

        public String getHeader() {
            return header;
        }

        public String getStatus() {
            return status;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getId() {
            return id;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getReal_name() {
            return real_name;
        }

        public String getFace_img() {
            return face_img;
        }

        public String getBegin_time() {
            return begin_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        @Override
        public String toString() {
            return "TaskModel{" +
                    "task_id='" + task_id + '\'' +
                    ", task='" + task + '\'' +
                    ", header='" + header + '\'' +
                    ", id='" + id + '\'' +
                    ", user_name='" + user_name + '\'' +
                    ", real_name='" + real_name + '\'' +
                    ", face_img='" + face_img + '\'' +
                    ", begin_time='" + begin_time + '\'' +
                    ", end_time='" + end_time + '\'' +
                    '}';
        }
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<TaskModel> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Task{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
