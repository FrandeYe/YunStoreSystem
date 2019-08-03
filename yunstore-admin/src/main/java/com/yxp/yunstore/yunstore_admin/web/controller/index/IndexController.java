package com.yxp.yunstore.yunstore_admin.web.controller.index;
import java.math.BigDecimal;

import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.yxp.yunstore_common.constants.CommonConstant;
import com.yxp.yunstore_common.constants.EnumConstant;
import com.yxp.yunstore_common.utils.DateUtil;
import com.yxp.yunstore_common.utils.EncryptUtil;


public class IndexController extends Controller {
	
	
	/**
	 * 首页Action
	 */
	@Clear
	public void index() {
		setCode();
		render("index/login.vm");
	}
	
	public void out() {
		removeSessionAttr(CommonConstant.SESSION_ADMIN);
		setCode();
		render("index/login.vm");
	}
	
	@Clear
	public void setCode (){
		
		String code = EncryptUtil.getRandomCode();
		
		setSessionAttr(CommonConstant.SESSION_CODE, code);
		
		set("codeStr", code);
		
		renderJson(code);
	}
	
	public void home() {
		set("session", getSessionAttr(CommonConstant.SESSION_ADMIN));
		render("index/index.vm");
	}
	
	public void welcome() {
		//所有文章数 = 笔记数
		int total_note = Db.queryInt("select count(1) from doc_noteinfo");
		//所有用户数
		int total_user = Db.queryInt("select count(1) from system_admin");
		//所有反馈信息
		int total_notice = Db.queryInt("select count(1) from system_notice");
		//所有商品数 默认 0
		int total_goods = 0;
		//总交易额 = 所有已支付订单付款金额之和
		BigDecimal total_amount = Db.queryBigDecimal("select ifnull(sum(total_amount),0) from trader_order where pay_status = ?", EnumConstant.PayStatus.PAYED.getValue());
		//总订单数 = 所有订单数之和
		int total_order = Db.queryInt("select count(1) from trader_order");
		
		String monthFirstDay = DateUtil.MonthFirstDay();
		String monthLastDay = DateUtil.MonthLastDay();
		String lMonthFirstDay = DateUtil.lastMonthFirstDay();
		String lMonthLastDay = DateUtil.lastMonthLastDay();
		
		//本月新增文章数
		int month_note = Db.queryInt("select count(1) from doc_noteinfo where created_at >= ? and created_at <= ?", monthFirstDay, monthLastDay);
		//上月
		int lmonth_note = Db.queryInt("select count(1) from doc_noteinfo where created_at >= ? and created_at <= ?", lMonthFirstDay, lMonthLastDay);
		
		//上月新增用户数
		int month_user = Db.queryInt("select count(1) from system_admin where created_at >= ? and created_at <= ?", monthFirstDay, monthLastDay);
		//上月
		int lmonth_user = Db.queryInt("select count(1) from system_admin where created_at >= ? and created_at <= ?", lMonthFirstDay, lMonthLastDay);
		
		set("total_note", total_note).set("total_user", total_user).set("total_notice", total_notice).set("total_goods", total_goods)
		.set("total_amount", total_amount).set("total_order", total_order).set("month_note", month_note).set("lmonth_note", lmonth_note)
		.set("month_user", month_user).set("lmonth_user", lmonth_user);
		render("index/welcome.vm");
	}
	
	
	
	
	
	
	
	
}