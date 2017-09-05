package com.wyu.iwork.model;

import com.wyu.iwork.widget.swipedelete.SlideView;

import java.util.List;

/**
 * Created by lx on 2017/3/14.
 */

public class NoticeModel {

    private int code;
    private String msg;
    private List<Data> data;

    public class Data{
        private String add_time,content,face_img,title,url,notice_id;
        public SlideView slideView;

        public String getAdd_time() {
            return add_time;
        }

        public String getContent() {
            return content;
        }

        public String getFace_img() {
            return face_img;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }

        public String getNotice_id() {
            return notice_id;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setFace_img(String face_img) {
            this.face_img = face_img;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setNotice_id(String notice_id) {
            this.notice_id = notice_id;
        }

        public void setSlideView(SlideView slideView) {
            this.slideView = slideView;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "add_time='" + add_time + '\'' +
                    ", content='" + content + '\'' +
                    ", face_img='" + face_img + '\'' +
                    ", title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    ", notice_id='" + notice_id + '\'' +
                    '}';
        }
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<Data> getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "NoticeUnReadModel{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
