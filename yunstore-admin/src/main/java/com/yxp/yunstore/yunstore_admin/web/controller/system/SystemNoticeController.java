package com.yxp.yunstore.yunstore_admin.web.controller.system;

import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.yxp.yunstore.yunstore_admin.web.controller.base.BaseController;
import com.yxp.yunstore_common.service.base.Result;
import com.yxp.yunstore_common.service.system.SystemNoticeService;

public class SystemNoticeController extends BaseController{

	@Inject
	private SystemNoticeService noticeService;
	
	@Clear
	public void sendNotice() {
		Kv kv = Kv.by("content", get("content"));
		Result result = noticeService.save(kv);
		renderJson(result);
	}
	
	public void noticeList() {
		Kv kv = Kv.by("pageNumber", get("pageNumber")).set("pageSize", get("pageSize"))
				.set("status", get("status"));
		Result result = noticeService.page(kv);
		renderJson(result.get("page"));
	}
	
	
	public void update() {
		Kv kv = Kv.by("id", get("id")).set("status", get("status"));
		Result result = noticeService.update(kv);
		renderJson(result);
	}
	
	
}
