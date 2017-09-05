package com.wyu.iwork.model;

import com.wyu.iwork.widget.swipedelete.SlideView;

/**
 * Created by lx on 2017/3/24.
 */

public class Notice {
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

    public SlideView getSlideView() {
        return slideView;
    }

    @Override
    public String toString() {
        return "Notice{" +
                "add_time='" + add_time + '\'' +
                ", content='" + content + '\'' +
                ", face_img='" + face_img + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", notice_id='" + notice_id + '\'' +
                ", slideView=" + slideView +
                '}';
    }
}
