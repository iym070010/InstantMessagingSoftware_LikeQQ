package com.ym.server;

public class Start {


	public static void main(String[] args) {

		new Thread() {	//��½�߳�
			
			public void run() {
				
				try {
					System.out.println("��½�����������ɹ�!");
					Loginserver.openServer();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}.start();
		
		new Thread() {	//ע���߳�
			
			public void run() {
				
				try {
					System.out.println("ע������������ɹ�!");
					ResServer.openServer();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}.start();
		
		new Thread() {	//��Ϣ�߳�
			
			public void run() {
				
				try {
					System.out.println("��Ϣ��ת�����������ɹ�!");
					UDPMessageServer.openServer();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}.start();
		
	}

}



