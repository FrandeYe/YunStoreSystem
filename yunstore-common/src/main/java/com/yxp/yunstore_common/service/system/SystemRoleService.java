package com.yxp.yunstore_common.service.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.yxp.yunstore_common.model.system.SystemMenu;
import com.yxp.yunstore_common.model.system.SystemOper;
import com.yxp.yunstore_common.model.system.SystemRole;
import com.yxp.yunstore_common.model.system.SystemRoleOperRef;
import com.yxp.yunstore_common.service.base.BaseService;
import com.yxp.yunstore_common.service.base.IBaseService;
import com.yxp.yunstore_common.service.base.Result;
import com.yxp.yunstore_common.utils.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SystemRoleService extends BaseService implements IBaseService{

	@Override
	public <T> Result save(T t) {
		
		try {
			SystemRole role = (SystemRole)t;
			
			if (StringUtil.isEmpty(role.getStr("role_name"))) {
				return Result.failure();
			}
			
			SystemRole systemRole = SystemRole.dao.findFirst("select id from system_role where role_name = ?", role.getStr("role_name"));
			
			if (null != systemRole) {
				return Result.failure("该角色已存在");
			}
			
			role.set("created_at", new Date());
			role.save();
			
			return Result.success();
		} catch (Exception e) {
			return Result.failure();
		}
	}

	
	public Result page(Kv kv) {
		Result result = Result.success();
		
		StringBuffer sql = new StringBuffer(" from system_role where 1=1");
		
		List<Object> params = new ArrayList<Object>();
		
		int pageNumber = Integer.parseInt(kv.getStr("pageNumber"));
		int pageSize = Integer.parseInt(kv.getStr("pageSize"));
		
		try {
			
			if (StringUtil.isNotEmpty(kv.getStr("role_name"))) {
				params.add(kv.getStr("role_name"));
				sql.append(" and role_name like '%' ? '%' ");
			}
			
			sql.append(" order by id desc");
			
			Page<SystemRole> page = SystemRole.dao.paginate(pageNumber, pageSize, "select *", sql.toString(), params.toArray());
			
			result.set("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public Result update(SystemRole role) {
		try {
			
			if (StringUtil.isEmpty(role.getStr("role_name"))) {
				return Result.failure();
			}
			
			if (StringUtil.isEmpty(role.getStr("id"))) {
				return Result.failure();
			}
			
			SystemRole systemRole = SystemRole.dao.findFirst("select id from system_role where role_name = ? and id <> ?", role.get("role_name"), role.get("id"));
			
			if (null != systemRole) {
				return Result.failure("该角色已存在");
			}
			
			role.set("updated", new Date());
			role.update();
			
			return Result.success();
		} catch (Exception e) {
			return Result.failure();
		}
	}
	
	/**
	 * 为角色设置权限
	 * */
	@Before(Tx.class)
	public Result updateRoleOper(String operIds, int roleId) {
		try {
			String [] operArr = operIds.split(",");
			Db.update("delete from system_role_oper_ref where role_id = ?", roleId);
			SystemRoleOperRef operRef = null;
			if (StringUtil.isNotEmpty(operIds)) {
				for(String operId : operArr) {
					operRef = new SystemRoleOperRef();
					operRef.set("role_id", roleId).set("oper_id", Integer.parseInt(operId)).save();
				}
			}
			return Result.success();
		} catch (Exception e) {
			return Result.failure();
		}
		
	}
	
	
	public String getRoleOper(int roleId) {
		//查询角色拥有的操作
		Set<String> hasOper = new HashSet<String>();
		JSONArray jsonarray = null;
		try {
			List<SystemOper> operList = SystemOper.dao.find("select o.* from system_oper o, system_role_oper_ref ro where o.id = ro.oper_id and ro.role_id = ? ", roleId);
			if (operList != null) {
				for (SystemOper oper : operList) {
					hasOper.add(oper.getStr("oper_code"));
				}
			}
			
			List<SystemMenu> allMenuList = new ArrayList<>();
			List<SystemOper> allOperList = new ArrayList<>();

			// 所有菜单
			String menu_sql = "select * from system_menu";
			allMenuList = SystemMenu.dao.find(menu_sql);

			// 所有操作
			String oper_sql = "select * from system_oper";
			allOperList = SystemOper.dao.find(oper_sql);

			jsonarray = new JSONArray();
			if (allMenuList != null) {
				JSONObject cldObj = null;
				for (SystemMenu menu : allMenuList) {
					cldObj = new JSONObject();
					int id = menu.getInt("id");
					cldObj.put("id", "menu_" + id);
					cldObj.put("name", menu.get("menu_name"));
					cldObj.put("pId", id == 0 ? menu.get("parent_id") : "menu_" + menu.get("parent_id"));
					cldObj.put("open", false);
					jsonarray.add(cldObj);
				}
			}

			if (allOperList != null) {
				JSONObject cldObj = null;
				String oper_code = null;
				for (SystemOper oper : allOperList) {
					cldObj = new JSONObject();
					oper_code = oper.get("oper_code");
					cldObj.put("id", oper.getInt("id"));
					cldObj.put("name", oper.get("oper_name"));
					cldObj.put("pId", "menu_" + oper.get("menu_id"));
					cldObj.put("code", oper_code);

					if (hasOper.contains(oper_code)) {
						cldObj.put("checked", true);
					}
					cldObj.put("open", false);
					jsonarray.add(cldObj);
				}
			}
		} catch (Exception e) {
			return null;
		}
		return jsonarray.toString();
	}
	

}
