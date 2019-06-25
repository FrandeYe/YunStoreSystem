package com.yxp.yunstore_common.plugin;

import java.io.File;
import java.util.List;

import javax.sql.DataSource;

import com.google.common.collect.Lists;
import com.jfinal.ext.kit.ClassSearcher;
import com.jfinal.ext.plugin.tablebind.INameStyle;
import com.jfinal.ext.plugin.tablebind.SimpleNameStyles;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.plugin.activerecord.Model;
import com.yxp.yunstore_common.utils.StringUtil;


public class TableBindPlugin extends ActiveRecordPlugin{

	protected final Log log = Log.getLog(getClass());
	
	 @SuppressWarnings("rawtypes")
	    private List<Class<? extends Model>> excludeClasses = Lists.newArrayList();
	    private List<String> includeJars = Lists.newArrayList();
	    private boolean autoScan = true;
	    private boolean includeAllJarsInLib = false;
	    private List<String> scanPackages = Lists.newArrayList();
	    private List<String> packageList = Lists.newArrayList(); 
	    private INameStyle nameStyle;
	    @SuppressWarnings("unused")
		private String classpath = PathKit.getRootClassPath();
	    @SuppressWarnings("unused")
		private String libDir = PathKit.getWebRootPath() + File.separator + "WEB-INF" + File.separator + "lib";
	    @SuppressWarnings("unused")
		private String configName;
	    public TableBindPlugin(IDataSourceProvider dataSourceProvider) {
	        this(DbKit.MAIN_CONFIG_NAME, dataSourceProvider, SimpleNameStyles.DEFAULT);
	    }

	    public TableBindPlugin(String configName, IDataSourceProvider dataSourceProvider) {
	        this(configName, dataSourceProvider, SimpleNameStyles.DEFAULT);
	    }

	    public TableBindPlugin(IDataSourceProvider dataSourceProvider, int transactionLevel) {
	        this(DbKit.MAIN_CONFIG_NAME, dataSourceProvider, transactionLevel, SimpleNameStyles.DEFAULT);
	    }

	    public TableBindPlugin(String configName, IDataSourceProvider dataSourceProvider, int transactionLevel) {
	        this(configName, dataSourceProvider, transactionLevel, SimpleNameStyles.DEFAULT);
	    }

	    public TableBindPlugin(IDataSourceProvider dataSourceProvider, INameStyle nameStyle) {
	        super(DbKit.MAIN_CONFIG_NAME, dataSourceProvider);
	        this.nameStyle = nameStyle;
	        this.configName = DbKit.MAIN_CONFIG_NAME;
	    }

	    public TableBindPlugin(String configName, IDataSourceProvider dataSourceProvider, INameStyle nameStyle) {
	        super(configName, dataSourceProvider);
	        this.nameStyle = nameStyle;
	        this.configName = configName;
	    }

	    public TableBindPlugin(IDataSourceProvider dataSourceProvider, int transactionLevel, INameStyle nameStyle) {
	        super(DbKit.MAIN_CONFIG_NAME, dataSourceProvider, transactionLevel);
	        this.nameStyle = nameStyle;
	        this.configName = DbKit.MAIN_CONFIG_NAME;
	    }

	    public TableBindPlugin(String configName, IDataSourceProvider dataSourceProvider, int transactionLevel, INameStyle nameStyle) {
	        super(configName, dataSourceProvider, transactionLevel);
	        this.nameStyle = nameStyle;
	        this.configName = configName;
	    }

	    public TableBindPlugin(DataSource dataSource) {
	        this(DbKit.MAIN_CONFIG_NAME, dataSource, SimpleNameStyles.DEFAULT);
	    }

	    public TableBindPlugin(String configName, DataSource dataSource) {
	        this(configName, dataSource, SimpleNameStyles.DEFAULT);
	    }

	    public TableBindPlugin(DataSource dataSource, int transactionLevel) {
	        this(DbKit.MAIN_CONFIG_NAME, dataSource, transactionLevel, SimpleNameStyles.DEFAULT);
	    }

	    public TableBindPlugin(String configName, DataSource dataSource, int transactionLevel) {
	        this(configName, dataSource, transactionLevel, SimpleNameStyles.DEFAULT);
	    }

	    public TableBindPlugin(DataSource dataSource, INameStyle nameStyle) {
	        super(DbKit.MAIN_CONFIG_NAME, dataSource);
	        this.nameStyle = nameStyle;
	        this.configName = DbKit.MAIN_CONFIG_NAME;
	    }

