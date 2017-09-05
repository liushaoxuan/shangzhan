package com.wyu.iwork.model;

import org.litepal.crud.DataSupport;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/26.
 */
public class UserInfo extends DataSupport implements Base  {
    /**
     * 融云 IM token
     */
    private String rongcloud_token = "";

    /**
     * 公司ID
     */
    private String company_id = "";
    /**
     * 公司名称
     */
    private String company = "";
    /**
     * 用户ID
     */
    private String user_id = "";
    /**
     * 用户名
     */
    private String user_name = "";
    /**
     * 移动办公
     */
    private String nick_name = "";
    /**
     * 用户头像URL地址
     */
    private String user_face_img = "";
    /**
     * 动态模块顶部图片地址
     */
    private String user_top_img = "";
    /**
     * 是否是管理员用户  0：否，1：是
     */
    private String is_admin = "";
    /**
     * 用户手机号
     */
    private String user_phone = "";
    /**
     * 用户真实姓名
     */
    private String real_name = "";
    /**
     * 性别：男/女
     */
    private String sex = "";
    /**
     * 生日，格式如：1990-01-01
     */
    private String birthday = "";
    /**
     * 所属部门
     */
    private String department = "";
    /**
     * 部门ID
     */
    private String department_id = "";
    /**
     * 用户角色
     */
    private String job = "";
    /**
     * 角色ID
     */
    private String role_id = "";
    /**
     * 邮箱
     */
    private String email = "";
    /**
     * 办公电话
     */
    private String work_phone = "";
    /**
     * 关于我们HTML页面地址
     */
    private String about_us = "";
    /**
     * 发送通讯录好友使用内容
     */
    private String welcome_sms_msg = "";
    /**
     * 用户微信号
     */
    private String wechat = "";
    /**
     * 用户住址
     */
    private String address;
    /**
     * 分享
     */
    private  ShareModel  share;
    /**
     * 企业认证状态  0:认证失败 1：认证成功 2：认证中 3：未认证
     * 企业认证状态 0:认证失败 1：认证成功 2：认证中 3：未认证
     */
    private  String  company_auth = "";
    private  int  is_update ;//是否需要更新 1：需要更新 0：不需要更新
    private  String  url = "";//更新连接,不需更新时为空，ANDROID下载地址

    public UserInfo() {
    }

    public UserInfo(String rongcloud_token, String company_id, String company, String user_id, String user_name, String nick_name, String user_face_img, String user_top_img, String is_admin, String user_phone, String real_name, String sex, String birthday, String department, String department_id, String job, String role_id, String email, String work_phone, String about_us, String welcome_sms_msg, String wechat, String address, ShareModel share, String company_auth) {
        this.rongcloud_token = rongcloud_token;
        this.company_id = company_id;
        this.company = company;
        this.user_id = user_id;
        this.user_name = user_name;
        this.nick_name = nick_name;
        this.user_face_img = user_face_img;
        this.user_top_img = user_top_img;
        this.is_admin = is_admin;
        this.user_phone = user_phone;
        this.real_name = real_name;
        this.sex = sex;
        this.birthday = birthday;
        this.department = department;
        this.department_id = department_id;
        this.job = job;
        this.role_id = role_id;
        this.email = email;
        this.work_phone = work_phone;
        this.about_us = about_us;
        this.welcome_sms_msg = welcome_sms_msg;
        this.wechat = wechat;
        this.address = address;
        this.share = share;
        this.company_auth = company_auth;
    }

    public String getRongcloud_token() {
        return rongcloud_token;
    }

    public void setRongcloud_token(String rongcloud_token) {
        this.rongcloud_token = rongcloud_token;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getUser_face_img() {
        return user_face_img;
    }

    public void setUser_face_img(String user_face_img) {
        this.user_face_img = user_face_img;
    }

    public String getUser_top_img() {
        return user_top_img;
    }

    public void setUser_top_img(String user_top_img) {
        this.user_top_img = user_top_img;
    }

    public String getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(String is_admin) {
        this.is_admin = is_admin;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWork_phone() {
        return work_phone;
    }

    public void setWork_phone(String work_phone) {
        this.work_phone = work_phone;
    }

    public String getAbout_us() {
        return about_us;
    }

    public void setAbout_us(String about_us) {
        this.about_us = about_us;
    }

    public String getWelcome_sms_msg() {
        return welcome_sms_msg;
    }

    public void setWelcome_sms_msg(String welcome_sms_msg) {
        this.welcome_sms_msg = welcome_sms_msg;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ShareModel getShare() {
        return share;
    }

    public void setShare(ShareModel share) {
        this.share = share;
    }

    public String getCompany_auth() {
        return company_auth;
    }

    public void setCompany_auth(String company_auth) {
        this.company_auth = company_auth;
    }

    public int getIs_update() {
        return is_update;
    }

    public void setIs_update(int is_update) {
        this.is_update = is_update;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "rongcloud_token='" + rongcloud_token + '\'' +
                ", company_id='" + company_id + '\'' +
                ", company='" + company + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", user_face_img='" + user_face_img + '\'' +
                ", user_top_img='" + user_top_img + '\'' +
                ", is_admin='" + is_admin + '\'' +
                ", user_phone='" + user_phone + '\'' +
                ", real_name='" + real_name + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", department='" + department + '\'' +
                ", department_id='" + department_id + '\'' +
                ", job='" + job + '\'' +
                ", role_id='" + role_id + '\'' +
                ", email='" + email + '\'' +
                ", work_phone='" + work_phone + '\'' +
                ", about_us='" + about_us + '\'' +
                ", welcome_sms_msg='" + welcome_sms_msg + '\'' +
                ", wechat='" + wechat + '\'' +
                ", address='" + address + '\'' +
                ", share=" + share +
                '}';
    }
}
