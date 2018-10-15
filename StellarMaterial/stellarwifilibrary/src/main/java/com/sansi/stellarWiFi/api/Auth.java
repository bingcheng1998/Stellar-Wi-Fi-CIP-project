package com.sansi.stellarWiFi.api;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface Auth {
    String value() default "";
    boolean isAuthRequire() default false;
    boolean isCheckStatus() default true;
}