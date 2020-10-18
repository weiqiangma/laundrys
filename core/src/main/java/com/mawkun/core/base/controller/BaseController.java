package com.mawkun.core.base.controller;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import cn.pertech.common.auth.AbsAdminSession;
import cn.pertech.common.exception.SysException;
import cn.pertech.common.utils.JsonUtils;
import cn.pertech.common.utils.StringUtils;
import cn.pertech.common.vo.RetResult;
import com.alibaba.fastjson.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mawkun.core.base.data.JsonResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public abstract class BaseController {
    protected final String PAGE_ERROR_404 = "/common/404.jsp";
    protected final String PAGE_ERROR_ERROR = "/common/error.jsp";
    protected Log logger = LogFactory.getLog(this.getClass());

    public BaseController() {
    }

    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    public HttpServletResponse getResponse() {
        return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getResponse();
    }

    protected AbsAdminSession getAdminSession() {
        return (AbsAdminSession)this.getRequest().getAttribute("ADMIN_PERTECH");
    }

    protected int getAdminId() {
        AbsAdminSession as = this.getAdminSession();
        return as == null ? 0 : as.getAdminId();
    }

    public void addAttribute(String key, Object val) {
        this.getRequest().setAttribute(key, val);
    }

    public void addAdminLog(String value) {
        this.getRequest().setAttribute("_ADMIN_LOG_KEY", value);
    }

    public void addAdminLog(Integer userId, String value) {
        this.getRequest().setAttribute("_ADMIN_USERID_KEY", userId);
        this.getRequest().setAttribute("_ADMIN_LOG_KEY", value);
    }

    protected boolean fromPertechClient(HttpServletRequest request) {
        return this.isThisAgent(request, "PertechClient");
    }

    protected boolean fromWeiXin(HttpServletRequest request) {
        return this.isThisAgent(request, "MicroMessenger");
    }

    protected boolean isThisAgent(HttpServletRequest request, String agentName) {
        String header = request.getHeader("User-Agent");
        return header != null && header.contains(agentName);
    }

    protected String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    protected JsonResult sendArgsError() {
        return new JsonResult(false, "提交参数错误");
    }

    protected JsonResult sendArgsError(String msg) {
        return new JsonResult(false, msg);
    }

    protected JsonResult sendException(RetResult<?, SysException> retResult) {
        SysException ex = retResult.getException();
        return new JsonResult(ex.getErrorCode(), ex.getErrorInfo());
    }

    protected JsonResult sendException(SysException exception) {
        return new JsonResult(exception.getErrorCode(), exception.getErrorInfo());
    }

    protected JsonResult sendResult(boolean result) {
        return new JsonResult(result, result ? "操作成功" : "操作失败");
    }

    protected JsonResult sendSuccess() {
        return new JsonResult(true, "操作成功");
    }

    protected JsonResult sendSuccess(String msg) {
        return new JsonResult(true, msg);
    }

    protected JsonResult sendSuccess(String msg, Object obj) {
        JsonResult result = new JsonResult(true, msg);
        result.setData(obj);
        return result;
    }

    protected JsonResult sendSuccess(Object obj) {
        JsonResult result = new JsonResult(true, "OK");
        result.setData(obj);
        return result;
    }

    protected JsonResult sendError(String msg) {
        return new JsonResult(false, msg);
    }

    protected JsonResult sendError(Integer status, String msg) {
        JsonResult result = new JsonResult(false, msg);
        result.setRet(status);
        return result;
    }

    protected JsonResult sendError() {
        return new JsonResult(false, "诶呀！系统跑偏了！！！");
    }

    protected String sendErrorPage(String errorInfo) {
        this.getRequest().setAttribute("errorInfo", errorInfo);
        return "/common/error.jsp";
    }

    protected String sendErrorPage(String errorTitle, String errorInfo) {
        this.getRequest().setAttribute("errorTitle", errorTitle);
        this.getRequest().setAttribute("errorInfo", errorInfo);
        return "/common/error.jsp";
    }

    protected String parseJsonHtml(JSONObject json) {
        return JsonUtils.toStringNoEx(json);
    }

    protected boolean isEmpty(Integer val) {
        return val == null || val <= 0;
    }

    protected boolean notEmpty(Integer val) {
        return val != null && val > 0;
    }

    protected boolean isEmpty(String str) {
        return StringUtils.isBlank(str);
    }

    protected boolean notEmpty(String str) {
        return StringUtils.isNotBlank(str);
    }

    protected int getPageNo() {
        int firstRow = this.getIntPar("firstRow", -1);
        int fetchSize = this.getIntPar("fetchSize", -1);
        if (firstRow > -1 && fetchSize > 0) {
            return firstRow / fetchSize + 1;
        } else {
            int pageNum = this.getIntPar("pageNum", -1);
            return pageNum > 0 ? pageNum : this.getIntPar("pageNo", 1);
        }
    }

    protected int getPageSize() {
        int fetchSize = this.getIntPar("fetchSize", -1);
        return fetchSize > 0 ? fetchSize : this.getIntPar("pageSize", 20);
    }

    protected int getIntPar(String parName) {
        return this.getIntPar(parName, 0);
    }

    protected int getIntPar(String parName, int defaultNum) {
        String parVal = this.getRequest().getParameter(parName);
        boolean var4 = false;

        int par;
        try {
            if (StringUtils.isNotEmpty(parVal)) {
                parVal = StringUtils.trim(parVal);
                par = Integer.parseInt(parVal);
            } else {
                par = defaultNum;
            }
        } catch (Exception var6) {
            par = defaultNum;
        }

        return par;
    }

    public String getStrPar(String param) {
        return this.getStrPar(param, "");
    }

    public boolean parIllegal(String val) {
        return val == null || val.trim().length() == 0;
    }

    public boolean parIllegal(Integer val) {
        return val == null || val < 1;
    }

    public boolean parIsNull(String key) {
        return this.getRequest().getParameter(key) == null;
    }

    public boolean hasParameter(String key) {
        return this.getRequest().getParameterMap().containsKey(key);
    }

    public boolean noParameter(String key) {
        return !this.getRequest().getParameterMap().containsKey(key);
    }

    public String getStrPar(String param, String defaultValue) {
        String value = this.getRequest().getParameter(param);
        value = StringUtils.isEmpty(value) ? defaultValue : value.trim();
        return value;
    }

    protected void sendHtml(String html) {
        HttpServletResponse response = this.getResponse();

        try {
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("expires", "0");
            response.setContentType("text/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(html);
            response.getWriter().close();
        } catch (Exception var4) {
            this.logger.error(var4);
        }

    }

    protected String redirect(String url) {
        return "redirect:" + url;
    }

    protected String forward(String url) {
        return "forward:" + url;
    }

    protected String urlEncoder(String data) {
        try {
            return URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            return "encode error";
        }
    }

    protected String urlDecoder(String data) {
        try {
            return URLDecoder.decode(data, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            return "encode error";
        }
    }
}
