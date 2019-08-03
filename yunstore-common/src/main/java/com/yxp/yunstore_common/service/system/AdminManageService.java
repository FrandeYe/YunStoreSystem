package com.yxp.yunstore_common.service.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.yxp.yunstore_common.constants.EnumConstant.DisabledFlag;
import com.yxp.yunstore_common.model.system.SystemAdmin;
import com.yxp.yunstore_common.model.system.SystemAdminInvalid;
import com.yxp.yunstore_common.service.base.BaseService;
import com.yxp.yunstore_common.service.base.Result;
import com.yxp.yunstore_common.utils.StringUtil;

public class AdminManageService extends BaseService{

	
	public Result page(Kv kv) {
		Result result = Result.success();
		
		StringBuffer sql = new StringBuffer(" from system_admin where 1=1");
		
		List<Object> params = new ArrayList<Object>();
		
		int pageNumber = Integer.parseInt(kv.getStr("pageNumber"));
		int pageSize = Integer.parseInt(kv.getStr("pageSize"));
		
		try {
			
			if (StringUtil.isNotEmpty(kv.getStr("userInfo"))) {
				params.add(kv.getStr("userInfo"));
				params.add(kv.getStr("userInfo"));
				sql.append(" and (login_name like '%' ? '%' or mobile like '%' ? '%')");
			}
			
			if (StringUtil.isNotEmpty(kv.getStr("login_name"))) {
				params.add(kv.get("login_name"));
				sql.append(" and login_name like '%' ? '%'");
			}
			
			if (StringUtil.isNotEmpty(kv.get("mobile"))) {
				params.add(kv.get("mobile"));
				sql.append(" and mobile like '%' ? '%'");
			}
			
			sql.append(" order by id desc");
			
			Page<SystemAdmin> page = SystemAdmin.dao.paginate(pageNumber, pageSize, "select *", sql.toString(), params.toArray());
			
			result.set("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public Result updateDisableFlag(Kv kv) {
		Result result = Result.success();
		try {
			
			if (StringUtil.isEmpty(kv.getStr("currentAdminId"))) {
				return Result.failure();
			}
			
			SystemAdmin cAdmin = SystemAdmin.dao.findById(kv.get("currentAdminId"));
			
			if (null == cAdmin) {
				return Result.failure();
			}
			
			
			if (StringUtil.isNotEmpty(kv.getStr("id")) && StringUtil.isNotEmpty(kv.getStr("disableFlag"))) {
				String sql = "update system_admin set disabled_flag = ? where id = ?";
				int disableFlag = Integer.parseInt(kv.getStr("disableFlag"));
				int id = Integer.parseInt(kv.getStr("id"));
				SystemAdmin admin = SystemAdmin.dao.findFirst("select login_name from system_admin where id = ?", id);
				if ("admin".equals(admin.getStr("login_name"))) {
					return Result.failure("无法修改admin用户");
				}
				if (cAdmin.getStr("login_name").equals(admin.getStr("login_name"))) {
					return Result.failure("不能禁用自己");
				}
				if (disableFlag == DisabledFlag.NO.getValue() || disableFlag == DisabledFlag.YES.getValue()) {
					Db.update(sql, disableFlag, id);
				}else {
					return Result.failure("参数错误");
				}
			}else {
				return Result.failure();
			}
			
		} catch (Exception e) {
			return Result.failure();
		}
		return result;
	}
	
	
	public Result delete(Kv kv) {
		Result result = Result.success();
		
		try {
			if (StringUtil.isEmpty(kv.get("id"))) {
				return Result.failure();
			}
			
			SystemAdmin admin = SystemAdmin.dao.findById(kv.get("id"));
			
			if (StringUtil.isEmpty(kv.get("currentAdminId"))) {
				return Result.failure();
			}
			
			SystemAdmin cAdmin = SystemAdmin.dao.findById(kv.get("currentAdminId"));
			
			if (null != admin && null != cAdmin) {
				if (admin.getStr("login_name").equals(cAdmin.getStr("login_name")) || admin.getStr("login_name").equals("admin")) {
					return Result.failure("无法删除该用户");
				}else {
					//备份删除的用户
					SystemAdminInvalid adminInvalid = new SystemAdminInvalid();
					BeanUtils.copyProperties(admin, adminInvalid);
					adminInvalid.set("admin_id", admin.get("id")).set("oper_time", new Date());
					adminInvalid.save();
					admin.delete();
				}
			}else {
				return Result.failure();
			}
			
			
		} catch (Exception e) {
			return Result.failure();
		}
		
		return result;
	}
	
}
