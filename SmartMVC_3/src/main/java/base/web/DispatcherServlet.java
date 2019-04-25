package base.web;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import base.common.Handler;
import base.common.HandlerMapping;


/**
 * 调度器
 * @author lenovo
 *
 */
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private HandlerMapping handlerMapping;
	
	@Override
	public void init() throws ServletException {
		//初始化参数
		String configFile = getServletConfig().getInitParameter("configFile");
		//读取配置处理类名 mvc.xml
		SAXReader saxReader = new SAXReader();
		InputStream in = getClass().getClassLoader().getResourceAsStream(configFile);
		try {
			Document document = saxReader.read(in);
			Element root = document.getRootElement();
			List<Element> elements = root.elements();
			List<Object> beans = new ArrayList<Object>();
			for(Element ele: elements) {
				String className = ele.attributeValue("class");
				//利用反射获取实例  放入lsit中
				Object bean = Class.forName(className).newInstance();
				beans.add(bean);
			}
			//创建HandlerMapping实例   service方法里千万不要创建了   出了一次错  注意啊
			handlerMapping = new HandlerMapping();
			handlerMapping.process(beans);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取请求路径尾部
		String path = request.getRequestURI().substring(request.getContextPath().length());
		System.out.println("请求path: "+path);
		//根据path在HandlerMap中查找对应的处理器对象
		Handler handler = handlerMapping.getHandlerMap(path);
		//利用反射调用处理器方法
		String viewName = null;
		try {
			viewName = handler.getMethod().invoke(handler.getObj()).toString();
			if(viewName.startsWith("redirect:")) {
				//重定向
				String redirectPath = request.getContextPath() + "/" 
						+ viewName.substring("redirect:".length());
				response.sendRedirect(redirectPath);
			}else {
				//转发  默认转发
				String jspPath = "/WEB-INF/" + viewName + ".jsp";
				request.getRequestDispatcher(jspPath).forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}
}
