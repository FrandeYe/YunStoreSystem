package com.yxp.yunstore_common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限点注解，标注在action上面
 * @author defier.lai
 * 2010-4-13 下午04:32:57
 * @version 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermissionPoint {
	/**
	 * 操作代码
	 * @return
	 */
	public abstract String[] points() default {};
}