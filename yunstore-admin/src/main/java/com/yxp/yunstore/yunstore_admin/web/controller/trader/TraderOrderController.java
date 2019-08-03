package com.yxp.yunstore.yunstore_admin.web.controller.trader;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.yxp.yunstore.yunstore_admin.web.controller.base.BaseController;
import com.yxp.yunstore_common.service.base.Result;
import com.yxp.yunstore_common.service.trader.TraderOrderService;

public class TraderOrderController extends BaseController{

	@Inject
	private TraderOrderService orderService;
	
	
	public void list() {
		
		Kv kv = Kv.by("pageNumber", get("pageNumber")).set("pageSize", get("pageSize"))
				.set("out_trade_no", get("out_trade_no")).set("order_name", get("order_name"));
		
		Result result = orderService.page(kv);
		
		set("page", result.get("page"));
		
		render("aliPay/list.vm");
	}
	
}
