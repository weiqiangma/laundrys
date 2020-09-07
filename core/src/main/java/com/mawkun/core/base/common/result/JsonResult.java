package com.mawkun.core.base.common.result;

import com.mawkun.core.utils.JsonUtils;

import java.io.Serializable;
import java.util.Date;

public class JsonResult extends RetMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    private Object data;
    private Date timestamp = new Date();

    public JsonResult() {
    }

    public JsonResult(RetMsg rm) {
        this.ret = rm.getRet();
        this.msg = rm.getMsg();
    }

    public JsonResult(int ret, String msg) {
        this.ret = ret;
        this.msg = msg;
    }

    public JsonResult(boolean success) {
        this.ret = success ? 0 : 1;
        this.msg = success ? "成功" : "失败";
    }

    public JsonResult(boolean success, String msg) {
        this.ret = success ? 0 : 1;
        this.msg = msg;
    }

    public JsonResult(boolean success, String msg, Object data) {
        this.ret = success ? 0 : 1;
        this.msg = msg;
        this.data = data;
    }

    public JsonResult success() {
        this.ret = 0;
        this.msg = "成功";
        return this;
    }

    public JsonResult success(String msg) {
        this.ret = 0;
        this.msg = msg;
        return this;
    }

    public JsonResult error(String msg) {
        this.ret = 1;
        this.msg = msg;
        return this;
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

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public String toString() {
        return JsonUtils.toStringNoEx(this);
    }
}
