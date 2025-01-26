package com.cinema.core.annotation;


import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

// FIXME : AOP Distibuted Lock

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {
    String key(); // 락 키
    long waitTime() default 5;  // 대기 시간 (초)
    long leaseTime() default 3;  // 유지 시간 (초)
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
