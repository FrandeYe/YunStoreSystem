package com.yxp.yunstore_common.service.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.yxp.yunstore_common.constants.CommonConstant;
import com.yxp.yunstore_common.constants.EnumConstant;
import com.yxp.yunstore_common.model.system.SystemAdmin;
import com.yxp.yunstore_common.model.system.SystemRegion;
import com.yxp.yunstore_common.service.base.BaseService;
import com.yxp.yunstore_common.service.base.Result;
import com.yxp.yunstore_common.utils.EncryptUtil;
import com.yxp.yunstore_common.utils.StringUtil;

public class SystemAdminService extends BaseService{
	
	
	public Result readAdmin(Kv kv) {
		
		Result result = Result.success();
		
		StringBuffer sql = new StringBuffer(" from system_admin where 1=1");
		List<Object> params = new ArrayList<Object>();
		
		try {
			String userInfo = kv.getStr("userInfo");
			String password = kv.getStr("password");
			
			if (StringUtil.isNotEmpty(userInfo) && StringUtil.isNotEmpty(password)) {
				
				sql.append(" and ( login_name = ? or mobile = ? )");
				params.add(kv.get("userInfo"));
				params.add(kv.get("userInfo"));
				
				String salt = Db.queryStr("select salt from system_admin where "
						+ "( login_name = ? or mobile = ? )", userInfo, userInfo);
				
				/**加密校验*/
				if (StringUtil.isNotEmpty(salt)) {
					salt = EncryptUtil.SHA(salt);
				}
				
				if (null == salt) {
					salt = "0";
				}
				password = EncryptUtil.MD5(password, salt);
				
				sql.append(" and password = ?");
				params.add(password);
				
				salt = null;
				password = null;
				
			}else {
				Result.failure("请输入登录信息");
				return result;
				
			}
			
			String inputCode = kv.getStr(CommonConstant.INPUT_CODE);
			String sessionCode = kv.getStr(CommonConstant.SESSION_CODE);
			
			if (StringUtil.isNotEmpty(inputCode)) {
				/**校验图形验证码*/
				
				if (!inputCode.equalsIgnoreCase(sessionCode)) {
					return Result.failure("图形验证码错误");
				}
				
			}else {
				return Result.failure("请输入图形验证码");
			}
			
			SystemAdmin systemAdmin = SystemAdmin.dao.findFirst("select *" + sql, params.toArray());
			
			if (null != systemAdmin) {
				/**用户名，密码，验证码 校验通过，进入下一步校验， 1、校验账户禁用状态； 2、校验登录错误次数；*/
				int status = systemAdmin.getInt("disabled_flag");
				int loginError = systemAdmin.getInt("login_error");
				
				if (status == EnumConstant.DisabledFlag.NO.getValue()) {
					return Result.failure("账号状态异常");
				}
				
				if (loginError >= CommonConstant.LOGIN_ERROR_LIMIT) {
					return Result.failure("登录错误次数过多，请稍后再试");
				}
				systemAdmin.set("last_login_time", new Date());
				systemAdmin.update();
				SystemAdmin sessionAdmin = new SystemAdmin();
				sessionAdmin.set("id", systemAdmin.get("id")).set("login_name", systemAdmin.get("login_name")).set("real_name", systemAdmin.get("real_name"))
							.set("created_at", systemAdmin.get("created_at")).set("remark", systemAdmin.get("remark"))
							.set("mobile", systemAdmin.get("mobile")).set("head_url", systemAdmin.get("head_url"));
				
				result.set("sessionAdmin", sessionAdmin);
			}else {
				/**用户名或密码错误*/
				return Result.failure("用户名或密码错误");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	@Before(Tx.class)
	public Result updatePassword(Kv kv) {
		
		Result result = Result.success();
		try {
			
			String oldPassword = kv.getStr("oldPassword");
			String newPassword = kv.getStr("newPassword");
			String confirPassword = kv.getStr("confirPassword");
			String adminId = kv.getStr("adminId");
			
			if (StringUtil.isNotEmpty(newPassword) && StringUtil.isNotEmpty(confirPassword)) {
				if (!newPassword.equals(confirPassword)) {
					return Result.failure("新密码与确认密码不一致");
				}
			}else {
				return Result.failure("请填写新密码或确认密码");
			}
			
			if (StringUtil.isNotEmpty(oldPassword)) {
				
				if (oldPassword.equals(newPassword)) {
					return Result.failure("新密码与原密码必须不一致");
				}
				
				SystemAdmin admin = SystemAdmin.dao.findById(adminId);
				
				if (null != admin) {
					
					String salt = admin.getStr("salt");
					salt = EncryptUtil.SHA(salt);
					oldPassword = EncryptUtil.MD5(oldPassword, salt);
					String password = admin.getStr("password");
					if (!oldPassword.equals(password)) {
						return Result.failure("输入的原密码错误");
					}
					
					salt = EncryptUtil.getRandomString();
					admin.set("salt", salt);
					salt = EncryptUtil.SHA(salt);
					newPassword = EncryptUtil.MD5(newPassword, salt);
					admin.set("password", newPassword).set("updated_at", new Date());
					admin.update();
					
					salt = null;
					newPassword = null;
					oldPassword = null;
				}else {
					return Result.failure();
				}
				
			}else {
				return Result.failure("请输入原密码");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	public Result updateAdmin(SystemAdmin admin, int type, String confirPassword) {
		
		Result result = Result.success();
		
		try {
			
			if (StringUtil.isEmpty(admin.getStr("login_name"))) {
				return Result.failure("请输入用户名");
			}
			
			/**是创建还是修改操作*/
			if (type == EnumConstant.UpdateType.CREATE.getValue() ) {
				
				if (StringUtil.isEmpty(admin.getStr("password"))) {
					return Result.failure("请输入密码");
				}
				
				if (StringUtil.isEmpty(confirPassword)) {
					return Result.failure("请输入确认密码");
				}
				
				SystemAdmin adminLoginName = SystemAdmin.dao.findFirst("select id from system_admin "
						+ "where login_name = ?", admin.getStr("login_name"));
				
				if (null != adminLoginName) {
					return Result.failure("该用户名已被使用，请更换");
				}
				
				if (StringUtil.isNotEmpty(admin.getStr("mobile"))) {
					SystemAdmin adminMobile = SystemAdmin.dao.findFirst("select id from system_admin "
							+ "where mobile = ?", admin.getStr("mobile"));
					if (null != adminMobile) {
						return Result.failure("该手机号已绑定过账号，请更换");
					}
				}
				
				String salt = EncryptUtil.getRandomString();
				admin.set("salt", salt);
				
				salt = EncryptUtil.SHA(salt);
				String password = EncryptUtil.MD5(admin.getStr("password"), salt);
				admin.set("password", password);
				admin.set("created_at", new Date());
				admin.set("updated_at", new Date());
				admin.save();
				
			}else if ( type == EnumConstant.UpdateType.UPDATE.getValue()) {
				
				if (StringUtil.isNotEmpty(admin.getStr("password")) || StringUtil.isNotEmpty(admin.getStr("salt"))
						|| StringUtil.isNotEmpty(admin.getStr("parent_id"))) {
					return Result.failure("参数错误");
				}
				
				String regionSql = "select * from system_region where region_code = ? and parent_id = ?";
				
				SystemRegion province = null;
				admin.set("province_name", "");
				if (StringUtil.isNotEmpty(admin.getStr("province_code"))) {
					province = SystemRegion.dao.findFirst(regionSql, admin.getStr("province_code"), 0);
					if (null != province) {
						admin.set("province_name", province.get("region_name"));
					}else {
						admin.set("province_code", "");
					}
				}
				
				SystemRegion city = null;
				admin.set("city_name", "");
				if (null != province && StringUtil.isNotEmpty(admin.getStr("city_code"))) {
					city = SystemRegion.dao.findFirst(regionSql, admin.getStr("city_code"), province.getInt("id"));
					if (null != city) {
						admin.set("city_name", city.get("region_name"));
					}else {
						admin.set("city_code", "");
					}
				}
				
				SystemRegion county = null;
				admin.set("county_name", "");
				if (null != city && StringUtil.isNotEmpty(admin.getStr("county_code"))) {
					county = SystemRegion.dao.findFirst(regionSql, admin.getStr("county_code"), city.getInt("id"));
					if (null != county) {
						admin.set("county_name", county.get("region_name"));
					}else {
						admin.set("county_code", "");
					}
				}
				
				SystemAdmin systemAdmin = SystemAdmin.dao.findById(admin.getInt("id"));
				if (null == systemAdmin) {
					return Result.failure("该账号不存在");
				}
				
				SystemAdmin adminLoginName = SystemAdmin.dao.findFirst("select id from system_admin "
						+ "where login_name = ? and id <> ?", admin.getStr("login_name"), admin.getInt("id"));
				
				if (null != adminLoginName) {
					return Result.failure("该用户名已被使用，请更换");
				}
				
				if (StringUtil.isNotEmpty(admin.getStr("mobile"))) {
					SystemAdmin adminMobile = SystemAdmin.dao.findFirst("select id from system_admin "
							+ "where mobile = ? and id <> ?", admin.getStr("mobile"), admin.getInt("id"));
					if (null != adminMobile) {
						return Result.failure("该手机号已绑定过账号，请更换");
					}
				}
				
				admin.set("updated_at", new Date());
				admin.update();
			}else {
				return Result.failure("未知操作类型");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
		
	}
	

	
	public Result updateHead(int id, String headUrl) {
		
		Result result = Result.success();
		
		try {
			SystemAdmin admin = new SystemAdmin();
			admin.set("id", id).set("head_url", headUrl);
			admin.update();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	public SystemAdmin getAdminById(int id) {
		return SystemAdmin.dao.findById(id);
	}
	
	
	
	
	
	
	
	
}
