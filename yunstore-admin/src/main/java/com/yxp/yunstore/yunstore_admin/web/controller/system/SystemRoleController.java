package com.yxp.yunstore.yunstore_admin.web.controller.system;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.yxp.yunstore.yunstore_admin.web.controller.base.BaseController;
import com.yxp.yunstore_common.model.system.SystemRole;
import com.yxp.yunstore_common.service.base.Result;
import com.yxp.yunstore_common.service.system.SystemRoleService;

public class SystemRoleController extends BaseController{

	@Inject
	private SystemRoleService roleService;
	
	
	public void index() {
		render("system/role/index.vm");
	}
	
	
	public void list() {
		
		Kv kv = Kv.by("pageNumber", get("pageNumber")).set("pageSize", get("pageSize"))
				.set("role_name", get("roleName"));
		
		Result result = roleService.page(kv);
		
		
		set("page", result.get("page"));
		
		render("system/role/list.vm");
	}
	
	
	public void addView() {
		render("system/role/add.vm");
	}
	
	public void editView() {
		SystemRole role = SystemRole.dao.findById(getInt("id"));
		set("role", role);
		render("system/role/edit.vm");
	}
	
	public void operView() {
		String operJson = roleService.getRoleOper(getInt("id"));
		set("operJson", operJson).set("roleId", getInt("id"));
		render("system/role/oper.vm");
	}
	
	public void save() {
		SystemRole role = getModel(SystemRole.class, "role");
		Result result = roleService.save(role);
		renderJson(result);
	}
	
	
	public void update() {
		SystemRole role = getModel(SystemRole.class, "role");
		Result result = roleService.update(role);
		renderJson(result);
	}
	
	
	public void doOper() {
		Result result = roleService.updateRoleOper(get("operIds"), getInt("roleId"));
		renderJson(result);
	}
	
	public void getRoleOper() {
		String opers = roleService.getRoleOper(getInt("roleId"));
		renderJson(opers);
	}
	
}
