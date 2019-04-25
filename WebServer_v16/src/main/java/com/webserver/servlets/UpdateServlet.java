package com.webserver.servlets;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.webserver.core.http.HttpRequest;
import com.webserver.core.http.HttpResponse;

/**
 * 修改密码业务
 * 
 * @author lenovo
 *
 */
public class UpdateServlet extends HttpServlet {
	public void service(HttpRequest request, HttpResponse response) {
		// 获取用户输入的用户名，原始密码和新密码
		String username = request.getParameter("username");
		String oldPassword = request.getParameter("oldpassword");
		String newPassword = request.getParameter("newpassword");

		// 匹配用户是否存在
		boolean flag = false;
		// 查询数据库信息
		try (RandomAccessFile raf = new RandomAccessFile("user.dat", "rw")) {
			for (int i = 0; i < raf.length() / 100; i++) {
				System.out.println("文件指针：" + raf.getFilePointer());
				// 移动指针到下一条记录
				raf.seek(i * 100);
				byte[] data = new byte[32];
				raf.read(data);
				String uName = new String(data, "utf-8").trim();
				if (uName.equals(username)) {
					flag = true;// 执行到这说明有此用户，修改标记
					raf.read(data);
					String pWord = new String(data, "utf-8").trim();
					if (pWord.equals(oldPassword)) {
						// 指针移动到密码位置
						raf.seek(i * 100 + 32);
						// 将新密码写入到文件中
						raf.write(Arrays.copyOf(newPassword.getBytes("utf-8"), 32));
						// 响应密码修改成功
						forward("/myweb/update_success.html", request, response);
					} else {
						// 响应密码错误页面
						forward("/myweb/update_fail.html", request, response);
					}
					break; // 结束查找
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!flag) {
			// 响应用户不存在
			forward("/myweb/no_user.html", request, response);
		}
	}
}
