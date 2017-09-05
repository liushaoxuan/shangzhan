package com.wyu.iwork.model;

import com.github.huajianjiang.expandablerecyclerview.widget.Parent;

import java.util.List;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/22.
 */
public class OrganzManageParent extends OrganzManageBase
        implements Parent<OrganzManageChild>, Base
{
    private static final String TAG = OrganzManageParent.class.getSimpleName();
    private List<OrganzManageChild> list;

    public void setList(List<OrganzManageChild> list) {
        this.list = list;
    }

    @Override
    public List<OrganzManageChild> getChildren() {
        return list;
    }

    @Override
    public boolean isInitiallyExpandable() {
        return true;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
