package com.wyu.iwork.model;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lx on 2017/4/18.
 */

public class DepartmentModel implements Serializable ,IPickerViewData{


    /**
     * department_id : 38
     * department : 技术部
     * list : [{"department_id":"57","department":"kkk"}]
     */

    private String department_id;
    private String department;
    private boolean expand = false;//是否展开
    private boolean isedit = false;//山否处于编辑状态
    private List<ListEntity> list;

    public DepartmentModel() {
    }

    public DepartmentModel(String department_id, String department) {
        this.department_id = department_id;
        this.department = department;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }



    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isedit() {
        return isedit;
    }

    public void setIsedit(boolean isedit) {
        this.isedit = isedit;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public String getDepartment() {
        return department;
    }

    public List<ListEntity> getList() {
        return list;
    }

    @Override
    public String getPickerViewText() {
        if (department==null){
            return "";
        }
        return department;
    }

    public  class ListEntity implements Serializable,IPickerViewData{
        /**
         * department_id : 57
         * department : kkk
         */

        private String department_id;
        private String department;
        private boolean isedit = false;//是否处于编辑状态

        public ListEntity() {
        }

        public ListEntity(String department_id, String department) {
            this.department_id = department_id;
            this.department = department;
        }

        public void setDepartment_id(String department_id) {
            this.department_id = department_id;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getDepartment_id() {
            return department_id;
        }

        public String getDepartment() {
            return department;
        }

        public boolean isedit() {
            return isedit;
        }

        public void setIsedit(boolean isedit) {
            this.isedit = isedit;
        }

        @Override
        public String getPickerViewText() {
            if (department==null){
                return "";
            }
            return department;
        }
    }
}
