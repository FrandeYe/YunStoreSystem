package com.yxp.yunstore.yunstore_admin.web.route;

import com.jfinal.config.Routes;
import com.yxp.yunstore.yunstore_admin.config.SysConfig;
import com.yxp.yunstore.yunstore_admin.web.controller.aliPay.AliPayController;
import com.yxp.yunstore.yunstore_admin.web.controller.system.SystemAdminController;
import com.yxp.yunstore.yunstore_admin.web.controller.system.SystemNoticeController;
import com.yxp.yunstore.yunstore_admin.web.controller.system.SystemRoleController;
import com.yxp.yunstore.yunstore_admin.web.controller.system.manage.AdminManageController;

/**
* @ClassName: AdminRoutes  
* @Description: 后台访问页面
* @author Administrator  
* @date 2019年5月23日  
*
 */
public class SystemRoutes extends Routes{

	@Override
	public void config() {
		add("/admin", SystemAdminController.class, SysConfig.viewPath);
		
		add("/admin/manage", AdminManageController.class, SysConfig.viewPath);
		
		add("/notice", SystemNoticeController.class, SysConfig.viewPath);
		
		add("/aliPay", AliPayController.class, SysConfig.viewPath);
		
		add("/role", SystemRoleController.class, SysConfig.viewPath);
	}

}
