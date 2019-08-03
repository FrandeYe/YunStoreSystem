package com.yxp.yunstore_common.service.trader;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.yxp.yunstore_common.model.order.TraderOrder;
import com.yxp.yunstore_common.service.base.BaseService;
import com.yxp.yunstore_common.service.base.IBaseService;
import com.yxp.yunstore_common.service.base.Result;
import com.yxp.yunstore_common.utils.StringUtil;

public class TraderOrderService extends BaseService implements IBaseService{



	@Override
	public <T> Result save(T t) {
		
		Result result = Result.success();
		try {
			
			TraderOrder traderOrder = (TraderOrder) t;
			if (StringUtil.isEmpty(traderOrder.getStr("tenant_org_id")) || StringUtil.isEmpty(traderOrder.getStr("out_trade_no"))
					|| StringUtil.isEmpty(traderOrder.getStr("initiator_user_id")) || StringUtil.isEmpty(traderOrder.getStr("buyer_id"))
					|| StringUtil.isEmpty(traderOrder.getStr("order_name")) || StringUtil.isEmpty(traderOrder.getStr("total_amount"))
					|| StringUtil.isEmpty(traderOrder.getStr("trader_type")) || StringUtil.isEmpty(traderOrder.getStr("fund_flow"))
					|| StringUtil.isEmpty(traderOrder.getStr("order_time")) || StringUtil.isEmpty(traderOrder.getStr("initiator_user_id"))
					|| StringUtil.isEmpty(traderOrder.getStr("pay_status"))) {
				return Result.failure();
			}
			
			TraderOrder order = TraderOrder.dao.findFirst("select id from trader_order where out_trade_no = ?", traderOrder.getStr("out_trade_no"));
			if (null != order) {
				return Result.failure("订单已存在");
			}
			traderOrder.save();
		} catch (Exception e) {
			return Result.failure();
		}
		return result;
	}
	
	
	public Result update(TraderOrder traderOrder) {
		Result result = Result.success();
		
		try {
			if (StringUtil.isEmpty(traderOrder.getStr("tenant_org_id")) || StringUtil.isEmpty(traderOrder.getStr("out_trade_no"))
					|| StringUtil.isEmpty(traderOrder.getStr("initiator_user_id")) || StringUtil.isEmpty(traderOrder.getStr("buyer_id"))
					|| StringUtil.isEmpty(traderOrder.getStr("order_name")) || StringUtil.isEmpty(traderOrder.getStr("total_amount"))
					|| StringUtil.isEmpty(traderOrder.getStr("trader_type")) || StringUtil.isEmpty(traderOrder.getStr("fund_flow"))
					|| StringUtil.isEmpty(traderOrder.getStr("order_time")) || StringUtil.isEmpty(traderOrder.getStr("initiator_user_id"))
					|| StringUtil.isEmpty(traderOrder.getStr("pay_status"))) {
				return Result.failure();
			}
			traderOrder.update();
		} catch (Exception e) {
			return Result.failure();
		}
		
		return result;
	}
	
	
	
	public Result page(Kv kv) {
		Result result = Result.success();
		
		StringBuffer sql = new StringBuffer(" from trader_order where 1=1");
		
		List<Object> params = new ArrayList<Object>();
		
		int pageNumber = Integer.parseInt(kv.getStr("pageNumber"));
		int pageSize = Integer.parseInt(kv.getStr("pageSize"));
		
		try {
			
			if (StringUtil.isNotEmpty(kv.getStr("order_name"))) {
				params.add(kv.getStr("order_name"));
				sql.append(" and order_name like '%' ? '%' ");
			}
			
			if (StringUtil.isNotEmpty(kv.getStr("out_trade_no"))) {
				params.add(kv.getStr("out_trade_no"));
				sql.append(" and out_trade_no like '%' ? '%' ");
			}
			
			sql.append(" order by id desc");
			
			Page<TraderOrder> page = TraderOrder.dao.paginate(pageNumber, pageSize, "select *", sql.toString(), params.toArray());
			
			result.set("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
