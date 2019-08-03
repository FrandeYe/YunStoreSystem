package com.yxp.yunstore_common.service.system;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.yxp.yunstore_common.model.system.SystemRegion;
import com.yxp.yunstore_common.service.base.BaseService;
import com.yxp.yunstore_common.service.base.Result;
import com.yxp.yunstore_common.utils.StringUtil;

public class SystemRegionService extends BaseService{

	
	
	public Result page(Kv kv) {
		
		Result result = Result.success();
		
		StringBuffer sql = new StringBuffer("from system_region where 1=1");
		
		List<Object> params = new ArrayList<>();
		
		if (StringUtil.isNotEmpty(kv.get("region_level"))) {
			params.add(kv.get("region_level"));
			sql.append(" and region_level = ?");
		}
		
		Page<SystemRegion> page = SystemRegion.dao.paginate(1, 10000, "select * ", sql.toString(), params);
		
		result.set("page", page);
		
		return result;
	}
	
	
	public List<SystemRegion> getRegionListByLevel(int level) {
		String sql = "select * from system_region where region_level = ?";
		return SystemRegion.dao.find(sql, level);
	}
	
	public SystemRegion getRegionListByCode(String code) {
		String sql = "select * from system_region where region_code = ?";
		return SystemRegion.dao.findFirst(sql, code);
	}

	public List<SystemRegion> getRegionListByParentId(int id) {
		String sql = "select * from system_region where parent_id = ?";
		return SystemRegion.dao.find(sql, id);
	}
	
	
}
