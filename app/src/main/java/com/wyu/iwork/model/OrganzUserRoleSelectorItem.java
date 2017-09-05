package com.wyu.iwork.model;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/28.
 */
public class OrganzUserRoleSelectorItem implements Base {
    private static final String TAG = OrganzUserRoleSelectorItem.class.getSimpleName();
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
