package com.yxp.yunstore.yunstore_admin.web.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;
import com.yxp.yunstore.yunstore_admin.config.SysConfig;

public class SysResourceHandler extends Handler{

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		
		request.setAttribute("resPath", SysConfig.resourcePath);
		
		
		/*
		 * 拦截所有静态资源 js, css, image
		 * int index = target.indexOf(".");
		if (index != -1) {
			target = target.substring(0, index);
		}*/
		
		next.handle(target, request, response, isHandled);
	}

}
