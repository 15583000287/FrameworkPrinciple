package com.itmayiedu.tomcat;

import com.itmayiedu.servlet.IndexServlet;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

/**
 * Tomcat + Servlet
 * 使用Java语言创建Tomcat服务器
 * Tomcat底层执行程序 最终servlet
 * SpringMVC底层使用servlet 包装
 */
public class TomcatTest {
    private static int PORT = 8081;
    //上下文路径，项目名
    private static String CONTEX_PATH = "/tomcat";
    private static String SERVLET_NAME = "indexServlet";

    public static void main(String[] args) throws LifecycleException {
        //创建Tomcat服务器
        Tomcat tomcatServer = new Tomcat();
        //设置端口号
        tomcatServer.setPort(PORT);
        //设置是否自动部署
        tomcatServer.getHost().setAutoDeploy(false);

        //创建上下文
        StandardContext standardContext = new StandardContext();
        standardContext.setPath(CONTEX_PATH);
        //监听上下文
        standardContext.addLifecycleListener(new Tomcat.FixContextListener());
        //Tomcat容器添加standardContext
        tomcatServer.getHost().addChild(standardContext);

        //这两步等价于web.xml中的<servlet>和<servlet-mapping>配置
        //创建Servlet
        tomcatServer.addServlet(CONTEX_PATH,SERVLET_NAME,new IndexServlet());
        //Servlet映射
        standardContext.addServletMappingDecoded("/index",SERVLET_NAME);

        tomcatServer.start();
        System.out.println("Tomcat启动成功...");

        //异步接收请求
        tomcatServer.getServer().await();

    }
}
