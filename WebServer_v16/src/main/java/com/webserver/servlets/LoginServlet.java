package com.webserver.servlets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

import com.webserver.core.http.HttpRequest;
import com.webserver.core.http.HttpResponse;

/**
 * 处理登陆业务
 * 
 * @author lenovo
 *
 */
public class LoginServlet extends HttpServlet{
	/*
	 * 登陆大致流程 
	 * 1.获取请求参数(username,passwrod) 
	 * 2.读取user.dat文件中的数据(username,password)
	 * 3. 1和2作比较 
	 * 		相同：响应客户端登陆成功页面 
	 * 		不同：响应客户端登陆失败页面
	 */
	public void service(HttpRequest request, HttpResponse response) {
		System.out.println("开始处理登陆业务！");
		// 获取请求参数
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		System.out.println("客户端:"+username+","+password);
		//验证登陆
		if(isLogin(username, password)){
			//response.setEntity(new File("webapps/myweb/login_sucess.html"));
			forward("/myweb/login_sucess.html", request, response); //webapps父类中已经拼接
		}else {
			forward("/myweb/login_fail.html", request, response);
		}
	}
	
	//判断登陆
	private boolean isLogin(String username,String password){
		// 读取user.dat中的文件数据	
		try (RandomAccessFile raf = new RandomAccessFile("user.dat", "r")) {
			//循环读取若干个100字节
			for(int i=0;i<raf.length()/100;i++){
				raf.seek(i*100);
				//读取用户名 先读32字节
				byte[] data = new byte[32];
				raf.read(data);
				String userData = new String(data,"utf-8").trim();
				//读取密码
				raf.read(data);
				String pwdDate = new String(data,"utf-8").trim();
				if(userData.equals(username) && pwdDate.equals(password)){
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
