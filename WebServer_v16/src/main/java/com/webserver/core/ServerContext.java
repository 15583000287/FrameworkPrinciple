package com.webserver.core;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 服务端相关配置信息
 * 
 * @author lenovo
 *
 */
public class ServerContext {
	/*
	 * Servlet映射关系
	 *  key:请求路径 
	 *  value:Servlet的名字
	 */
	public static Map<String, String> servletMapping = new HashMap<String, String>();

	static {
		initServletMapping();
	}
	
	/**
	 * 初始化Servlet映射
	 */
	private static void initServletMapping(){
		//解析conf/servlets.xml
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read("conf/servlets.xml");
			Element root = document.getRootElement();
			List<Element> servlets = root.elements();
			//遍历
			for(Element ele: servlets){
				String url = ele.attributeValue("url");
				String className = ele.attributeValue("className");
				servletMapping.put(url, className);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据请求路径获取对应的Servlet名字
	 * @param url
	 * @return
	 */
	public static String getServletName(String url){
		return servletMapping.get(url);
	}
}
