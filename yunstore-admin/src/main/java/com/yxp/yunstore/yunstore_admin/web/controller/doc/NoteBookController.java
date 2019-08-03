package com.yxp.yunstore.yunstore_admin.web.controller.doc;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.yxp.yunstore.yunstore_admin.web.controller.base.BaseController;
import com.yxp.yunstore_common.constants.EnumConstant;
import com.yxp.yunstore_common.model.doc.DocNoteBook;
import com.yxp.yunstore_common.service.base.Result;
import com.yxp.yunstore_common.service.doc.NoteBookService;

public class NoteBookController extends BaseController{

	@Inject
	private NoteBookService bookService;
	
	public void index() {
		render("doc/notebook/index.vm");
	}
	
	
	public void save() {
		DocNoteBook noteBook = getModel(DocNoteBook.class, "notebook");
		noteBook.set("create_by", getAdminId());
		noteBook.set("update_by", getAdminId());
		Result result = bookService.update(noteBook, EnumConstant.UpdateType.CREATE.getValue());
		renderJson(result);
	}
	
	public void remove() {
		Kv kv = Kv.by("id", get("id"));
		Result result = bookService.delete(kv);
		renderJson(result);
	}
	
	
	public void edit() {
		DocNoteBook noteBook = getModel(DocNoteBook.class, "noteBook");
		noteBook.set("update_by", getAdminId());
		Result result = bookService.update(noteBook, EnumConstant.UpdateType.UPDATE.getValue());
		renderJson(result);
	}
	
	
	public void list() {
		Kv kv = Kv.by("pageNumber", get("pageNumber")).set("pageSize", get("pageSize"))
				.set("title", get("title"));
		
		Result result = bookService.page(kv);
		
		set("page",  result.get("page"));
		
		render("doc/notebook/list.vm");
	}
	
	public void addView() {
		render("doc/notebook/add.vm");
	}
	
}
