package com.yxp.yunstore.yunstore_admin.web.interceptor;

import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import com.jfinal.validate.Validator;

public class LoginValidator extends Validator{

	@Override
	protected void validate(Controller c) {
		setRet(Ret.fail());
		this.setShortCircuit(true);
		
		validateRequiredString("", "", "");
	}

	@Override
	protected void handleError(Controller c) {
		// TODO Auto-generated method stub
		
	}

}
