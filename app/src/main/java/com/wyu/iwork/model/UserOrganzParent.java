package com.wyu.iwork.model;

import com.github.huajianjiang.expandablerecyclerview.widget.Parent;

import java.util.List;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/22.
 */
public class UserOrganzParent extends OrganzManageBase
        implements Parent<UserOrganzChild>, Base
{
    private static final String TAG = UserOrganzParent.class.getSimpleName();
    private List<UserOrganzChild> list;

    public void setList(List<UserOrganzChild> list) {
        this.list = list;
    }

    @Override
    public List<UserOrganzChild> getChildren() {
        return list;
    }

    @Override
    public boolean isInitiallyExpandable() {
        return false;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return true;
    }
}
