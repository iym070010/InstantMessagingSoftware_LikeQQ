package com.qq.view.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import com.qq.view.util.Config;
/**
 * 
 * 	���շ�������ת��������Ϣ
 * @author ����ʿ	
 *
 */
public class MessageService extends Thread{

	private DatagramSocket client = null;
	
	public void run() {
		
		while(true) {
			try {
				byte[] bytes = new byte[1024*32];
				DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length,
						InetAddress.getByName(Config.IP),Config.Msg_PORT);
				client.receive(datagramPacket);
				
				// ���յ���Ϣ�洢����Ϣջ��
				MessageStack.getMessageStack().addMessage(new String(datagramPacket.getData(),
						0,datagramPacket.getData().length));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public MessageService(DatagramSocket client) {
		this.client = client;
		this.start();
	}
	
}
