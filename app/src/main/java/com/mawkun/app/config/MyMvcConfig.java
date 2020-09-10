package com.mawkun.app.config;

import com.mawkun.app.component.interpectors.AuthenticationInterceptor;
import com.mawkun.app.component.interpectors.UserLoginInterceptor;
import com.mawkun.core.base.service.UserCacheService;
import com.mawkun.core.spring.resolver.LoginArgumentResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
public class MyMvcConfig extends WebMvcConfigurationSupport {

    @Autowired
    private UserCacheService userCacheService;

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new LoginArgumentResolver());
        super.addArgumentResolvers(argumentResolvers);
    }

    //解决跨域问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("解决跨域问题>>>>");
        registry.addMapping("/**").allowedOrigins("*").allowCredentials(true).allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE").maxAge(3600);
    }

    //创建登录拦截器并注入userCacheService
    @Bean
    UserLoginInterceptor createLoginInterceptor() {
        return new UserLoginInterceptor(userCacheService);
    }
    //认证的拦截器，将用户session存在request，后续登录注解解析有用到
    @Bean
    AuthenticationInterceptor createAuthenticationInterceptor() {return new AuthenticationInterceptor(); }

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(createLoginInterceptor()).addPathPatterns("/adm/**");
        //registry.addInterceptor(createAuthenticationInterceptor());
        super.addInterceptors(registry);
    }

    //添加swagger-ui.html资源不然访问不到
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**").addResourceLocations("file:/D:/Images/");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }
}
