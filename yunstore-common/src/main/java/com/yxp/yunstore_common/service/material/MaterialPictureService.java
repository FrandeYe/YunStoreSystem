package com.yxp.yunstore_common.service.material;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.yxp.yunstore_common.constants.EnumConstant.DisabledFlag;
import com.yxp.yunstore_common.model.material.MaterialPicture;
import com.yxp.yunstore_common.model.system.SystemAdmin;
import com.yxp.yunstore_common.service.base.BaseService;
import com.yxp.yunstore_common.service.base.Result;
import com.yxp.yunstore_common.utils.StringUtil;

public class MaterialPictureService extends BaseService{

	
	public Result page(Kv kv) {
		Result result = Result.success();
		
		StringBuffer sql = new StringBuffer(" from material_picture where 1=1");
		
		List<Object> params = new ArrayList<Object>();
		
		
		try {
			
			if (StringUtil.isEmpty(kv.getStr("pageNumber")) && StringUtil.isEmpty(kv.getStr("pageSize"))) {
				return Result.failure();
			}
			
			int pageNumber = Integer.parseInt(kv.getStr("pageNumber"));
			int pageSize = Integer.parseInt(kv.getStr("pageSize"));
			
			if (StringUtil.isNotEmpty(kv.getStr("title"))) {
				params.add(kv.get("title"));
				sql.append(" and login_name like '%' ? '%'");
			}
			
			sql.append(" order by id desc");
			
			Page<MaterialPicture> page = MaterialPicture.dao.paginate(pageNumber, pageSize, "select *", sql.toString(), params.toArray());
			
			result.set("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	public Result save(MaterialPicture mPicture) {
		Result result = Result.success();
		if (null == mPicture) {
			return Result.failure();
		}
		
		if (StringUtil.isEmpty(mPicture.getStr("url"))) {
			return Result.failure();
		}
		
		mPicture.save();
		
		return result;
	}
	
	public Result updateDisableFlag(Kv kv) {
		Result result = Result.success();
		if (StringUtil.isNotEmpty(kv.getStr("id")) && StringUtil.isNotEmpty(kv.getStr("disableFlag"))) {
			String sql = "update system_admin set disabled_flag = ? where id = ?";
			int disableFlag = Integer.parseInt(kv.getStr("disableFlag"));
			int id = Integer.parseInt(kv.getStr("id"));
			SystemAdmin admin = SystemAdmin.dao.findFirst("select login_name from system_admin where id = ?", id);
			if ("admin".equals(admin.getStr("login_name"))) {
				return Result.failure("无法修改admin用户");
			}
			if (disableFlag == DisabledFlag.NO.getValue() || disableFlag == DisabledFlag.YES.getValue()) {
				Db.update(sql, disableFlag, id);
			}else {
				return Result.failure("参数错误");
			}
		}else {
			return Result.failure();
		}
		return result;
	}
	
}
