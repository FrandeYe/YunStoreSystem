package com.yxp.yunstore.yunstore_admin.web.controller.base;

import com.jfinal.core.Controller;
import com.yxp.yunstore_common.constants.CommonConstant;
import com.yxp.yunstore_common.model.system.SystemAdmin;
import com.yxp.yunstore_common.service.system.SystemAdminService;

public class BaseController extends Controller{

	
	public int getAdminId() {
		SystemAdmin admin = getSessionAttr(CommonConstant.SESSION_ADMIN);
		return admin.getInt("id");
	}
	
	public SystemAdmin commonCode(SystemAdminService adminService) {
		int id = getInt("id");
		SystemAdmin admin = adminService.getAdminById(id);
		SystemAdmin systemAdmin = new SystemAdmin();
		systemAdmin.set("login_name", admin.get("login_name")).set("mobile", admin.get("mobile")).set("gender", admin.get("gender"))
			.set("birth", admin.get("birth")).set("province_name", admin.get("province_name")).set("province_code", admin.get("province_code"))
			.set("city_name", admin.get("city_name")).set("city_code", admin.get("city_code")).set("county_name", admin.get("county_name"))
			.set("county_code", admin.get("county_code")).set("remark", admin.get("remark")).set("id", admin.get("id")).set("head_url", admin.get("head_url"));
		return systemAdmin;
	}
	
	public SystemAdmin getSessionAdmin() {
		SystemAdmin systemAdmin = getSessionAttr(CommonConstant.SESSION_ADMIN);
		return systemAdmin;
	}
	
		
	
	public static void main(String[] args) {
		
	}
	
}
