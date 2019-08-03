package com.yxp.yunstore.yunstore_admin.web.controller.material;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.upload.UploadFile;
import com.yxp.yunstore.yunstore_admin.config.SysConfig;
import com.yxp.yunstore.yunstore_admin.web.controller.base.BaseController;
import com.yxp.yunstore_common.constants.EnumConstant;
import com.yxp.yunstore_common.model.material.MaterialVideo;
import com.yxp.yunstore_common.service.base.Result;
import com.yxp.yunstore_common.service.material.MaterialVideoService;
import com.yxp.yunstore_common.utils.FileUtil;

public class VideoController extends BaseController{

	@Inject
	private MaterialVideoService videoService;
	
	public void index() {
		render("material/video/index.vm");
	}
	
	
	public void uploadView() {
		render("material/video/upload.vm");
	}
	
	public void upload() {
		List<UploadFile> uploadFiles = getFiles();
		Result result = null;
		if (null != uploadFiles) {
			for(UploadFile uploadFile: uploadFiles) {
				result = FileUtil.upload(uploadFile);
				if (null != result && result.isSuccess()) {
					MaterialVideo video = new MaterialVideo();
					video.set("v_url", "/"+SysConfig.uploadPath + result.get("savePath")).set("v_name", result.get("fileName"))
					.set("create_by", getAdminId()).set("created_at", new Date()).set("m_type", EnumConstant.MaterialType.VIDEO.getValue());
					Result res = videoService.save(video);
					if (!res.isSuccess()) {
						renderJson(res);
					}
				}
			}
		}
		renderJson(result);
	}
	
	
	public void list() {
		Kv kv = Kv.by("pageNumber", get("pageNumber")).set("pageSize", get("pageSize")).set("type", EnumConstant.MaterialType.VIDEO.getValue());
		Result result = videoService.page(kv);
		renderJson(result.get("page"));
	}
	
	
}
