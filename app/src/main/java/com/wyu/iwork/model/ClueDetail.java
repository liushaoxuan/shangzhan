package com.wyu.iwork.model;

/**
 * Created by lx on 2017/6/15.
 */

public class ClueDetail {

    private String code;
    private String msg;
    private Clue data;

    public class Clue{

        private String id; //  线索ID
        private String name; //  姓名
        private String company; //  公司
        private String job; //  职务
        private String phone; //  联系电话
        private String province; //  省
        private String city; //  市
        private String district; //  区
        private String address; //  详细地址
        private String email; //  邮箱
        private String source_type; //  线索来源 1：其他 2：搜索引擎 3：客户介绍 4：会议 5：广告 6：电话 7：网站
        private String activity_id; //  活动ID
        private String activity_name; //  活动名称
        private String detail; //  线索详情
        private String follow_user; //  跟进人
        private String remark; //  备注
        private String add_time; //  添加时间 格式：2017-01-12 12:12:12

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCompany() {
            return company;
        }

        public String getJob() {
            return job;
        }

        public String getPhone() {
            return phone;
        }

        public String getProvince() {
            return province;
        }

        public String getCity() {
            return city;
        }

        public String getDistrict() {
            return district;
        }

        public String getAddress() {
            return address;
        }

        public String getEmail() {
            return email;
        }

        public String getSource_type() {
            return source_type;
        }

        public String getActivity_id() {
            return activity_id;
        }

        public String getActivity_name() {
            return activity_name;
        }

        public String getDetail() {
            return detail;
        }

        public String getFollow_user() {
            return follow_user;
        }

        public String getRemark() {
            return remark;
        }

        public String getAdd_time() {
            return add_time;
        }

        @Override
        public String toString() {
            return "Clue{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", company='" + company + '\'' +
                    ", job='" + job + '\'' +
                    ", phone='" + phone + '\'' +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ", district='" + district + '\'' +
                    ", address='" + address + '\'' +
                    ", email='" + email + '\'' +
                    ", source_type='" + source_type + '\'' +
                    ", activity_id='" + activity_id + '\'' +
                    ", activity_name='" + activity_name + '\'' +
                    ", detail='" + detail + '\'' +
                    ", follow_user='" + follow_user + '\'' +
                    ", remark='" + remark + '\'' +
                    ", add_time='" + add_time + '\'' +
                    '}';
        }
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Clue getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ClueDetail{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
