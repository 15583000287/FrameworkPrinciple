package base.common;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Woodstox;

import base.annotation.RequestAnnotation;
/**
 * 映射处理器：
 * 		处理请求路径与处理器的对应关系
 * @author lenovo
 *
 */

public class HandlerMapping {
	/*
	 * 用来保存请求路径  Key
	 * 	   处理器对象，方法对象封装成Handler   value
	 */
	private Map<String, Handler> handlerMap = new HashMap<String, Handler>();
	
	//根据请求路径获取对应的handler
	public Handler getHandlerMap(String path) {
		return handlerMap.get(path);
	}

	public void process(List<Object> beans) {
		for(Object bean: beans) {
			Class clazz = bean.getClass();
			//获取所有方法
			Method[] methods = clazz.getDeclaredMethods();
			for(Method m: methods) {
				//获取指定的注解
				RequestAnnotation ra = m.getDeclaredAnnotation(RequestAnnotation.class);
				String path = ra.value();
				handlerMap.put(path, new Handler(m, bean));
			}
		}
		System.out.println("handlerMap 请求路径与对应hanlder(方法对象,处理器名)   "+handlerMap);
	}


	
	
}
