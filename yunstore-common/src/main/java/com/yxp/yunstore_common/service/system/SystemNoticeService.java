package com.yxp.yunstore_common.service.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.yxp.yunstore_common.constants.EnumConstant;
import com.yxp.yunstore_common.model.system.SystemNotice;
import com.yxp.yunstore_common.service.base.BaseService;
import com.yxp.yunstore_common.service.base.Result;
import com.yxp.yunstore_common.utils.StringUtil;

public class SystemNoticeService extends BaseService{

	public Result save(Kv kv) {
		
		SystemNotice notice = null;
		
		if (StringUtil.isEmpty(kv.getStr("content"))) {
			return Result.failure();
		}
		try {
			notice = new SystemNotice();
			notice.set("content", kv.getStr("content"));
			notice.set("created_at", new Date());
			notice.set("status", EnumConstant.NoticeStatus.NEW.getValue());
			notice.save();
		} catch (Exception e) {
			return Result.failure();
		}
		return Result.success();
	}
	
	public Result page(Kv kv) {
		Result result = Result.success();
		int pageNumber = 0;
		int pageSize = 0;
		
		StringBuffer sql = new StringBuffer(" from system_notice where 1=1");
		List<Object> paras = new ArrayList<Object>();
		
		
		if (StringUtil.isNotEmpty(kv.getStr("pageNumber"))) {
			pageNumber = Integer.parseInt(kv.getStr("pageNumber"));
		}
		
		if (StringUtil.isNotEmpty(kv.getStr("pageSize"))) {
			pageSize = Integer.parseInt(kv.getStr("pageSize"));
		}
		
		if (StringUtil.isNotEmpty(kv.getStr("status"))) {
			sql.append(" and status = ?");
			paras.add(kv.get("status"));
		}
		
		sql.append(" order by id desc");
		
		try {
			Page<SystemNotice> page  = SystemNotice.dao.paginate(pageNumber, pageSize, "select *", sql.toString(), paras.toArray());
			if (page.getList().size() == 0) {
				pageNumber = 1;
				page  = SystemNotice.dao.paginate(pageNumber, pageSize, "select *", sql.toString(), paras.toArray());
			}
			result.set("page", page);
		} catch (Exception e) {
			return Result.failure(e.getMessage());
		}
		
		return result;
	}
	
	
	public Result update(Kv kv) {
		Result result = Result.success();
		
		try {
			SystemNotice notice = null;
			if (StringUtil.isNotEmpty(kv.getStr("id"))) {
				notice = SystemNotice.dao.findById(kv.get("id"));
				
				if (kv.getStr("id").equals("0")) {
					List<SystemNotice> notices = SystemNotice.dao.find("select * from system_notice where status = 0");
					List<SystemNotice> notices2 = new ArrayList<>();
					for(SystemNotice notice2 : notices) {
						notice2.set("status", EnumConstant.NoticeStatus.VISITED.getValue());
						notice2.set("updated_at", new Date());
						notices2.add(notice2);
					}
					Db.batchUpdate(notices2, notices2.size());
				}else {
					if (null == notice) {
						return Result.failure("该消息已删除");
					}
					
					if (StringUtil.isNotEmpty(kv.getStr("status"))) {
						int status = Integer.parseInt(kv.getStr("status"));
						if (null == EnumConstant.NoticeStatus.getEnum(status)) {
							return Result.failure("参数错误");
						}
						if (notice.getInt("status") != status) {
							notice.set("status", status);
							notice.set("updated_at", new Date());
							notice.update();
						}
					}
				}
				
				
			}
		} catch (Exception e) {
			return Result.failure(e.getMessage());
		}
		
		return result;
	}
	
	
}
