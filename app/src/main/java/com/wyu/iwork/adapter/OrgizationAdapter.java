package com.wyu.iwork.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.Organization;

import java.util.ArrayList;

/**
 * Created by lx on 2017/3/15.
 */

public class OrgizationAdapter extends BaseExpandableListAdapter {


    private Context context;
    private ArrayList<Organization.FirstOrg> list;
    private GroupViewHolder groupViewHolder;
    public OrgizationAdapter(Context context, ArrayList<Organization.FirstOrg> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public Organization.FirstOrg.SecondOrg getChild(int groupPosition, int childPosition) {
        if(list.get(groupPosition).getJunior()!=null){
            return list.get(groupPosition).getJunior().get(childPosition);
        }
        return null;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(list.get(groupPosition).getJunior()!=null){
            return list.get(groupPosition).getJunior().size();
        }
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;
        if(convertView == null){
            childViewHolder = new ChildViewHolder();
            convertView = View.inflate(context, R.layout.item_orgnz_parent_inner,null);
            childViewHolder.childName = (TextView) convertView.findViewById(R.id.name);
            childViewHolder.childIndictor = (ImageView) convertView.findViewById(R.id.indicator);
            childViewHolder.ll_child = (LinearLayout) convertView.findViewById(R.id.ll_child);
            convertView.setTag(childViewHolder);
        }else{
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.childName.setText(getChild(groupPosition,childPosition).getDepartment_name());

        return convertView;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getGroupCount() {
        //判断数据传回是否为空
        if(list != null){
            return list.size();
        }else{
            return 0;
        }
    }

    @Override
    public Organization.FirstOrg getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if(convertView == null){
            groupViewHolder = new GroupViewHolder();
            convertView = View.inflate(context,R.layout.item_orgnz_parent_new,null);
            groupViewHolder.parentName = (TextView) convertView.findViewById(R.id.name);
            groupViewHolder.parentIndictor = (ImageView) convertView.findViewById(R.id.indicator);
            groupViewHolder.ll_parent = (LinearLayout) convertView.findViewById(R.id.ll_parent);
            convertView.setTag(groupViewHolder);
        }else{
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.parentName.setText(getGroup(groupPosition).getDepartment_name());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder{
        TextView parentName;
        ImageView parentIndictor;

        LinearLayout ll_parent;
    }

    class ChildViewHolder{
        TextView childName;
        ImageView childIndictor;
        LinearLayout ll_child;
    }

}
