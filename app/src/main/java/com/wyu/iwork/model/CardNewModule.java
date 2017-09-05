package com.wyu.iwork.model;

import java.util.ArrayList;

/**
 * Created by lx on 2017/4/19.
 */

public class CardNewModule {
    /**
     *  [id] => 名片ID
     [user_id] => 用户自己的ID
     [name] => 名片信息：名字
     [phone] => 名片信息：手机
     [email] => 名片信息：邮箱
     [wechat_id] => 名片信息：微信
     [job] => 名片信息：职位
     [company] => 名片信息：公司
     [department] => 名片信息：部门
     [address] => 名片信息：地址
     [card_img] => 名片的图片绝对地址
     */
    private String code;
    private String msg;
    private ArrayList<Card> data;

    public class Card{

        private String id,user_id,name,phone,email,wechat_id,job,company,department,address,card_img;

        public void setId(String id) {
            this.id = id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setWechat_id(String wechat_id) {
            this.wechat_id = wechat_id;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setCard_img(String card_img) {
            this.card_img = card_img;
        }

        public String getId() {
            return id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public String getEmail() {
            return email;
        }

        public String getWechat_id() {
            return wechat_id;
        }

        public String getJob() {
            return job;
        }

        public String getCompany() {
            return company;
        }

        public String getDepartment() {
            return department;
        }

        public String getAddress() {
            return address;
        }

        public String getCard_img() {
            return card_img;
        }

        @Override
        public String toString() {
            return "Card{" +
                    "id='" + id + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", name='" + name + '\'' +
                    ", phone='" + phone + '\'' +
                    ", email='" + email + '\'' +
                    ", wechat_id='" + wechat_id + '\'' +
                    ", job='" + job + '\'' +
                    ", company='" + company + '\'' +
                    ", department='" + department + '\'' +
                    ", address='" + address + '\'' +
                    ", card_img='" + card_img + '\'' +
                    '}';
        }
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(ArrayList<Card> data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public ArrayList<Card> getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "CardNewModule{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
