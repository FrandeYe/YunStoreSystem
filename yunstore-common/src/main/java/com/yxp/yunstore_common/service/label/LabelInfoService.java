package com.yxp.yunstore_common.service.label;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.yxp.yunstore_common.constants.EnumConstant;
import com.yxp.yunstore_common.model.doc.DocNoteinfo;
import com.yxp.yunstore_common.model.label.LabelInfo;
import com.yxp.yunstore_common.service.base.BaseService;
import com.yxp.yunstore_common.service.base.Result;
import com.yxp.yunstore_common.utils.StringUtil;

public class LabelInfoService extends BaseService{

	
	public Result update(LabelInfo labelInfo, int updateType) {
		Result result = Result.success();
		
		if (StringUtil.isEmpty(labelInfo.getStr("title"))) {
			return Result.failure("请输入标签名");
		}
		
		try {

			LabelInfo info = LabelInfo.dao.findFirst("select id from label_info where title = ?", labelInfo.getStr("title"));
			
			if (null != info) {
				return Result.failure("该标签名已存在");
			}
			
			if (updateType == EnumConstant.UpdateType.CREATE.getValue()) {
				labelInfo.set("created_at", new Date());
				labelInfo.set("updated_at", new Date());
				labelInfo.save();
			}else if (updateType == EnumConstant.UpdateType.UPDATE.getValue()) {
				labelInfo.set("updated_at", new Date());
				labelInfo.update();
			}else {
				return Result.failure("未知错误");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	public Result page(Kv kv) {
		Result result = Result.success();
		
		StringBuffer sql = new StringBuffer(" from label_info where 1=1");
		
		List<Object> params = new ArrayList<Object>();
		
		int pageNumber = Integer.parseInt(kv.getStr("pageNumber"));
		int pageSize = Integer.parseInt(kv.getStr("pageSize"));
		
		try {
			
			if (StringUtil.isNotEmpty(kv.getStr("title"))) {
				params.add(kv.getStr("title"));
				sql.append(" and title like '%' ? '%' ");
			}
			
			
			
			sql.append(" order by id desc");
			
			Page<LabelInfo> page = LabelInfo.dao.paginate(pageNumber, pageSize, "select *", sql.toString(), params.toArray());
			
			result.set("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public Result findById(Kv kv) {
		Result result = Result.success();
		String id = kv.getStr("id");
		if (StringUtil.isNotEmpty(id)) {
			DocNoteinfo  noteinfo = DocNoteinfo.dao.findById(id);
			result.set("noteinfo", noteinfo);
		}else {
			return Result.failure();
		}
		return result;
	}
	
	
	@Before(Tx.class)
	public Result remove(Kv kv) {
		
		Result result = Result.success();
		String id = kv.getStr("id");
		if (StringUtil.isNotEmpty(id)) {
			DocNoteinfo.dao.deleteById(id);
		}else {
			return Result.failure();
		}
		
		return result;
	}
	
}
