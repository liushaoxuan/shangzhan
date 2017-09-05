package com.wyu.iwork.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.wyu.iwork.model.ContactsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxliu on 2017/4/11.
 * 读取手机数据库中的通讯录
 */

public class GetPhoneNumberFromMobile {
    private static GetPhoneNumberFromMobile mobile;
    private List<ContactsModel> contactslist;
    /**
     * 上一次的拼音
     */
    private String lastPy = "";

    private GetPhoneNumberFromMobile() {
    }

    public static GetPhoneNumberFromMobile instance() {
        if (mobile == null) {
            mobile = new GetPhoneNumberFromMobile();
        }
        return mobile;
    }

    public List<ContactsModel> getContactFromMobile(Context context) throws Exception{
        if (contactslist == null) {
            contactslist = new ArrayList<>();
        } else {
            contactslist.clear();
        }
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);

        while (cursor.moveToNext()) {
            //读取通讯录的姓名
            String name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //姓名首字母
            String Py = (FirstLetterUtil.getFirstLetter(name)).toUpperCase().toString();
            if (lastPy.equals(Py)){
                Py = "";
            }else {
                lastPy = Py;
            }
            //读取通讯录的号码
            String number = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            ContactsModel model = new ContactsModel(name, number, Py);
            contactslist.add(model);
        }
        return contactslist;
    }
}
