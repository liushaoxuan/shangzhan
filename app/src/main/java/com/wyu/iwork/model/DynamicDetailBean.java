package com.wyu.iwork.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lx on 2016/12/23.
 * 动态详情——jaavabean
 */

public class DynamicDetailBean implements Serializable {

    /**
     * 动态ID
     */
    private String dynamic_id;
    /**
     * 用户id
     */
   private String user_id;
    /**
     * 用户头像
     */
   private String user_face_img;
    /**
     * 用户昵称
     */
   private String nick_name;
    /**
     * 用户手机号
     */
   private String user_phone;
    /**
     * 动态内容
     */
   private String text;
    /**
     * 赞数
     */
   private String count_praise;
    /**
     * 评论数
     */
   private String count_comment;
    /**
     * 添加时间：格式 2016-12-12 12:12
     */
   private String add_time;
    /**
     * 是否点赞过 0未点赞 1点赞
     */
    private String is_praise;

    /**
     * 点赞该动态的用户集合
     */
   private List<DynamicPraiseBean> praise_list;
    /**
     * 评论该动态的用户集合
     */
   private List<DynamicCommentBean> comment_list;

    public DynamicDetailBean() {
    }

    public DynamicDetailBean(String dynamic_id, String user_id, String user_face_img, String nick_name, String user_phone, String text, String count_praise, String count_comment, String add_time, String is_praise, List<DynamicPraiseBean> praise_list, List<DynamicCommentBean> comment_list) {
        this.dynamic_id = dynamic_id;
        this.user_id = user_id;
        this.user_face_img = user_face_img;
        this.nick_name = nick_name;
        this.user_phone = user_phone;
        this.text = text;
        this.count_praise = count_praise;
        this.count_comment = count_comment;
        this.add_time = add_time;
        this.is_praise = is_praise;
        this.praise_list = praise_list;
        this.comment_list = comment_list;
    }

    public String getDynamic_id() {
        return dynamic_id;
    }

    public void setDynamic_id(String dynamic_id) {
        this.dynamic_id = dynamic_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_face_img() {
        return user_face_img;
    }

    public void setUser_face_img(String user_face_img) {
        this.user_face_img = user_face_img;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCount_praise() {
        return count_praise;
    }

    public void setCount_praise(String count_praise) {
        this.count_praise = count_praise;
    }

    public String getCount_comment() {
        return count_comment;
    }

    public void setCount_comment(String count_comment) {
        this.count_comment = count_comment;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getIs_praise() {
        return is_praise;
    }

    public void setIs_praise(String is_praise) {
        this.is_praise = is_praise;
    }

    public List<DynamicPraiseBean> getPraise_list() {
        return praise_list;
    }

    public void setPraise_list(List<DynamicPraiseBean> praise_list) {
        this.praise_list = praise_list;
    }

    public List<DynamicCommentBean> getComment_list() {
        return comment_list;
    }

    public void setComment_list(List<DynamicCommentBean> comment_list) {
        this.comment_list = comment_list;
    }
}
