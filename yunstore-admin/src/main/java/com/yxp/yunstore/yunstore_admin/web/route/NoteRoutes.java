package com.yxp.yunstore.yunstore_admin.web.route;

import com.jfinal.config.Routes;
import com.yxp.yunstore.yunstore_admin.config.SysConfig;
import com.yxp.yunstore.yunstore_admin.web.controller.doc.NoteBookController;
import com.yxp.yunstore.yunstore_admin.web.controller.doc.NoteInfoController;

/**
* @ClassName: AdminRoutes  
* @Description: 后台访问页面
* @author Administrator  
* @date 2019年5月23日  
*
 */
public class NoteRoutes extends Routes{

	@Override
	public void config() {
		add("/doc/notebook", NoteBookController.class, SysConfig.viewPath);
		
		add("/doc/noteinfo", NoteInfoController.class, SysConfig.viewPath);
	}

}
