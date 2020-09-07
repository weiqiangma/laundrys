package com.mawkun.core.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mawkun
 * @date 2020-08-19 20:40:42
 */
//注解的生命周期，表示注解会被保留到什么阶段，可以选择编译阶段、类加载阶段，或运行阶段
@Retention(RetentionPolicy.RUNTIME)
//注解作用的位置，ElementType.METHOD表示该注解仅能作用于方法上
@Target(ElementType.PARAMETER)
public @interface LoginedAuth {
	
}
