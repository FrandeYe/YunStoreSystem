package com.yxp.yunstore_common.model.doc;

import com.yxp.yunstore_common.model.base.BaseDocNoteinfo;
import com.yxp.yunstore_common.model.system.SystemAdmin;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class DocNoteinfo extends BaseDocNoteinfo<DocNoteinfo> {
	
	public static final DocNoteinfo dao = new DocNoteinfo().dao();
	
	public SystemAdmin getUpdateAdmin() {
		return SystemAdmin.dao.findById(getInt("update_by"));
	}
	
	public SystemAdmin getCreateAdmin() {
		return SystemAdmin.dao.findById(getInt("create_by"));
	}
	
}
