package com.mawkun.core.base.data;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import cn.pertech.common.utils.JsonUtils;
import java.io.Serializable;
import java.util.Date;

public class JsonResult extends RetMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    private Object data;
    private Date timestamp = new Date();

    public JsonResult() {
    }

    public JsonResult(RetMsg rm) {
        this.code = rm.getCode();
        this.message = rm.getMessage();
    }

    public JsonResult(int ret, String msg) {
        this.code = ret;
        this.message = msg;
    }

    public JsonResult(boolean success) {
        this.code = success ? 200 : 500;
        this.message = success ? "成功" : "失败";
    }

    public JsonResult(boolean success, String msg) {
        this.code = success ? 200 : 500;
        this.message = msg;
    }

    public JsonResult(boolean success, String msg, Object data) {
        this.code = success ? 200 : 500;
        this.message = msg;
        this.data = data;
    }

    public JsonResult success() {
        this.code = 200;
        this.message = "成功";
        return this;
    }

    public JsonResult success(String msg) {
        this.code = 200;
        this.message = msg;
        return this;
    }

    public JsonResult error(String msg) {
        this.code = 500;
        this.message = msg;
        return this;
    }

    public int getRet() {
        return this.code;
    }

    public void setRet(int ret) {
        this.code = ret;
    }

    public String getMsg() {
        return this.message;
    }

    public void setMsg(String msg) {
        this.message = msg;
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
