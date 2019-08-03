package com.yxp.yunstore_common.service.doc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.yxp.yunstore_common.constants.EnumConstant;
import com.yxp.yunstore_common.model.doc.DocNoteinfo;
import com.yxp.yunstore_common.service.base.BaseService;
import com.yxp.yunstore_common.service.base.Result;
import com.yxp.yunstore_common.utils.StringUtil;

public class NoteInfoService extends BaseService{

	
	public Result update(DocNoteinfo noteinfo, int type) {
		Result result = Result.success();
		
		if (StringUtil.isEmpty(noteinfo.getStr("title"))) {
			return Result.failure("请输入标题");
		}
		
		try {

			if (type == EnumConstant.UpdateType.CREATE.getValue()) {
				noteinfo.set("created_at", new Date());
				noteinfo.set("updated_at", new Date());
				noteinfo.save();
			}else if (type == EnumConstant.UpdateType.UPDATE.getValue()) {
				noteinfo.set("updated_at", new Date());
				noteinfo.update();
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
		
		StringBuffer sql = new StringBuffer(" from doc_noteinfo where 1=1");
		
		List<Object> params = new ArrayList<Object>();
		
		int pageNumber = Integer.parseInt(kv.getStr("pageNumber"));
		int pageSize = Integer.parseInt(kv.getStr("pageSize"));
		
		try {
			
			if (StringUtil.isNotEmpty(kv.getStr("title"))) {
				params.add(kv.getStr("title"));
				sql.append(" and title like '%' ? '%' ");
			}
			
			
			
			sql.append(" order by id desc");
			
			Page<DocNoteinfo> page = DocNoteinfo.dao.paginate(pageNumber, pageSize, "select *", sql.toString(), params.toArray());
			
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
