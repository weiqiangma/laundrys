package com.mawkun.core.base.data;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.io.Serializable;

public class RetMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int RET_SUCCESS = 0;
    public static final int RET_ERROR = 1;
    protected int code;
    protected String message;

    public RetMsg() {
    }

    public RetMsg(int ret, String msg) {
        this.code = ret;
        this.message = msg;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int ret) {
        this.code = ret;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }
}
