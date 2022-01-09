package cn.ihoway.annotation;

import cn.ihoway.type.AuthorityLevel;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Processor {
    String name() default ""; //程序名称
    boolean certification() default false; //是否进行安全认证
    AuthorityLevel limitAuthority() default AuthorityLevel.COMMONMEMBER; //普通用户访问权限
}