	    public TableBindPlugin(String configName, DataSource dataSource, INameStyle nameStyle) {
	        super(configName, dataSource);
	        this.nameStyle = nameStyle;
	        this.configName = configName;
	    }

	    public TableBindPlugin(DataSource dataSource, int transactionLevel, INameStyle nameStyle) {
	        super(DbKit.MAIN_CONFIG_NAME, dataSource, transactionLevel);
	        this.nameStyle = nameStyle;
	        this.configName = DbKit.MAIN_CONFIG_NAME;
	    }

	    public TableBindPlugin(String configName, DataSource dataSource, int transactionLevel, INameStyle nameStyle) {
	        super(configName, dataSource, transactionLevel);
	        this.nameStyle = nameStyle;
	        this.configName = configName;
	    }

	    /**
	     * 添加需要扫描的包，默认为扫描所有包
	     *
	     * @param packages
	     * @return
	     */
	    public TableBindPlugin addScanPackages(String... packages) {
	        for (String pkg : packages) {
	            scanPackages.add(pkg);
	        }
	        return this;
	    }

	    @SuppressWarnings({ "rawtypes", "unchecked" })
		public TableBindPlugin addExcludeClasses(Class<? extends Model>... clazzes) {
	        for (Class<? extends Model> clazz : clazzes) {
	            excludeClasses.add(clazz);
	        }
	        return this;
	    }

	    @SuppressWarnings("rawtypes")
	    public TableBindPlugin addExcludeClasses(List<Class<? extends Model>> clazzes) {
	        if (clazzes != null) {
	            excludeClasses.addAll(clazzes);
	        }
	        return this;
	    }

	    public TableBindPlugin addJars(List<String> jars) {
	        if (jars != null) {
	            includeJars.addAll(jars);
	        }
	        return this;
	    }

	    public TableBindPlugin addJars(String... jars) {
	        if (jars != null) {
	            for (String jar : jars) {
	                includeJars.add(jar);
	            }
	        }
	        return this;
	    }

	    @SuppressWarnings({"rawtypes"})
	    @Override
	    public boolean start() {
	    	List<Class<? extends Model>> modelClasses = ClassSearcher.of(Model.class).injars(includeJars).includeAllJarsInLib(includeAllJarsInLib).search();
	        TableBind tb = null;
	        for (Class modelClass : modelClasses) {
	            //指定了扫描包的情况下根据包名来进行加载映射关系,否则就全部加载
	            if(!packageList.isEmpty()){
	                for (String pack : packageList) {
	                   if (modelClass.getName().startsWith(pack)) {
	                      scanModel(tb,modelClass);
	                   }
	                }
	            }
	        }
	        return super.start();
	    }

	    @SuppressWarnings({ "unchecked", "rawtypes"})
	    private void scanModel(TableBind tb,Class clazz){
		    if (!excludeClasses.contains(clazz)) {
		         tb = (TableBind) clazz.getAnnotation(TableBind.class);
		         String tableName;
		         if (tb == null) {
		             if (autoScan) {
		              tableName = nameStyle.name(clazz.getSimpleName());
		              this.addMapping(tableName, clazz);
		              log.debug("addMapping(" + tableName + ", " + clazz.getName() + ")");
		             }
		         } else {
		             tableName = tb.tableName();
		             if (StringUtil.isNotEmpty(tb.pkName())) {
		                 this.addMapping(tableName, tb.pkName(), clazz);
		                 log.debug("addMapping(" + tableName + ", " + tb.pkName() + "," + clazz.getName() + ")");
		             } else {
		                 this.addMapping(tableName, clazz);
		                 log.debug("addMapping(" + tableName + ", " + clazz.getName() + ")");
		             }
		         }
		     }
	    }
	    
	    @Override
	    public boolean stop() {
	        return super.stop();
	    }

	    public TableBindPlugin autoScan(boolean autoScan) {
	        this.autoScan = autoScan;
	        return this;
	    }

	    public TableBindPlugin classpath(String classpath) {
	        this.classpath = classpath;
	        return this;
	    }

	    public TableBindPlugin libDir(String libDir) {
	        this.libDir = libDir;
	        return this;
	    }
	    public TableBindPlugin includeAllJarsInLib(boolean includeAllJarsInLib) {
	        this.includeAllJarsInLib = includeAllJarsInLib;
	        return this;
	    }
	    
	    /**
	     * 包扫描
	     * @param packages
	     * @return
	     */
	    public TableBindPlugin scanPackages(String...packages){
	        if(packages != null){
	           for (String pack : packages) {
	              packageList.add(pack);
	           }
	        }
	        return this;
	     }

}
