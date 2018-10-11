package cc.vant.tinyspring.webmvc;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;


/**
 * 实现此接口的类会在容器初始化时被调用onStartup
 */
public interface WebApplicationInitializer {
	void onStartup(ServletContext servletContext) throws ServletException;
}