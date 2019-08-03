package com.yxp.yunstore.yunstore_admin.web.route;

import com.jfinal.config.Routes;
import com.yxp.yunstore.yunstore_admin.config.SysConfig;
import com.yxp.yunstore.yunstore_admin.web.controller.index.IndexController;

/**
* @ClassName: AdminRoutes  
* @Description: 后台访问页面
* @author Administrator  
* @date 2019年5月23日  
*
 */
public class BaseRoutes extends Routes{

	@Override
	public void config() {
		add("/", IndexController.class, SysConfig.viewPath);
	}

}
