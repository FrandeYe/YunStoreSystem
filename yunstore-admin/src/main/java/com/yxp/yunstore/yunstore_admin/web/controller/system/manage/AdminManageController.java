package com.yxp.yunstore.yunstore_admin.web.controller.system.manage;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.yxp.yunstore.yunstore_admin.web.controller.base.BaseController;
import com.yxp.yunstore_common.constants.EnumConstant;
import com.yxp.yunstore_common.model.system.SystemAdmin;
import com.yxp.yunstore_common.model.system.SystemRegion;
import com.yxp.yunstore_common.service.base.Result;
import com.yxp.yunstore_common.service.system.AdminManageService;
import com.yxp.yunstore_common.service.system.SystemAdminService;
import com.yxp.yunstore_common.service.system.SystemRegionService;
import com.yxp.yunstore_common.utils.StringUtil;

public class AdminManageController extends BaseController{

	@Inject
	private AdminManageService manageService;
	@Inject
	private SystemAdminService adminService;
	@Inject
	private SystemRegionService regionService;
	
	public void index() {
		render("system/admin/manage/index.vm");
	}
	
	public void list() {
		
		Kv kv = Kv.by("pageNumber", get("pageNumber")).set("pageSize", get("pageSize"))
				.set("userInfo", get("userInfo"));
		
		Result result = manageService.page(kv);
		
		
		set("page", result.get("page"));
		
		render("system/admin/manage/list.vm");
	}
	
	public void register() {
		SystemAdmin admin = getModel(SystemAdmin.class, "admin");
		String confirPassword = get("confirPassword");
		Result result = adminService.updateAdmin(admin, EnumConstant.UpdateType.CREATE.getValue(), confirPassword);
		renderJson(result);
	}
	
	
	public void registerView() {
		render("system/admin/manage/add.vm");
	}
	
	public void edit() {
		SystemAdmin admin = getModel(SystemAdmin.class, "admin");
		Result result = adminService.updateAdmin(admin, EnumConstant.UpdateType.UPDATE.getValue(), null);
		renderJson(result);
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
				List<SystemRegion> counties = new ArrayList<>();;
				if (StringUtil.isNotEmpty(admin.getStr("city_code"))) {
					SystemRegion region = regionService.getRegionListByCode(admin.getStr("city_code"));
					if (null != region) {
						int parent_id = region.getInt("id");
						counties = regionService.getRegionListByParentId(parent_id);
					}
				}
				
				set("admin", admin).set("provinces", provinces).set("cities", cities)
				.set("counties", counties);
		render("system/admin/manage/edit.vm");
	}
	
	
	public void changeUseStatus() {
		Kv kv = Kv.by("id", get("id")).set("disableFlag", get("disableFlag")).set("currentAdminId", getAdminId());
		renderJson(manageService.updateDisableFlag(kv));
	}
	
	public void del() {
		Kv kv = Kv.by("id", get("id")).set("currentAdminId", getAdminId());
		Result result = manageService.delete(kv);
		renderJson(result);
	}
	
}
