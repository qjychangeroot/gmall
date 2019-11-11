package com.atguigu.gmall.index.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface GmallCache {
    /**
     * 缓存的前缀
     * @return
     */
    String prefix() default "cache";

    /**
     * 单位是秒
     * @return
     */
    long timeout() default 300L;

    /**
     * 为了防止雪崩，而设置的过期时间的随机值范围
     * @return
     */
    long random() default 300L;
}
