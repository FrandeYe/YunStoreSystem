package com.yxp.yunstore.yunstore_admin.config;

import com.alibaba.druid.filter.stat.StatFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.yxp.yunstore.yunstore_admin.web.handler.SysResourceHandler;
import com.yxp.yunstore.yunstore_admin.web.interceptor.BaseInterceptor;
import com.yxp.yunstore.yunstore_admin.web.route.BaseRoutes;
import com.yxp.yunstore.yunstore_admin.web.route.MaterialRoutes;
import com.yxp.yunstore.yunstore_admin.web.route.NoteRoutes;
import com.yxp.yunstore.yunstore_admin.web.route.SystemRoutes;
import com.yxp.yunstore.yunstore_admin.web.route.TraderRoutes;
import com.yxp.yunstore_common.model._MappingKit;

public class SysConfig extends JFinalConfig {
	/**
	 * 将全局配置提出来 方便其他地方重用
	 */
	private static Prop p;
	//private WallFilter wallFilter;
	/**静态文件访问网址*/
	public static String resourcePath;
	/**上传文件保存路径*/
	public static String uploadPath;
	/**上传文件访问路径*/
	public static String uploadPathDomain;
	/**视图访问路径*/
	public static String viewPath;
	/**上传文件大小限制*/
	public static int maxPostSize;
	
	/**支付宝配置对象*/
	private static Prop aliPay;
	
	
	/**
	 * 配置JFinal常量
	 */
	@Override
	public void configConstant(Constants me) {
		//读取数据库配置文件
		loadConfig();
		
		resourcePath = p.get("resourcePath");
		uploadPath = p.get("uploadPath");
		uploadPathDomain = p.get("uploadPathDomain");
		viewPath = p.get("viewPath");
		maxPostSize = p.getInt("maxPostSize");
		
		/**设置当前是否为开发模式*/
		me.setDevMode(p.getBoolean("devMode"));
		/**设置默认上传文件保存路径 getFile等使用*/
		me.setBaseUploadPath(uploadPath);
		
		/**设置默认视图类型*/
		me.setViewType(ViewType.JFINAL_TEMPLATE);
		me.setViewExtension("*.vm");
		
		/**设置404渲染视图*/
		me.setError404View(viewPath + "common/404.vm");
		
		/**设置上传文件最大限制*/
		me.setMaxPostSize(maxPostSize);
		
		//设置启用依赖注入
		me.setInjectDependency(true);
	}
	
	
	/**
	 * 配置项目路由
	 * 路由拆分到 FrontRutes 与 AdminRoutes 之中配置的好处：
	 * 1：可分别配置不同的 baseViewPath 与 Interceptor
	 * 2：避免多人协同开发时，频繁修改此文件带来的版本冲突
	 * 3：避免本文件中内容过多，拆分后可读性增强
	 * 4：便于分模块管理路由
	 */
	@Override
	public void configRoute(Routes me) {
		me.add(new SystemRoutes());
		me.add(new BaseRoutes());
		me.add(new NoteRoutes());
		me.add(new MaterialRoutes());
		me.add(new TraderRoutes());
	}
	
	
	/**先加载开发环境配置，再追加生产环境的少量配置覆盖掉开发环境配置*/
	static void loadConfig() {
		if (p == null) {
			p = PropKit.use("config.properties").appendIfExists("config-pro.properties");
		}
		
		if (aliPay == null) {
			aliPay = PropKit.use("alipay.properties");
			AliPayConfig.ALIPAY_PUBLIC_KEY = aliPay.get("ALIPAY_PUBLIC_KEY");
			AliPayConfig.APP_PRIVATE_KEY = aliPay.get("APP_PRIVATE_KEY");
			AliPayConfig.APPID = aliPay.get("APPID");
			AliPayConfig.CHARSET = aliPay.get("CHARSET");
			AliPayConfig.NOTIFY_URL = aliPay.get("NOTIFY_URL");
			AliPayConfig.RETURN_URL = aliPay.get("RETURN_URL");
			AliPayConfig.SIGN_TYPE = aliPay.get("SIGN_TYPE");
		}
		
		
	}
	
	
	/**
	 * 获取数据库插件
	 * 抽取成独立的方法，便于重用该方法，减少代码冗余
	 */
	public static DruidPlugin getDruidPlugin() {
		loadConfig();
		return new DruidPlugin(p.get("jdbcUrl"), p.get("user"), p.get("password"));
	}
	
	
	/**
	 * 配置JFinal插件
	 * 数据库连接池
	 * ActiveRecordPlugin
	 * 缓存
	 * 定时任务
	 * 自定义插件
	 */
	@Override
	public void configPlugin(Plugins me) {
		loadConfig();
		
		/**配置数据库连接池插件*/
		DruidPlugin dbPlugin=getDruidPlugin();
		
		/**加强数据库安全*/
		//wallFilter = new WallFilter();
		//wallFilter.setDbType("mysql");
		//dbPlugin.addFilter(wallFilter);
		
		/**添加 StatFilter 才会有统计数据*/
		dbPlugin.addFilter(new StatFilter());
		
		/**数据映射 配置ActiveRecord插件*/
		ActiveRecordPlugin arp=new ActiveRecordPlugin(dbPlugin);
		arp.setShowSql(p.getBoolean("devMode"));
		arp.setDialect(new MysqlDialect());
		dbPlugin.setDriverClass("com.mysql.jdbc.Driver");
		/********在此添加数据库 表-Model 映射*********/
		
		/**如果使用了JFinal Model 生成器 生成了BaseModel 把下面注释解开即可*/
		_MappingKit.mapping(arp);
		
		/**添加到插件列表中*/
		me.add(dbPlugin);
		me.add(arp);
		
	}
	
	
	/**
	 * 配置全局拦截器
	 * 只会拦截action，不拦截静态资源（html, css, js等）
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		me.add(new BaseInterceptor());
	}
	
	
	/**
	 * 配置全局处理器
	 * 拦截所有请求
	 */
	@Override
	public void configHandler(Handlers me) {
		//说明：druid的统计页面涉及安全性 需要自行处理根据登录权限判断是否能访问统计页面 
		//me.add(DruidKit.getDruidStatViewHandler()); // druid 统计页面功能
		//me.add(new ContextPathHandler("BASE_PATH"));
		me.add(new SysResourceHandler());
		me.add(new UrlSkipHandler("/assets/", false));
	}
	
	
	/**
	 * 项目启动后调用
	 */
	@Override
	public void onStart() {
		// 让 druid 允许在 sql 中使用 union
		//wallFilter.getConfig().setSelectUnionCheck(false);
	}
	
	/**
	 * 配置模板引擎 
	 */
	@Override
	public void configEngine(Engine me) {
		/**配置模板支持热加载*/
		me.setDevMode(p.getBoolean("engineDevMode", false));
		
		me.addSharedFunction(viewPath + "common/header.vm");
	}
	

}