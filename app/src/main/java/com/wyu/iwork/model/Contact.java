package com.wyu.iwork.model;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/27.
 */
public class Contact {
    private static final String TAG = Contact.class.getSimpleName();
    private String name;
    private String phone;
    private String alpha;

    public String getAlpha() {
        return alpha;
    }

    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Contact{" +
               "name='" + name + '\'' +
               ", phone='" + phone + '\'' +
               ", alpha='" + alpha + '\'' +
               '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
