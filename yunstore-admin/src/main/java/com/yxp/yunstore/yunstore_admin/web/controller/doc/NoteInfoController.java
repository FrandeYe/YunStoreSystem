package com.yxp.yunstore.yunstore_admin.web.controller.doc;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.upload.UploadFile;
import com.yxp.yunstore.yunstore_admin.config.SysConfig;
import com.yxp.yunstore.yunstore_admin.web.controller.base.BaseController;
import com.yxp.yunstore_common.constants.EnumConstant;
import com.yxp.yunstore_common.model.doc.DocNoteinfo;
import com.yxp.yunstore_common.service.base.Result;
import com.yxp.yunstore_common.service.doc.NoteInfoService;
import com.yxp.yunstore_common.utils.FileUtil;

public class NoteInfoController extends BaseController{
	
	@Inject
	private NoteInfoService noteInfoService;
	
	public void index() {
		render("doc/noteinfo/index.vm");
	}

	
	public void addView() {
		render("doc/noteinfo/add.vm");
	}
	
	
	
	public void save() {
		
		DocNoteinfo noteinfo = getModel(DocNoteinfo.class, "noteinfo");
		noteinfo.set("create_by", getAdminId());
		noteinfo.set("update_by", getAdminId());
		Result result = noteInfoService.update(noteinfo, EnumConstant.UpdateType.CREATE.getValue());
		renderJson(result);
	}
	
	public void update() {
		DocNoteinfo noteinfo = getModel(DocNoteinfo.class, "noteinfo");
		noteinfo.set("update_by", getAdminId());
		Result result = noteInfoService.update(noteinfo, EnumConstant.UpdateType.UPDATE.getValue());
		renderJson(result);
	}
	
	
	public void list() {
		
		Kv kv = Kv.by("pageNumber", get("pageNumber")).set("pageSize", get("pageSize"))
				.set("title", get("title"));
		
		Result result = noteInfoService.page(kv);
		
		
		set("page",  result.get("page"));
		
		render("doc/noteinfo/list.vm");
	}
	
	
	public void editView() {
		Kv kv = Kv.by("id", getInt("id"));
		Result result = noteInfoService.findById(kv);
		set("noteinfo", result.get("noteinfo"));
		render("doc/noteinfo/edit.vm");
	}
	
	
	@Clear
	public void infoView() {
		Kv kv = Kv.by("id", getInt("id"));
		Result result = noteInfoService.findById(kv);
		set("noteinfo", result.get("noteinfo"));
		render("doc/noteinfo/info.vm");
	}
	
	
	public void delete() {
		Kv kv = Kv.by("id", getInt("id"));
		Result result = noteInfoService.remove(kv);
		renderJson(result);
	}
	
	public void ueditUpload(){
		
		// 百度编辑器加载出按钮图标前 会将所有控件的路径 先通过config.json
		// 文件加载出来(包括上传图片路径，视频路径等路径都是通过config.json 文件读取的)
		// 所以某些控件点击不了 是因为 config.json文件没有找到 或者是文件里面的路径有问题
		if ("config".equals(getPara("action"))) {
			// 这里千万注意 "config.json" 文件前方的目录一定要正确
			render("doc/noteinfo/config.json") ;//这里地址写成自己的config.json所在的地址
			return;
		}
		
		UploadFile file = getFile("upfile");
		Result result = FileUtil.upload(file);
		String savePath = result.getStr("savePath").replaceAll("\\\\", "/");
		String fileName = result.getStr("fileName");
        String[] typeArr = fileName.split("\\.");
        String orig = file.getOriginalFileName();
		Map<String, Object> map = new HashMap<>();
		map.put("state", "SUCCESS");  //下面这几个都是必须返回给ueditor的数据
		map.put("url", "/" + SysConfig.uploadPath + savePath);  //文件上传后的路径
		map.put("title", file.getFileName());  //文件名称
		map.put("original", orig);
		map.put("type", "."+ typeArr[1]);
		map.put("size", file.getFile().hashCode());		
		renderJson(map);
	}
	
}
