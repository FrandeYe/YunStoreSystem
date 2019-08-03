package com.yxp.yunstore.yunstore_admin.web.controller.label;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.yxp.yunstore.yunstore_admin.web.controller.base.BaseController;
import com.yxp.yunstore_common.constants.EnumConstant;
import com.yxp.yunstore_common.model.label.LabelInfo;
import com.yxp.yunstore_common.service.base.Result;
import com.yxp.yunstore_common.service.label.LabelInfoService;

public class LabelInfoController extends BaseController{
	
	@Inject
	private LabelInfoService labelInfoService;
	
	public void index() {
		render("label/labelinfo/index.vm");
	}

	
	
	public void save() {
		
		LabelInfo labelInfo = getModel(LabelInfo.class, "labelInfo");
		labelInfo.set("create_by", getAdminId());
		Result result = labelInfoService.update(labelInfo, EnumConstant.UpdateType.CREATE.getValue());
		renderJson(result);
	}
	
	public void update() {
		LabelInfo labelInfo = getModel(LabelInfo.class, "labelInfo");
		Result result = labelInfoService.update(labelInfo, EnumConstant.UpdateType.UPDATE.getValue());
		renderJson(result);
	}
	
	
	public void list() {
		
		Kv kv = Kv.by("pageNumber", get("pageNumber")).set("pageSize", get("pageSize"))
				.set("title", get("title"));
		
		Result result = labelInfoService.page(kv);
		
		renderJson(result.get("page"));
	}
	
	
	
	
	
	public void delete() {
		Kv kv = Kv.by("id", getInt("id"));
		Result result = labelInfoService.remove(kv);
		renderJson(result);
	}
	
	
}
