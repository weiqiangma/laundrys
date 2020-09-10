package com.mawkun.app.component.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 日志拦截打印
 */
@Aspect
@Component
public class WebLogAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    public WebLogAspect() {
        this.logger.info("<WebLogAspect> AOP init....");
    }

    @Pointcut("execution(* com.mawkun.admin.controller..*.*(..))")
    public void webLog() {}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint){
        if(this.logger.isInfoEnabled()){
            this.logger.info("=======================================<WebLogAspect>=======================================");
            this.startTime.set(System.currentTimeMillis());
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            this.logger.info("<WebLogAspect> URL : " + request.getRequestURL().toString());
            this.logger.info("<WebLogAspect> HTTP_METHOD : " + request.getMethod());
            this.logger.info("<WebLogAspect> CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
            this.logger.info("<WebLogAspect> ARGS : " + Arrays.toString(joinPoint.getArgs()));
        }
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret){
        if(this.logger.isInfoEnabled()) {
            this.logger.info("<WebLogAspect> RESPONSE : " + ret);
            this.logger.info("<WebLogAspect> SPEND TIME : " + (System.currentTimeMillis() - this.startTime.get()));
            long endTime = System.currentTimeMillis();
            this.startTime.get();
            this.logger.info("<WebLogAspect> 运行时间: " + (endTime - this.startTime.get()) / 1000);
        }
    }
}
