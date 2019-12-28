package com.qq.view.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.qq.view.util.Config;

import net.sf.json.JSONObject;

/**
 * 
 * 	�����������������
 * @author ����ʿ
 *
 */

public class MessageRegService extends Thread{

	private DatagramSocket client = null;
	
	//ÿ10s�����������������
	public void run() {
		
		String uid = JSONObject.fromObject(Config.personality_json_data).getString("uid");
		String jsonstr = "{\"type\":\"reg\",\"myUID\":\""+uid+"\"}";
		byte[] bytes = jsonstr.getBytes();
		while(true) {

			try {
				DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length,
						InetAddress.getByName(Config.IP),Config.Msg_PORT);
				
				//�����������͸�������
				client.send(datagramPacket);
				Thread.sleep(9999); 	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public MessageRegService(DatagramSocket client) {
		this.client = client;
		this.start();
	}
	
}
