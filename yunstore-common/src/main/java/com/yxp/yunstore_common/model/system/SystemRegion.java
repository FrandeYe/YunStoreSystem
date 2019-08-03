package com.yxp.yunstore_common.model.system;

import com.yxp.yunstore_common.model.base.BaseSystemRegion;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class SystemRegion extends BaseSystemRegion<SystemRegion> {
	
	public static final SystemRegion dao = new SystemRegion().dao();
	
	
	public String getRegionNameByCode(String code) {
		String sql = "select region_name from system_region where region_code = ?";
		return dao.findFirst(sql, code).getStr("region_name");
	}
}