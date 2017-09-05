package com.wyu.iwork.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lx on 2017/3/29.
 * 收藏名片
 */

public class CardModel implements Serializable {
    /**
     * Array
     * (
     * [code] => 请求结果：数字 0 代表请求成功，非 0 代表请求失败
     * [msg] => 请求结果状态说明，可以提示给用户
     * [data] => Array
     * (
     * [0] => Array
     * (
     * [name] => 名片信息：名字
     * [job] => 名片信息：职位
     * [company] => 名片信息：公司
     * [card_img] => 名片图片地址
     * )
     * <p>
     * )
     * <p>
     * )
     * <p>
     * ""address": "森林大道119号",
     * "card_img": "",
     * "company": "魔法学院",
     * "department": "研究部",
     * "email": "helloprot@magic.com",
     * "id": "1",
     * "job": "magic man",
     * "name": "哈利波特",
     * "phone": "18888",
     * "user_id": "23",
     * "wechat_id": "hellpport"
     */
    private String code;
    private String msg;
    private Data data;

    public class Data{
        private String company;
        private ArrayList<Card> list;
        private Share share;
        private String user_name;
        private String face_img;

        public class Card{
            /**
             * [id] => 名片ID
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
            private String id,name,phone,use_id,email,wechat_id,job,company,department,address,card_img;

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getPhone() {
                return phone;
            }

            public String getUse_id() {
                return use_id;
            }

            public String getCard_img() {
                return card_img;
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

            @Override
            public String toString() {
                return "Card{" +
                        "id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        ", phone='" + phone + '\'' +
                        ", use_id='" + use_id + '\'' +
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

        public class Share{
            private String icon,intro,title,url;

            public String getIcon() {
                return icon;
            }

            public String getIntro() {
                return intro;
            }

            public String getTitle() {
                return title;
            }

            public String getUrl() {
                return url;
            }

            @Override
            public String toString() {
                return "Share{" +
                        "icon='" + icon + '\'' +
                        ", intro='" + intro + '\'' +
                        ", title='" + title + '\'' +
                        ", url='" + url + '\'' +
                        '}';
            }
        }

        public String getCompany() {
            return company;
        }

        public String getFace_img() {
            return face_img;
        }

        public ArrayList<Card> getList() {
            return list;
        }

        public Share getShare() {
            return share;
        }

        public String getUser_name() {
            return user_name;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "company='" + company + '\'' +
                    ", list=" + list +
                    ", share=" + share +
                    ", user_name='" + user_name + '\'' +
                    '}';
        }
    }

    public String getCode() {
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
        return "CardModel{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
