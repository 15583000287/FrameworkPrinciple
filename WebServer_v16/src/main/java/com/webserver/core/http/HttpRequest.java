package com.webserver.core.http;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Handler;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.webserver.core.EmptyRequestException;

/**
 * 请求对象 每个实例表示客户端发送过来的一个具体请求
 * 
 * @author lenovo
 *
 */
public class HttpRequest {
	/*
	 * 请求行相关信息定义
	 */
	// 请求方式
	private String method;
	// 资源路径
	private String url;
	// 协议版本
	private String protocol;

	// url中的请求部分
	private String requestURI;
	// url中的参数部分
	private String queryString;
	// 每个参数
	private Map<String, String> parameters = new HashMap<String, String>();

	/*
	 * 客户端连接相关信息定义
	 */
	private Socket socket;
	private InputStream in;

	/*
	 * 消息头相关信息定义
	 */
	private Map<String, String> headers = new HashMap<String, String>();

	/*
	 * 消息正文相关信息定义
	 */

	/**
	 * 初始化请求
	 * 
	 * @throws EmptyRequestException
	 */
	public HttpRequest(Socket socket) throws EmptyRequestException {
		try {
			this.socket = socket;
			this.in = socket.getInputStream();
			/*
			 * 解析请求： 1:解析请求行 2:解析消息头 3:解析消息正文
			 */
			parseRequestLine();
			parseHeaders();
			parseContent();
		} catch (EmptyRequestException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析请求行
	 * 
	 * @throws EmptyRequestException
	 */
	private void parseRequestLine() throws EmptyRequestException {
		System.out.println("开始解析请求行...");
		try {
			String line = readLine();
			System.out.println("请求行：" + line);
			/*
			 * 将请求行进行拆分，将每部分内容对应的设置到属性上
			 * 
			 * 一下在后期运行过程中可能会出现数组下标越界的情况。
			 * 这时由于HTTP协议允许客户端发送一个空请求过来，而这时通过空格拆分后得到不三项内容。后期解决
			 */
			String[] requestLine = line.split("\\s");// 空格 直接" "
			// 如果是空请求则抛出异常 不执行解析工作了
			if (requestLine.length != 3) {
				// 自定义空请求异常
				throw new EmptyRequestException();
			}
			method = requestLine[0];
			url = requestLine[1];
			// 进一步解析URL
			parseURL();
			protocol = requestLine[2];
			// 打桩
			System.out.println("method:" + method);
			System.out.println("url:" + url);
			System.out.println("protocol:" + protocol);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("请求行解析完毕！");
	}

	/**
	 * 进一步解析URL rul可能会有两种格式：带参数和不带参数 1.不带参数：/myweb/index.html
	 * 2.带参数：/myweb/reg?username=zhangsan&password=123
	 */
	private void parseURL() {
		/*
		 * 首先判断当前url是否含有参数，判断的依据是看url是否含有"?",
		 * 含有则认为这个url是包含参数的，否则直接将url赋值requestURI
		 * ...
		 * 解析过程中要注意url的几个特别情况： 
		 * 1.url可能含有"?"但是没有参数部分 如：/myweb/reg?
		 * 2.参数部分有可能只有参数名没有参数值 如：/myweb/reg?username=&password=&age=&nickname=
		 */
		
		//判断是否含参数部分
		if (url.contains("?")) {
			// 按照？拆分
			String[] data = url.split("\\?");
			requestURI = data[0];
			// 判断？后面是否含有参数
			if (data.length > 1) {
				queryString = data[1];
				//进一步解析参数部分(提取成方法，解析POST请求的消息正文时重用)
				parseParameter(queryString);
			}
		} else {
			// 不含参数
			this.requestURI = this.url;
		}
		// 打桩
		System.out.println("requestURI:" + requestURI);
		System.out.println("queryString:" + queryString);
		System.out.println("parameters:" + parameters);
		System.out.println("------------------------------");
	}

	/**
	 * 解析消息头
	 */
	private void parseHeaders() {
		System.out.println("开始解析消息头...");
		try {
			/*
			 * 解析消息头流程： 循环调用readLine方法，读取每一个消息头
			 * 当readLine方法返回值为空字符串时停止循环(因为返回空字符串说明单独读取了CRLF 而这是这是作为消息头结束的标志)
			 * 在读取到每个消息头后,根据“: ”(冒号空格)进行拆分，
			 * 并将消息头的名字作为key，消息头对应的值作为value保存到属性headers这是Map中完成解析工作
			 */
			String line = null;
			String[] lines = {};
			while (!"".equals(line = readLine())) {
				lines = line.split(":\\s");
				headers.put(lines[0], lines[1]);
				// 打桩
				// System.out.println(lines[0] + "---" + lines[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("请求头部分："+headers);
		System.out.println("消息头解析完毕！");
	}

	/**
	 * 解析消息正文
	 */
	private void parseContent() {
		System.out.println("开始解析消息正文...");
		/*
		 * 根据消息头是否含有Content-Length决定该请求是否含有消息正文
		 */
		try {
			if(headers.containsKey("Content-Length")){
				//含有消息正文的长度
				int length = Integer.parseInt(headers.get("Content-Length")); //将字符串解析为int基本类型
				//读取消息正文内容
				byte[] data = new byte[length];
				in.read(data);
				/*
				 * 根据消息头Content-Type判断该消息正文的数据类型
				 */
				String contentType = headers.get("Content-Type");
				//判断是否为form表单提交数据
				if("application/x-www-form-urlencoded".equals(contentType)){
					/*
					 * 该正文内容相当于原GET请求地址栏里url "?" 右侧的内容(参数部分)
					 */
					String line = new String(data,"ISO8859-1");
					System.out.println("正文内容："+line);
					//解析消息正文内容
					parseParameter(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("消息正文解析完毕！");
	}
	/**
	 * 解析参数
	 * 格式：name=value&name=value&...
	 * @param line
	 */
	private void parseParameter(String line){
		/*
		 *现将参数中的"%xx"的内容按照对应字符集(浏览器通常用UTF-8)还原为对应文字 
		 */
		try {
			/*
			 * URLDecoder的decode方法可以将给定的字符串中的"%xx"内容转为对应2进制字节
			 * 然后按照给定的字符集将这些字节还原为对应字符替换这些"%XX"部分，然后将换好的字符串返回
			 */
			System.out.println("对参数转码前："+line);
			line = URLDecoder.decode(line, "GBK");
			System.out.println("对参数转码后："+line);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 按照 & 进一步解析参数部分
		String[] paraArr = line.split("\\&");
		// 遍历每个参数进 按照=行拆分
		for (String paras : paraArr) {
			String[] para = paras.split("\\=");
			if (para.length > 1) {
				parameters.put(para[0], para[1]);
			} else {
				parameters.put(para[0], ""); //不能为null 文件读取出错
			}
		}
	}

	/**
	 * 读取一行字符串，当连续读取CR,LF时停止，并将之前的内容以一行字符串形式返回。
	 * 
	 * @return
	 * @throws IOException
	 */
	private String readLine() throws IOException {
		StringBuilder builder = new StringBuilder();
		// 本次读取的字节
		int d = -1;
		// c1表示上次读取的字符，c2表示本次读取的字符
		char c1 = 'a', c2 = 'a';
		while ((d = in.read()) != -1) {
			c2 = (char) d;
			if (c1 == HttpContext.CR && c2 == HttpContext.LF) {
				break;
			}
			builder.append(c2);
			c1 = c2;
		}
		return builder.toString().trim();
	}

	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}

	public String getProtoco() {
		return protocol;
	}

	/**
	 * 根据给定的消息头的名字获取对应消息头的值
	 * 
	 * @param name
	 *            消息头的名字
	 * @return
	 */
	public String getHeader(String name) {
		return headers.get("name");
	}

	public String getRequestURI() { // uri部分 不包含ip和端口的路径
		return requestURI;
	}

	public String getQueryString() { // 参数部分
		return queryString;
	}

	/**
	 * 根据给定的参数名称获取对应的参数值
	 * 
	 * @param name
	 * @return
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}
}
