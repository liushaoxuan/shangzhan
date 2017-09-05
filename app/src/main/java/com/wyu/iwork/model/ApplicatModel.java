package com.wyu.iwork.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lx on 2017/1/18.
 * 应用bean
 */

public class ApplicatModel implements Serializable{
    /**
     * 常用应用
     */
    private List<ApplicatCommonModel> common;

    /**
     * 精选应用
     */
    private List<ApplicationItemModel> exquisite;

    /**
     * 应用导航
     */
    private List<ApplicationItemModel> navigation;

    public ApplicatModel() {
    }

    public ApplicatModel(List<ApplicatCommonModel> common, List<ApplicationItemModel> navigation, List<ApplicationItemModel> exquisite) {
        this.common = common;
        this.navigation = navigation;
        this.exquisite = exquisite;
    }

    public List<ApplicatCommonModel> getCommon() {
        return common;
    }

    public void setCommon(List<ApplicatCommonModel> common) {
        this.common = common;
    }

    public List<ApplicationItemModel> getNavigation() {
        return navigation;
    }

    public void setNavigation(List<ApplicationItemModel> navigation) {
        this.navigation = navigation;
    }

    public List<ApplicationItemModel> getExquisite() {
        return exquisite;
    }

    public void setExquisite(List<ApplicationItemModel> exquisite) {
        this.exquisite = exquisite;
    }

    @Override
    public String toString() {
        return "ApplicatModel{" +
                "common=" + common +
                ", exquisite=" + exquisite +
                ", navigation=" + navigation +
                '}';
    }
}
