package com.webserver.servlets;

import java.io.RandomAccessFile;
/**
 * raf读文件demo
 * @author lenovo
 *
 */
public class ReadUserdatDemo {
	public static void main(String[] args) {
		try(RandomAccessFile raf = new RandomAccessFile("user.dat", "r")) {
			for(int i=0;i<raf.length()/100;i++){
				raf.seek(i*100);
				byte[] data = new byte[32];
				raf.read(data);
				String s1 = new String(data,"utf-8").trim();
				raf.read(data);
				String s2 = new String(data,"utf-8").trim();
				System.out.println(s1+","+s2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
