package com.wyu.iwork.presenter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.wyu.iwork.interfaces.ContactIView;
import com.wyu.iwork.model.Contact;
import com.wyu.iwork.util.HanziToPinyin;
import com.wyu.iwork.util.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/27.
 */
public class CommuContactPresenter
        extends BasePresenter<ContactIView<List<Contact>>, List<Contact>>
{
    private static final String TAG = CommuContactPresenter.class.getSimpleName();
    private Context mCtxt;

    public CommuContactPresenter(Context ctxt) {
        this.mCtxt = ctxt;
    }

    public void loadContact() {
        Logger.e(TAG, "loadContact>>>>>>>>>>>>");
        new ReadContactsTask().execute();
    }

    private List<Contact> doReadContacts() {
        List<Contact> contacts = new ArrayList<>();
        ContentResolver cr = mCtxt.getContentResolver();
        Cursor contactCursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
                ContactsContract.PhoneLookup.DISPLAY_NAME + " asc");
        if (contactCursor == null) return null;
        while (contactCursor.moveToNext()) {
            Contact contact = new Contact();
            String name = contactCursor.getString(
                    contactCursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            contact.setName(name);
            String ContactId = contactCursor
                    .getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phoneCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null,
                    null);
            if (phoneCursor == null) continue;
            if (phoneCursor.moveToNext()) {
                String phone = phoneCursor.getString(
                        phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contact.setPhone(phone);
            }
            phoneCursor.close();
            contacts.add(contact);
        }
        contactCursor.close();
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                String s1 = HanziToPinyin.getPinyin(o1.getName());
                String s2 = HanziToPinyin.getPinyin(o2.getName());
                o1.setAlpha(String.valueOf(Character.toUpperCase(s1.charAt(0))));
                o2.setAlpha(String.valueOf(Character.toUpperCase(s2.charAt(0))));
                int n1 = s1.length();
                int n2 = s2.length();
                int min = Math.min(n1, n2);
                for (int i = 0; i < min; i++) {
                    char c1 = s1.charAt(i);
                    char c2 = s2.charAt(i);
                    if (c1 != c2) {
                        c1 = Character.toUpperCase(c1);
                        c2 = Character.toUpperCase(c2);
                        if (c1 != c2) {
                            c1 = Character.toLowerCase(c1);
                            c2 = Character.toLowerCase(c2);
                            if (c1 != c2) {
                                // No overflow because of numeric promotion
                                return c1 - c2;
                            }
                        }
                    }
                }
                return n1 - n2;
            }
        });
        Logger.e(TAG, contacts.toString());
        return contacts;
    }

    private class ReadContactsTask extends AsyncTask<Void, Void, List<Contact>> {
        @Override
        protected List<Contact> doInBackground(Void... params) {
            return doReadContacts();
        }

        @Override
        protected void onPostExecute(List<Contact> contacts) {
            if (view != null) view.onLoadFinished(contacts);
            cachedData = contacts;
        }
    }
}
