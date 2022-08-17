package com.marsart.plugin.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 被代理的方法标定 注解.
 * 插件对基线版本中定义的功能进行，功能替换或者增量
 * @author qiyao(1210)
 * 2022-04-06
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProxyMethod {
    String id() default "";
}
