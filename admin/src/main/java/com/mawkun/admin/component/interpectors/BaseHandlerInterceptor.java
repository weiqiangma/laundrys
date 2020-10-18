package com.mawkun.admin.component.interpectors;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseHandlerInterceptor implements HandlerInterceptor {
    protected String encoding = "UTF=8";
    protected Log logger = LogFactory.getLog(this.getClass());

    public BaseHandlerInterceptor() {
    }

    protected void init(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
    }

    protected void sendJsonError(HttpServletRequest request, HttpServletResponse response, int code, String message) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Accept,Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Vary", "Origin,Accept-Encoding");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("expires", "0");
        response.setContentType("text/json; charset=UTF-8");
        response.getWriter().write("{\"message\":\"" + message + "\",\"code\":" + code + "}");
    }

    protected void sendPageError(HttpServletRequest request, HttpServletResponse response, int ret, String msg) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Accept,Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Vary", "Origin,Accept-Encoding");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("expires", "0");
        response.setContentType("text/json; charset=UTF-8");
        response.setContentType("text/json; charset=UTF-8");
    }
}
