package base.common;

import java.lang.reflect.Method;
/*
 *  保存方法对象  和 处理器对象类
 */
public class Handler {
	private Method method;
	private Object obj;
	
	public Handler(Method method, Object obj) {
		this.method = method;
		this.obj = obj;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	
}
