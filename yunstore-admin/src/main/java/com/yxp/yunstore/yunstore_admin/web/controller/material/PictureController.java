package com.yxp.yunstore.yunstore_admin.web.controller.material;

import java.util.List;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.upload.UploadFile;
import com.yxp.yunstore.yunstore_admin.config.SysConfig;
import com.yxp.yunstore.yunstore_admin.web.controller.base.BaseController;
import com.yxp.yunstore_common.model.material.MaterialPicture;
import com.yxp.yunstore_common.service.base.Result;
import com.yxp.yunstore_common.service.material.MaterialPictureService;
import com.yxp.yunstore_common.utils.FileUtil;

public class PictureController extends BaseController{

	@Inject
	private MaterialPictureService pictureService;
	
	public void index() {
		render("material/picture/index.vm");
	}
	
	public void list() {
		Kv kv = Kv.by("pageNumber", get("pageNumber")).set("pageSize", get("pageSize"))
				.set("title", get("title"));
		Result result = pictureService.page(kv);
		renderJson(result.get("page"));
	}
	
	public void uploadView() {
		render("material/picture/upload.vm");
	}
	
	public void upload() {
		List<UploadFile> uploadFiles = getFiles();
		Result result = null;
		MaterialPicture mPicture;
		if (null != uploadFiles) {
			for(UploadFile uploadFile: uploadFiles) {
				result = FileUtil.upload(uploadFile);
				/**service*/
				mPicture = new MaterialPicture();
				mPicture.set("url", "/"+SysConfig.uploadPath + result.getStr("savePath")).set("title", result.getStr("fileName"));
				pictureService.save(mPicture);
			}
		}
		renderJson(result);
	}
	
}
