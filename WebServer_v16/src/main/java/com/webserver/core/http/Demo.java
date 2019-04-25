package com.webserver.core.http;

public class Demo {
	public static void main(String[] args) {
		//获取文件名后缀的部分
		String fileName = "lo.g.png";
		String s = fileName.substring(fileName.lastIndexOf('.')+1);
		System.out.println(s);
		
		String[] s2 = fileName.split("\\.");
		System.out.println(s2[s2.length-1]);
	}
}	
