package com.webserver.core;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.webserver.core.http.HttpRequest;
import com.webserver.core.http.HttpResponse;
import com.webserver.servlets.HttpServlet;
import com.webserver.servlets.LoginServlet;
import com.webserver.servlets.RegServlet;
import com.webserver.servlets.UpdateServlet;

/**
 * 处理客户端请求
 * 
 * @author lenovo
 *
 */
public class ClientHandler implements Runnable {
	private Socket socket;

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			/*
			 * 主流程： 1.解析请求 2.处理请求 3.发送响应
			 */

			// 1准备工作
			// 1.1解析请求，创建请求对象
			HttpRequest request = new HttpRequest(socket);
			// 1.2创建响应对象
			HttpResponse response = new HttpResponse(socket);

			// 2.处理请求
			// 2.1:获取请求的资源路径
			String url = request.getRequestURI();
			
			//判断该请求是否为请求业务  (获取ServletContext中servletMapping请求对应的servlet类名)
			String servletName = ServerContext.servletMapping.get(url);
			if(servletName!=null){ //查找到对应的Servlet类名才执行，没找到就说明没有这个Servlet,执行其他操作
				Class cls = Class.forName(servletName);
				HttpServlet servlet = (HttpServlet)cls.newInstance();
				servlet.service(request, response);
			}else {
				// 2.2:根据资源路径去webapps目录中寻找该资源
						//资源存在  返回
						//资源不存在 返回一个404状态码和一个404页面
				File file = new File("webapps" + url);
				if (file.exists()) {
					// 向响应对象中设置要响应的资源内容
					response.setEntity(file);
				} else {
					// 设置状态代码404
					response.setStatusCode(404);
					// 设置404页面
					response.setEntity(new File("webapps/root/404.html"));
				}
			}
			
			// 3响应客户端
			response.flush();

		} catch (EmptyRequestException e) {
			/**
			 * 该实例化HttpRequest时若发现是空请求时，该构造方法会将异常抛出，
			 * 这里不做任何处理，直接在finally中与客户端断开即可
			 */
			System.out.println("空请求！");
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 与客户端断开连接
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
