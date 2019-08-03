package com.yxp.yunstore.yunstore_admin.web.controller.aliPay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.yxp.yunstore.yunstore_admin.config.AliPayConfig;
import com.yxp.yunstore.yunstore_admin.web.controller.base.BaseController;
import com.yxp.yunstore_common.constants.EnumConstant;
import com.yxp.yunstore_common.model.order.TraderOrder;
import com.yxp.yunstore_common.service.base.Result;
import com.yxp.yunstore_common.service.system.SystemNoticeService;
import com.yxp.yunstore_common.service.trader.TraderOrderService;

/**
 * 
* @ClassName: AliPayController  
* @Description: TODO
* @author Administrator  
* @date 2019年7月30日  
*
 */
public class AliPayController extends BaseController{
	
	@Inject
	private SystemNoticeService noticeService;
	@Inject
	private TraderOrderService orderService;
	
	public void index() {
		render("aliPay/index.vm");
	}

	
	
	public void confirmOrder() throws IOException {
		System.out.println("确认订单，支付跳转>>>");
		Result result = Result.success();
		result.set("url", "/aliPay/goPay");
		renderJson(result);
	}
	
	/**前往支付页面*/
	public void goPay() throws UnsupportedEncodingException {
				//初始化一个AlipayClient
				AlipayClient alipayClient = new DefaultAlipayClient(AliPayConfig.ALIPAY_URL, AliPayConfig.APPID, AliPayConfig.APP_PRIVATE_KEY, AliPayConfig.FORMAT, AliPayConfig.CHARSET, AliPayConfig.ALIPAY_PUBLIC_KEY, AliPayConfig.SIGN_TYPE);
				
				//设置请求参数
				AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
				alipayRequest.setReturnUrl(AliPayConfig.RETURN_URL);
				alipayRequest.setNotifyUrl(AliPayConfig.NOTIFY_URL);
				
				//商户订单号，商户网站订单系统中唯一订单号，必填
				String out_trade_no = get("out_trade_no");
				//付款金额，必填
				String total_amount = get("total_amount");
				//订单名称，必填
				String subject = get("subject");
				//商品描述，可空
				String body = get("body");
				
				alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
						+ "\"total_amount\":\""+ total_amount +"\"," 
						+ "\"subject\":\""+ subject +"\"," 
						+ "\"body\":\""+ body +"\"," 
						+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
				
				String form = "";
				
				//请求
				try {
					AlipayTradePagePayResponse response = alipayClient.pageExecute(alipayRequest);
					if(response.isSuccess()){
						System.out.println("通道调用成功");
						//调用支付宝，生成表单
							//第一次请求，生成一个系统订单
							TraderOrder order = new TraderOrder();
							//商户id， 暂时默认为 3， 支付渠道默认为 支付宝
							order.set("tenant_org_id", 3).set("out_trade_no", out_trade_no).set("initiator_user_id", 3)
							.set("real_name", "admin").set("buyer_id", getAdminId()).set("buyer_real_name", getSessionAdmin().get("real_name"))
							.set("order_name", subject).set("total_amount", total_amount).set("trader_type", EnumConstant.TraderType.BUYGOODS.getValue())
							.set("fund_flow", EnumConstant.InAndOut.EXPEND.getValue()).set("order_time", new Date()).set("pay_source", EnumConstant.PaySource.AliPay.getValue())
							.set("pay_status", EnumConstant.PayStatus.WAIT.getValue());
							
							Result result = orderService.save(order);
							if (result.isSuccess()) {
								StringBuffer orderStr = new StringBuffer();
								orderStr.append("【新的订单(未支付)：】订单号(").append(out_trade_no+")").append("订单名称(").append(subject+")")
								.append("交易金额(").append(total_amount+")").append("订单描述(").append(body+")");
								Kv kv = Kv.by("content", orderStr);
								noticeService.save(kv);
							}else {
								renderHtml(result.getMessage());
								return;
							}
						
						form = alipayClient.pageExecute(alipayRequest).getBody();
						
					} else {
						System.out.println("调用失败");
					}
					
				} catch (AlipayApiException e) {
					renderJson(e.getErrMsg());
				}
				
		renderHtml(form);
		return;
	}
	
	
	/**支付宝-查询订单*/
	public void query() {
		
		Result result = Result.success();
		
		AlipayClient alipayClient = new DefaultAlipayClient(AliPayConfig.ALIPAY_URL, AliPayConfig.APPID, AliPayConfig.APP_PRIVATE_KEY, AliPayConfig.FORMAT, AliPayConfig.CHARSET, AliPayConfig.ALIPAY_PUBLIC_KEY, AliPayConfig.SIGN_TYPE);
		
		//设置请求参数
		AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
		
		//商户订单号，商户网站订单系统中唯一订单号
		String out_trade_no = get("out_trade_no");
		//支付宝交易号
		String trade_no = get("trade_no");
		//请二选一设置
		alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","+"\"trade_no\":\""+ trade_no +"\"}");
		
		
		String queryRes = "";
		//请求
		try {
			AlipayTradeQueryResponse response = alipayClient.execute(alipayRequest);
			
			if (response.isSuccess()) {
				System.out.println("【支付宝-订单查询-调用成功】>>>" + response);
				queryRes = alipayClient.execute(alipayRequest).getBody();
			}else {
				System.out.println("【支付宝-订单查询-调用失败】>>>" + response);
			}
			
			result.set("res", queryRes);
			renderJson(result);
		} catch (AlipayApiException e) {
			renderJson(Result.failure(e.getErrMsg()));
		}
		
	}
	
	
	/**支付--同步回调*/
	@Clear
	public void return_url() throws Exception {
		System.out.println("【同步回调,支付成功】>>>进入验证");
		HttpServletRequest request = getRequest();
		
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		
		boolean signVerified = AlipaySignature.rsaCheckV1(params, AliPayConfig.ALIPAY_PUBLIC_KEY, AliPayConfig.CHARSET, AliPayConfig.SIGN_TYPE); //调用SDK验证签名

		if(signVerified) {
			System.out.println("【验证成功】");
			//商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		
			//支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
		
			//付款金额
			String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");
			
			set("message", "订单支付成功！").set("out_trade_no", out_trade_no).set("total_amount", total_amount)
			.set("trade_no", trade_no);
			render("aliPay/payResult.vm");
		}else {
			renderJson("验签失败");
		}
		
	}
	
	/**支付--异步通知*/
	@Clear
	public void notify_url() throws Exception {
		System.out.println("【支付宝异步通知】");
		HttpServletRequest request = getRequest();
		
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
			params.put(name, valueStr);
		}
		
		//调用SDK验证签名
		boolean signVerified = AlipaySignature.rsaCheckV1(params, AliPayConfig.ALIPAY_PUBLIC_KEY, AliPayConfig.CHARSET, AliPayConfig.SIGN_TYPE);

		if(signVerified) {//验证成功
			//商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
			//支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
			//交易状态
			String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
			//付款金额
			String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");
			//app_id
			String app_id = new String(request.getParameter("app_id").getBytes("ISO-8859-1"),"UTF-8");
			//交易付款时间
			String gmt_payment = new String(request.getParameter("gmt_payment").getBytes("ISO-8859-1"),"UTF-8");
			//与系统数据对比，验证数据是否正确
			/**
			 * 订单号，订单金额，交易商户等是否正确
			 */
			
			if (!AliPayConfig.APPID.equals(app_id)) {
				renderJson("failure");
			}
			
			TraderOrder traderOrder = TraderOrder.dao.findFirst("select * from trader_order where out_trade_no = ? and total_amount = ?", out_trade_no, total_amount);
			
			if (null == traderOrder) {
				renderJson("failure");
			}
			
			if(trade_status.equals("TRADE_FINISHED")){
				//不支持退款功能，或已超过可退款时间，触发此通知
				
				System.out.println("【交易完成】");
			}else if (trade_status.equals("TRADE_SUCCESS")){
				//支持退款功能的前提下，买家付款成功
				System.out.println("【卖家付款成功】");
				//修改订单状态
				traderOrder.set("pay_status", EnumConstant.PayStatus.PAYED.getValue()).set("pay_time", gmt_payment);
				Result result = orderService.update(traderOrder);
				if (result.isSuccess()) {
					StringBuffer order = new StringBuffer();
					order.append("【通知：订单支付成功：】订单号(").append(out_trade_no+")").append("支付宝交易号(").append(trade_no+")");
					Kv kv = Kv.by("content", order);
					noticeService.save(kv);
					renderJson("success");
				}else {
					System.out.println("【系统更新订单失败】");
					renderJson("failure");
				}
				
			}
			System.out.println("异步：success");
		}else {//验证失败
			System.out.println("异步：failure");
			renderJson("failure");
			
		}
		
	}
	
	
	
	/**退款
	 * @throws AlipayApiException */
	public void refund() throws Exception {
		AlipayClient alipayClient = new DefaultAlipayClient(AliPayConfig.ALIPAY_URL, AliPayConfig.APPID, AliPayConfig.APP_PRIVATE_KEY, AliPayConfig.FORMAT, AliPayConfig.CHARSET, AliPayConfig.ALIPAY_PUBLIC_KEY, AliPayConfig.SIGN_TYPE);		
		//设置请求参数
		AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
		
		//商户订单号，商户网站订单系统中唯一订单号
		String out_trade_no = new String(get("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//支付宝交易号
		String trade_no = new String(get("trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//请二选一设置
		//需要退款的金额，该金额不能大于订单金额，必填
		String refund_amount = new String(get("refund_amount").getBytes("ISO-8859-1"),"UTF-8");
		//退款的原因说明
		String refund_reason = new String(get("refund_reason").getBytes("ISO-8859-1"),"UTF-8");
		//标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
		String out_request_no = new String(get("out_request_no").getBytes("ISO-8859-1"),"UTF-8");
		
		alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
				+ "\"trade_no\":\""+ trade_no +"\"," 
				+ "\"refund_amount\":\""+ refund_amount +"\"," 
				+ "\"refund_reason\":\""+ refund_reason +"\"," 
				+ "\"out_request_no\":\""+ out_request_no +"\"}");
		
		//请求
		String refundRes = "";
		
		AlipayTradeRefundResponse refundResponse = alipayClient.execute(alipayRequest);
		
		if (refundResponse.isSuccess()) {
			refundRes = alipayClient.execute(alipayRequest).getBody();
			renderJson(Result.success().set("res", refundRes));
		}else {
			renderJson(Result.failure());
		}
		
	}
	
	/**退款查询*/
	public void queryRefund() throws Exception {
		AlipayClient alipayClient = new DefaultAlipayClient(AliPayConfig.ALIPAY_URL, AliPayConfig.APPID, AliPayConfig.APP_PRIVATE_KEY, AliPayConfig.FORMAT, AliPayConfig.CHARSET, AliPayConfig.ALIPAY_PUBLIC_KEY, AliPayConfig.SIGN_TYPE);		
		
		//设置请求参数
		AlipayTradeFastpayRefundQueryRequest alipayRequest = new AlipayTradeFastpayRefundQueryRequest();
		
		//商户订单号，商户网站订单系统中唯一订单号
		String out_trade_no = new String(get("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//支付宝交易号
		String trade_no = new String(get("trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//请二选一设置
		//请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的外部交易号，必填
		String out_request_no = new String(get("out_request_no").getBytes("ISO-8859-1"),"UTF-8");
		
		alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
				+"\"trade_no\":\""+ trade_no +"\","
				+"\"out_request_no\":\""+ out_request_no +"\"}");
		
		//请求
		String queryRefundRes = alipayClient.execute(alipayRequest).getBody();
		
		Result request = Result.success();
		request.set("res", queryRefundRes);
		renderJson(request);
	}
	
	/**交易关闭*/
	public void close() throws Exception {
		//获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(AliPayConfig.ALIPAY_URL, AliPayConfig.APPID, AliPayConfig.APP_PRIVATE_KEY, AliPayConfig.FORMAT, AliPayConfig.CHARSET, AliPayConfig.ALIPAY_PUBLIC_KEY, AliPayConfig.SIGN_TYPE);		
		
		//设置请求参数
		AlipayTradeCloseRequest alipayRequest = new AlipayTradeCloseRequest();
		//商户订单号，商户网站订单系统中唯一订单号
		String out_trade_no = new String(get("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//支付宝交易号
		String trade_no = new String(get("trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//请二选一设置
		
		alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," +"\"trade_no\":\""+ trade_no +"\"}");
		
		//请求
		
		AlipayTradeCloseResponse response = alipayClient.execute(alipayRequest);
		
		if (response.isSuccess()) {
			String closeRes = alipayClient.execute(alipayRequest).getBody();
			Result result = Result.success();
			result.set("res", closeRes);
			renderJson(result);
		}else {
			System.out.println("【交易关闭失败】>>>" + response);
			renderJson(Result.failure().set("res", response.getBody()));
		}
		
	}
	
	/**
	 * 订单管理--去付款
	 * @throws AlipayApiException */
	public void conPay() throws AlipayApiException {
		//初始化一个AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(AliPayConfig.ALIPAY_URL, AliPayConfig.APPID, AliPayConfig.APP_PRIVATE_KEY, AliPayConfig.FORMAT, AliPayConfig.CHARSET, AliPayConfig.ALIPAY_PUBLIC_KEY, AliPayConfig.SIGN_TYPE);
		
		//设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(AliPayConfig.RETURN_URL);
		alipayRequest.setNotifyUrl(AliPayConfig.NOTIFY_URL);
		
		//商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = get("out_trade_no");
		//付款金额，必填
		String total_amount = get("total_amount");
		//订单名称，必填
		String subject = get("subject");
		//商品描述，可空
		String body = get("body");
		
		alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
				+ "\"total_amount\":\""+ total_amount +"\"," 
				+ "\"subject\":\""+ subject +"\"," 
				+ "\"body\":\""+ body +"\"," 
				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		
		String form = "";
		form = alipayClient.pageExecute(alipayRequest).getBody();
		renderHtml(form);
	}
	
	
}
