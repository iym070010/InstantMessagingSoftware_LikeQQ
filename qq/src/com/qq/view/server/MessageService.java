package com.qq.view.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import com.qq.view.util.Config;
/**
 * 
 * 	接收服务器中转过来的消息
 * @author 黄龙士	
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
				
				// 接收的消息存储至消息栈里
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
