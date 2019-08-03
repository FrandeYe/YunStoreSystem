package com.yxp.yunstore.yunstore_admin.web.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.yxp.yunstore_common.constants.CommonConstant;

public class BaseInterceptor implements Interceptor{

	@Override
	public void intercept(Invocation inv) {
		System.out.println("拦截器验证session " + inv.getActionKey());
		
		Object sObject = inv.getController().getSessionAttr(CommonConstant.SESSION_ADMIN);
		System.out.println("session admin:>>>" + sObject);
		
		if (null != sObject) {
			inv.getController().set(CommonConstant.SESSION_ADMIN, sObject);
			inv.invoke();
		}else {
			inv.getController().redirect("/");
		}
		System.out.println("After invoking " + inv.getActionKey());
	}

}
