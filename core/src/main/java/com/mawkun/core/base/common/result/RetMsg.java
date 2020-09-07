package com.mawkun.core.base.common.result;

import java.io.Serializable;

public class RetMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int RET_SUCCESS = 0;
    public static final int RET_ERROR = 1;
    protected int ret;
    protected String msg;

    public RetMsg() {
    }

    public RetMsg(int ret, String msg) {
        this.ret = ret;
        this.msg = msg;
    }

    public int getRet() {
        return this.ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
