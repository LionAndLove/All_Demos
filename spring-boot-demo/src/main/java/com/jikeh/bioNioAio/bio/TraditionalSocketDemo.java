package com.jikeh.bioNioAio.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 传统Socket阻塞案例：Telnet客户端远程连接测试服务端(telnet localhost 7777; 然后，ctrl+])
 */
public class TraditionalSocketDemo{
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(7777);
		System.out.println("服务端启动...");
		while(true){
			// 获取socket套接字
			// 1、accept()阻塞点(等待客户端的连接)
			Socket socket = serverSocket.accept();
			System.out.println("有新客户端连接上来了...");
			// 获取客户端输入流
			InputStream is = socket.getInputStream();
			byte[] b = new byte[1024];
			while(true){
				// 循环读取数据
				// 2、read() 阻塞点(等待客户端的输入)
				int data = is.read(b);
				if(data != -1){
					String info = new String(b,0,data,"GBK");
					System.out.println(info);
				}else{
					break;
				}
			}
		}
	}
}
