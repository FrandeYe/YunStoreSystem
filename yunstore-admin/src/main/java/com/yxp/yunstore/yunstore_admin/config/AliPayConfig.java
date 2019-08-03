package com.yxp.yunstore.yunstore_admin.config;

/**
 * 
* @ClassName: AliPayConfig  
* @Description: 支付宝-电脑网站支付
* 参数配置
* @author Administrator  
* @date 2019年7月30日  
*
 */
public class AliPayConfig {

	/**
	 * 支付宝网关（固定）
	 * 沙箱测试：https://openapi.alipaydev.com/gateway.do
	 * 正式：
	 */
	public static final String ALIPAY_URL = "https://openapi.alipaydev.com/gateway.do"; 
	
	/**应用APPID*/
	public static String APPID;
	
	/**应用私钥*/
	public static String APP_PRIVATE_KEY;
	
	/**支付宝公钥*/
	public static String ALIPAY_PUBLIC_KEY;
	
	/**参数返回，只支持 json*/
	public static final String FORMAT = "json";
	
	/**编码集，支持 GBK/UTF-8*/
	public static String CHARSET;
	
	/**商户生成签名字符串所使用的签名算法类型，目前支持 RSA2 和 RSA，推荐使用 RSA2*/
	public static String SIGN_TYPE;
	
	/**用户确认支付后，支付宝通过 get 请求 returnUrl（商户入参传入），返回同步返回参数*/
	public static String RETURN_URL;
	
	
	/**交易成功后，支付宝通过 post 请求 notifyUrl（商户入参传入），返回异步通知参数*/
	public static String NOTIFY_URL;
	
	
	
}
