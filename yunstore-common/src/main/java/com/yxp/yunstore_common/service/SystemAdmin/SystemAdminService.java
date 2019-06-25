package com.yxp.yunstore_common.service.SystemAdmin;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.kit.Kv;
import com.yxp.yunstore_common.model.SystemAdmin;
import com.yxp.yunstore_common.service.base.BaseService;
import com.yxp.yunstore_common.service.base.DefaultResult;
import com.yxp.yunstore_common.service.base.Result;
import com.yxp.yunstore_common.service.base.ResultCode;
import com.yxp.yunstore_common.utils.StringUtil;

public class SystemAdminService extends BaseService{
	
	static final void failCode(Result result, ResultCode resultCode, String message){
		
		if (StringUtil.isEmpty(message)) {
			resultCode = new ResultCode(ResultCode.FAILURE);
		}else {
			resultCode = new ResultCode(ResultCode.FAILURE, message);
		}
		
		result.setResultCode(resultCode);
	}

	public Result login(Kv kv) {
		Result result = new DefaultResult();
		ResultCode resultCode = new ResultCode(ResultCode.SUCCESS);
		
		StringBuffer sql = new StringBuffer(" from system_admin where 1=1");
		List<Object> params = new ArrayList<>();
		
		try {
			
			if (StringUtil.isNotEmpty(kv.get("userInfo")) && StringUtil.isNotEmpty(kv.get("password"))) {
				
				sql.append(" and ( login_name = ? or mobile = ? )");
				params.add(kv.get("userInfo"));
				params.add(kv.get("userInfo"));
				sql.append(" and password = ?");
				params.add(kv.get("password"));
				
			}else {
				failCode(result, resultCode, "请输入用户名和密码");
				return result;
				
			}
			
			if (StringUtil.isNotEmpty(kv.get("code"))) {
				/**校验图形验证码*/
			}else {
				failCode(result, resultCode, "请输入图形验证码");
				return result;
			}
			
			SystemAdmin systemAdmin = SystemAdmin.dao.findFirst("select *" + sql, params.toArray());
			
			if (null != systemAdmin) {
				/**用户名，密码校验通过，进入下一步校验， 1、校验验证码； 2、校验账户禁用状态； 3、校验登录错误次数；*/
			}else {
				/**用户名或密码错误*/
				failCode(result, resultCode, "用户名或密码错误");
				return result;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			failCode(result, resultCode, null);
			return result;
		}
		
		result.setResultCode(resultCode);
		return result;
	}
	
}
