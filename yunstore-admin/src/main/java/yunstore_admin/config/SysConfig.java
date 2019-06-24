package yunstore_admin.config;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;

/**  
* <p>Title: SysConfig</p>  
* <p>Description: </p>  
* @author Y x p  
* @date 2019年6月24日  
*/
public class SysConfig extends JFinalConfig{

	public static void main(String[] args) {
		UndertowServer.start(SysConfig.class, 80, true);
	}
	
	@Override
	public void configConstant(Constants me) {
	}

	@Override
	public void configRoute(Routes me) {
	}

	@Override
	public void configEngine(Engine me) {
	}

	@Override
	public void configPlugin(Plugins me) {
	}

	@Override
	public void configInterceptor(Interceptors me) {
	}

	@Override
	public void configHandler(Handlers me) {
	}

}
