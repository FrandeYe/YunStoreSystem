package com.yxp.yunstore.yunstore_admin.web.route;

import com.jfinal.config.Routes;
import com.yxp.yunstore.yunstore_admin.config.SysConfig;
import com.yxp.yunstore.yunstore_admin.web.controller.label.LabelInfoController;
import com.yxp.yunstore.yunstore_admin.web.controller.material.MusicController;
import com.yxp.yunstore.yunstore_admin.web.controller.material.PictureController;
import com.yxp.yunstore.yunstore_admin.web.controller.material.VideoController;

/**
* @ClassName: AdminRoutes  
* @Description: 后台访问页面
* @author Administrator  
* @date 2019年5月23日  
*
 */
public class MaterialRoutes extends Routes{

	@Override
	public void config() {
		add("/material/picture", PictureController.class, SysConfig.viewPath);
		
		add("/material/video", VideoController.class, SysConfig.viewPath);
		
		add("/material/music", MusicController.class, SysConfig.viewPath);
		
		add("/label/labelinfo", LabelInfoController.class, SysConfig.viewPath);
	}

}
