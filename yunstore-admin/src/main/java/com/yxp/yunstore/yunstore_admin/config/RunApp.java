package com.yxp.yunstore.yunstore_admin.config;

import com.jfinal.server.undertow.UndertowServer;

public class RunApp {

	public static void main(String[] args) {
		UndertowServer.create(SysConfig.class,"undertow.properties").start();
	}
	
	
}
