package demo;

import base.annotation.RequestAnnotation;

public class HelloController {
	/*
	 * 处理业务逻辑
	 * 方法前加@RequesetMapping注解
	 * 返回值 视图名
	 */
	@RequestAnnotation("/hello.do")
	public String hello() {
		/*
		 * 返回视图名
		 * DispatcherServlet会按照
		 * WEB-INF + 视图名 +  .jsp 生成jsp地址
		 */
		return "hello";
	}
	
	@RequestAnnotation("/toLogin.do")
	public String toLogin() {
		return "login";
	}
	
	@RequestAnnotation("/login.do")
	public String login() {
		return "redirect:toWelcome.do";
	}
	
	@RequestAnnotation("/toWelcome.do")
	public String toWelcome() {
		return "welcome";
	}
}
