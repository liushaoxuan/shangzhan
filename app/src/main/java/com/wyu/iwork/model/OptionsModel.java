package com.wyu.iwork.model;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.io.Serializable;

/**
 * Created by lx on 2017/5/5.
 * 条件选择
 */

public class OptionsModel implements IPickerViewData,Serializable {

    private String text;

    public OptionsModel() {
    }

    public OptionsModel(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getPickerViewText() {
        return text;
    }
}
