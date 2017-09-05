package com.wyu.iwork.model;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/22.
 */
public class OrganzManageBase implements Base {
    private static final String TAG = OrganzManageBase.class.getSimpleName();
    private String department_id;
    private String department;

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
}
