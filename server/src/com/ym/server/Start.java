package com.ym.server;

public class Start {


	public static void main(String[] args) {

		new Thread() {	//登陆线程
			
			public void run() {
				
				try {
					System.out.println("登陆服务器启动成功!");
					Loginserver.openServer();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}.start();
		
		new Thread() {	//注册线程
			
			public void run() {
				
				try {
					System.out.println("注册服务器启动成功!");
					ResServer.openServer();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}.start();
		
		new Thread() {	//消息线程
			
			public void run() {
				
				try {
					System.out.println("信息中转服务器启动成功!");
					UDPMessageServer.openServer();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}.start();
		
	}

}



