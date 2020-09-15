package com.mawkun.admin.component.interpectors;

import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.service.UserCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器拦截请求获取用户信息存放request里，后续参数解析器会从request中获取用户信息放在注解上
 * @Date 2020/7/7 14:03
 * @Author mawkun
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserCacheService userCacheService;

    @Bean
    public UserCacheService createUserCacheService() {return new UserCacheService();}

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        UserSession session = userCacheService.getAdminSession(authorization);
        request.setAttribute("session", session);
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
