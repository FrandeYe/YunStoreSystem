package com.yxp.yunstore.yunstore_admin.web.controller.system;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.yxp.yunstore.yunstore_admin.config.SysConfig;
import com.yxp.yunstore.yunstore_admin.web.controller.base.BaseController;
import com.yxp.yunstore_common.constants.CommonConstant;
import com.yxp.yunstore_common.constants.EnumConstant;
import com.yxp.yunstore_common.model.system.SystemAdmin;
import com.yxp.yunstore_common.model.system.SystemRegion;
import com.yxp.yunstore_common.model.system.SystemToken;
import com.yxp.yunstore_common.service.base.Result;
import com.yxp.yunstore_common.service.system.SystemAdminService;
import com.yxp.yunstore_common.service.system.SystemRegionService;
import com.yxp.yunstore_common.utils.FileUtil;
import com.yxp.yunstore_common.utils.StringUtil;

public class SystemAdminController extends BaseController{
	
	@Inject
	private SystemAdminService adminService;
	@Inject
	private SystemRegionService regionService;
	
	@Clear
	public void login() {
		
		Kv kv = Kv.by("userInfo", get("userInfo")).set("password", get("password"))
				.set(CommonConstant.INPUT_CODE, get("code"))
				.set(CommonConstant.SESSION_CODE, getSessionAttr(CommonConstant.SESSION_CODE));
		
		Result result = adminService.readAdmin(kv);
		
		if (result.getCode() == EnumConstant.AppErrorCodes.SUCCESS.getValue()) {
			/**登录成功 设置session*/
			SystemAdmin admin = (SystemAdmin)result.get("sessionAdmin");
			SystemToken token = SystemToken.dao.findFirst("select * from system_token where user_id = ?", admin.getInt("id"));
			if (null != token) {
				//had login
				admin.put("token", token.get("token"));
				token.set("token", UUID.randomUUID().toString());
				setSessionAttr(CommonConstant.TOCKEN_KEY, token.get("token"));
				token.update();
			}else {
				//first login
				token = new SystemToken();
				token.set("user_id", admin.get("id"));
				token.set("token", UUID.randomUUID().toString());
				setSessionAttr(CommonConstant.TOCKEN_KEY, token.get("token"));
				token.save();
			}
			setSessionAttr(CommonConstant.SESSION_ADMIN, admin);
		}
		
		renderJson(result);
	}
	
	public void resetPassword() {
		Kv kv = Kv.by("oldPassword", get("oldPassword")).set("newPassword", get("newPassword"))
				.set("confirPassword", get("confirPassword"))
				.set("adminId", get("adminId"));

		Result result = adminService.updatePassword(kv);

		renderJson(result);
	}
	
	
	public void updateInfo() {
		SystemAdmin admin = getModel(SystemAdmin.class, "admin");
		Result result = adminService.updateAdmin(admin, EnumConstant.UpdateType.UPDATE.getValue(), null);
		
		if (result.getCode() == EnumConstant.AppErrorCodes.SUCCESS.getValue()) {
			/**修改成功*/
			SystemAdmin sessionAdmin = getSessionAttr(CommonConstant.SESSION_ADMIN);
			admin.set("head_url", sessionAdmin.get("head_url"));
			setSessionAttr(CommonConstant.SESSION_ADMIN, admin);
		}
		renderJson(result);
	}
	
	
	public void infoView() {
		SystemAdmin admin = commonCode(adminService);
		set("admin", admin);
		render("system/admin/info.vm");
	}
	
	public void editView() {
		SystemAdmin admin = commonCode(adminService);
		
		//省列表
		List<SystemRegion> provinces = regionService.getRegionListByLevel(1);
		
		//市列表
		List<SystemRegion> cities = new ArrayList<>();
		if (StringUtil.isNotEmpty(admin.getStr("province_code"))) {
			SystemRegion region = regionService.getRegionListByCode(admin.getStr("province_code"));
			if (null != region) {
				int parent_id = region.getInt("id");
				cities = regionService.getRegionListByParentId(parent_id);
			}
		}
		
		//县区列表
		List<SystemRegion> counties = new ArrayList<>();
		if (StringUtil.isNotEmpty(admin.getStr("city_code"))) {
			SystemRegion region = regionService.getRegionListByCode(admin.getStr("city_code"));
			if (null != region) {
				int parent_id = region.getInt("id");
				counties = regionService.getRegionListByParentId(parent_id);
			}
		}
		
		set("admin", admin).set("provinces", provinces).set("cities", cities)
		.set("counties", counties);
		render("system/admin/edit.vm");
	}
	
	public void getRegionList() {
		SystemRegion region = regionService.getRegionListByCode(get("code"));
		List<SystemRegion> regions = new ArrayList<>();
		if (null != region) {
			int parent_id = region.getInt("id");
			regions = regionService.getRegionListByParentId(parent_id);
		}
		renderJson(regions);
	}
	
	public void resetPwdView() {
		render("system/admin/reset.vm");
	}
	
	
	public void upload() {
		Result uResult = FileUtil.upload(getFile());
		
		int res = uResult.getCode();
		String fileName = (String) uResult.get("fileName");
		String savePath = uResult.getStr("savePath");
		
		if (res >= 0) {
			uResult = adminService.updateHead(getInt("id"), SysConfig.uploadPath 
					+ savePath);
			if (uResult.getCode() >= 0) {
				SystemAdmin admin = getSessionAttr(CommonConstant.SESSION_ADMIN);
				admin.set("head_url", SysConfig.uploadPath + savePath);
				setSessionAttr(CommonConstant.SESSION_ADMIN, admin);
			}
		}
		
		uResult.set("filePath", SysConfig.uploadPathDomain + SysConfig.uploadPath);
		uResult.set("fileName", fileName);
		
		renderJson(uResult);
	}
	
	
	
	
	
	
	
	
	
	
	
}
