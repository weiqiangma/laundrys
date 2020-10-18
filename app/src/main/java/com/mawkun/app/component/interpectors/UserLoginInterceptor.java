package com.mawkun.app.component.interpectors;

import cn.pertech.common.constants.Constants;
import cn.pertech.common.spring.SpringContext;
import cn.pertech.common.utils.StringUtils;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.entity.User;
import com.mawkun.core.base.service.UserCacheService;
import com.mawkun.core.service.UserServiceExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录拦截器
 * 功能说明：<br>
 * 模块名称：NRAdmin<br>
 * 功能描述：UserLoginInterceptor<br>
 * 文件名称: UserLoginInterceptor.java<br>
 * 系统名称：ICELOVE<br>
 * 软件著作权：ICELOVE 版权所有<br>
 * 开发人员：lujun <br>
 * 开发时间：2016-10-11 下午8:40:45<br>
 * 系统版本：1.0.0<br>
 */
@Component
public class UserLoginInterceptor extends BaseHandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(UserLoginInterceptor.class);

    private UserCacheService userCacheService;
    private UserServiceExt userServiceExt;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.init(request, response, handler);
        String reqUrl = request.getServletPath();
        if ("OPTIONS".equals(request.getMethod())) {
            logger.debug("OPTIONS>>> " + reqUrl);
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            logger.error("没有授权信息>>>" + request.getRequestURI());
            sendJsonError(request, response, Constant.LOGIN_TIME_REFUSE, "拒绝访问");
            return false;
        }
        if (userCacheService == null) {
            userCacheService = SpringContext.getBean("userCacheService", UserCacheService.class);
        }
        if(userServiceExt == null) {
            userServiceExt = SpringContext.getBean("userServiceExt", UserServiceExt.class);
        }
        UserSession userSession = userCacheService.getUserSession(token);
        if (userSession == null) {
            logger.error("权限错误(登录超时):openId=" + token);
            sendJsonError(request, response, Constant.LOGIN_TIME_OUT_OVER, "登录超时");
            return false;
        }
        User user = userServiceExt.getById(userSession.getId());
        if(user == null) {
            logger.error("查询不到该用户信息");
            sendJsonError(request, response, Constant.USER_NOT_FOUND, "未查询到该用户信息");
            return false;
        }

        request.setAttribute(Constants.USER_SESSION_KEY, userSession);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //logger.info("<postHandle>");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //logger.info("<afterCompletion>"+handler.getClass().getName());
    }

    public UserLoginInterceptor(UserCacheService userCacheService, UserServiceExt userServiceExt) {
        this.userCacheService = userCacheService;
        this.userServiceExt = userServiceExt;
    }
}
