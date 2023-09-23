package cn.ihoway.annotation;

import cn.ihoway.type.AuthorityLevel;

import java.lang.annotation.*;

/**
 * name:程序名称
 * certification:是否进行安全认证 默认 否
 * limitAuthority:访问权限 默认 普通用户
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Processor {
    String name() default ""; //程序名称
    boolean certification() default false; //是否进行安全认证
    AuthorityLevel limitAuthority() default AuthorityLevel.COMMON_MEMBER; //普通用户访问权限
}
