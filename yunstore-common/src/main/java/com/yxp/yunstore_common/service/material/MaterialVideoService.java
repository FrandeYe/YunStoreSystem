package com.yxp.yunstore_common.service.material;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.yxp.yunstore_common.model.material.MaterialVideo;
import com.yxp.yunstore_common.service.base.BaseService;
import com.yxp.yunstore_common.service.base.Result;
import com.yxp.yunstore_common.utils.StringUtil;

public class MaterialVideoService extends BaseService{

	
	public Result page(Kv kv) {
		Result result = Result.success();
		
		StringBuffer sql = new StringBuffer(" from material_video where 1=1");
		
		List<Object> params = new ArrayList<Object>();
		
		
		try {
			
			if (StringUtil.isEmpty(kv.getStr("pageNumber")) && StringUtil.isEmpty(kv.getStr("pageSize"))) {
				return Result.failure();
			}
			
			int pageNumber = Integer.parseInt(kv.getStr("pageNumber"));
			int pageSize = Integer.parseInt(kv.getStr("pageSize"));
			
			if (StringUtil.isNotEmpty(kv.getStr("title"))) {
				params.add(kv.get("title"));
				sql.append(" and title like '%' ? '%'");
			}
			
			if (StringUtil.isNotEmpty(kv.getStr("type"))) {
				params.add(kv.getStr("type"));
				sql.append(" and m_type = ?");
			}
			
			sql.append(" order by id desc");
			
			Page<MaterialVideo> page = MaterialVideo.dao.paginate(pageNumber, pageSize, "select *", sql.toString(), params.toArray());
			
			result.set("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	public Result save(MaterialVideo mVideo) {
		Result result = Result.success();
		if (null == mVideo) {
			return Result.failure();
		}
		
		if (StringUtil.isEmpty(mVideo.getStr("v_url"))) {
			return Result.failure();
		}
		
		mVideo.save();
		
		return result;
	}
	
	
}
