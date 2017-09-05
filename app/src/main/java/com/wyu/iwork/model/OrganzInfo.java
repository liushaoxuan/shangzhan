package com.wyu.iwork.model;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/29.
 */
public class OrganzInfo implements Base {
    private String department_superior_id = "0";
    private String department_superior = "";
    private String department_id = "0";
    private String department = "";
    private String tel = "";
    private String fax = "";
    private String address = "";

    public String getDepartment_superior_id() {
        return department_superior_id;
    }

    public void setDepartment_superior_id(String department_superior_id) {
        this.department_superior_id = department_superior_id;
    }

    public String getDepartment_superior() {
        return department_superior;
    }

    public void setDepartment_superior(String department_superior) {
        this.department_superior = department_superior;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
