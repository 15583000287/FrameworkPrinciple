package com.webserver.servlets;

import java.io.File;

import com.webserver.core.http.HttpRequest;
import com.webserver.core.http.HttpResponse;

/**
 * 所有Servlet的超类
 * 
 * @author lenovo
 *
 */
public abstract class HttpServlet {
	public abstract void service(HttpRequest request, HttpResponse response);
	
	/**
	 * 跳转到指定路径
	 * 注：TOMCAT中实际该方法属于转发器，可以通过request获取。
	 * @param path
	 * @param request
	 * @param response
	 */
	public void forward(String path,HttpRequest request,HttpResponse response) {
		response.setEntity(new File("webapps"+path));
	}
}
