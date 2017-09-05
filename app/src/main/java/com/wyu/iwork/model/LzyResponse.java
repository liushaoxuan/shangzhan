package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by lx on 2017/5/8.
 */

public class LzyResponse<T>  implements Serializable {
    private static final long serialVersionUID = 5213230387175987834L;

    public int code;
    public String msg;
    public T data;
}
